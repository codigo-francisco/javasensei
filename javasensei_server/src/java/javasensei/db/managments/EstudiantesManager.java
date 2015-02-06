package javasensei.db.managments;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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
        //Tramos los ejercicios del alumno

        DBObject alumno = alumnosCollection.findOne(QueryBuilder.start("id")
                .is(estudiante.getId())
                .get(),
                QueryBuilder.start("_id").is(0)
                .put("ejercicios").is(1)
                .get()
        );
        
        if (alumno!=null){
            BasicDBList ejercicios;
            //List<DBObject> ejerciciosAlumno
            
            if (alumno.containsField("ejercicios"))
                ejercicios = ((BasicDBList)alumno.get("ejercicios"));
            else
                ejercicios = new BasicDBList(); //Arreglo vacio
            
            
            //El campo ejercicio se llena con los id que no tenga el usuario (de ejercicios)
            //Ademas se pone un valor 0 para entender que no esta terminado
            
            
        }

        return "";
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
