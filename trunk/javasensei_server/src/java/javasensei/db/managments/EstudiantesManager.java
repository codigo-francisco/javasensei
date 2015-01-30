package javasensei.db.managments;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.collections.AlumnosCollection;
import javasensei.db.collections.EjerciciosCollection;
import javasensei.db.collections.TemasCollection;
import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author Rock
 */
public class EstudiantesManager {

    private DBCollection alumnosCollection = new AlumnosCollection().getAlumnosCollection();
    private DBCollection temasCollection = new TemasCollection().getTemasCollection();
    private DBCollection ejerciciosCollection = new EjerciciosCollection().getEjerciciosCollection();

    private ModeloEstudiante estudiante;

    public EstudiantesManager(ModeloEstudiante estudiante) {
        this.estudiante = estudiante;
    }

    public String insertOrCreateStudent() {
        String result = "{}";

        try {
            //Buscamos el id, en caso de no existir, creamos el objeto
            DBObject dbObject = alumnosCollection.findOne(new BasicDBObject("id", estudiante.getId()));
            if (dbObject != null) {
                dbObject.put("token", estudiante.getToken()); //Se actualiza el token de facebook
            } else { //El estudiante es nuevo
                dbObject = estudiante.convertToDBObject();
            }

            //Se actualizan o insertan los cambios
            WriteResult write = alumnosCollection.save(dbObject);

            result = dbObject.toString();

            //userCollections.getMongo().close();
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }

        return result;
    }

    public String createOrUpdateDomainModel() {
        //Lista de temas
        List<DBObject> temas = temasCollection.find(QueryBuilder.start().get(),
                QueryBuilder.start("_id").is(0)
                .put("id").is(1)
                .put("nombre").is(1)
                .get()
        ).toArray();
        
        DBObject ejerciciosOPbject = (DBObject)alumnosCollection.findOne(
                QueryBuilder.start("id").is(estudiante.getId())
                        .get()
        ).get("ejercicios");
        //Obtenemos los id que ya tenga el alumno
        
        
        temas.replaceAll(object -> {
            List<DBObject> ejercicios = ejerciciosCollection.find(
                    QueryBuilder.start("idTema").is(object.get("id"))
                    .get(),
                    QueryBuilder.start("_id").is(0)
                    .put("id").is(1)
                    .get()
            ).toArray();

            ejercicios.replaceAll(ejercicio -> {
                ejercicio.put("terminado", true);

                return ejercicio;
            });

            object.put("ejercicios",
                    ejercicios
            );
            
            return object;

        });
        
        
        
        return temas.toString();
    }

    public boolean updateDataStudent() {
        boolean result = false;

        try {
            alumnosCollection.findAndModify(new BasicDBObject("id", estudiante.getId()), QueryBuilder.start("$set").is(estudiante.convertToDBObject()).get());
            
            result = true;

        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }

        return result;
    }
}
