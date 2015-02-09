package javasensei.db.managments;

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

    public String finalizarEjercicio(int idEjercicio) {
        WriteResult writeResult = alumnosCollection.update(
                QueryBuilder.start("id").is(estudiante.getId())
                .put("ejercicios.id").is(idEjercicio)
                .get(),
                QueryBuilder.start("$set").is(
                        QueryBuilder.start("ejercicios.$.terminado").is(1) //1 es que termino el ejercicio
                        .get()
                )
                .get()
        );        

        return String.format("{result:%s}", writeResult.getN() > 0);
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

    public Double getAbilityGlobal() {
        double result = 0;

        DBObject alumno = alumnosCollection.findOne(new BasicDBObject("id", estudiante.getId()));

        if (alumno != null) {
            //Obtenemos los ejercicios del modelo del estudiante
            BasicDBList ejercicios = (BasicDBList) alumno.get("ejercicios");
            Double totalEjercicios = new Integer(ejercicios.size()).doubleValue();
            Double ejerciciosRealizados = 0D;

            for (Object ejercicio : ejercicios) {
                DBObject ejercicioObject = (DBObject) ejercicio;

                if (Double.parseDouble(ejercicioObject.get("terminado").toString()) == 1) {
                    ejerciciosRealizados++;
                }
            }

            result = ejerciciosRealizados / totalEjercicios;
        }

        return result;
    }

    public boolean createOrUpdateDomainModel() {
        boolean result = false;

        //Tramos los ejercicios del alumno
        DBObject alumno = alumnosCollection.findOne(QueryBuilder.start("id")
                .is(estudiante.getId())
                .get(),
                QueryBuilder.start("_id").is(0)
                .put("ejercicios").is(1)
                .get()
        );

        if (alumno != null) {
            List<Integer> ejercicios = new ArrayList<>();
            //List<DBObject> ejerciciosAlumno = new ArrayList<>();

            if (alumno.containsField("ejercicios")) {
                BasicDBList listEjercicios = (BasicDBList) alumno.get("ejercicios");

                listEjercicios.stream().map((listEjercicio) -> (DBObject) listEjercicio).forEach((ejercicio) -> {
                    ejercicios.add(((Double) ejercicio.get("id")).intValue());
                });

            }

            //El campo ejercicio se llena con los id que no tenga el usuario (de ejercicios)
            //Ademas se pone un valor 0 para entender que no esta terminado
            //Id de ejercicios que el alumno no tiene
            DBCursor ejerciciosCursor = ejerciciosCollection.find(QueryBuilder.start("id")
                    .notIn(ejercicios).get(),
                    QueryBuilder.start("_id").is(0)
                    .put("idLeccion").is(1)
                    .put("idTema").is(1)
                    .put("id").is(1)
                    .get()
            );

            while (ejerciciosCursor.hasNext()) {
                DBObject objectEjercicio = ejerciciosCursor.next();
                objectEjercicio.put("terminado", 0);
                //ejerciciosAlumno.add(objectEjercicio);

                alumnosCollection.update(new BasicDBObject("id", estudiante.getId()),
                        QueryBuilder.start("$addToSet").is(
                                QueryBuilder.start("ejercicios")
                                .is(objectEjercicio)
                                .get()
                        ).get()
                );
            }

            result = true;
        }

        return result;
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
