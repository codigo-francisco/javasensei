package javasensei.db.managments;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.ReadPreference;
import com.mongodb.WriteResult;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.Connection;
import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author Rock
 */
public class EstudiantesManager {

    private DBCollection alumnosCollection = Connection.getCollection().get(CollectionsDB.ALUMNOS);
    private DBCollection temasCollection = Connection.getCollection().get(CollectionsDB.TEMAS);
    private DBCollection ejerciciosCollection = Connection.getCollection().get(CollectionsDB.EJERCICIOS);

    private ModeloEstudiante estudiante;

    public EstudiantesManager(ModeloEstudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public Boolean finalizarEjercicio(int idEjercicio) {
        return finalizarEjercicio(idEjercicio, 1);
    }

    public Boolean finalizarEjercicio(int idEjercicio, double valor) {
        boolean result = false;
        System.out.println("valor para terminado: "+valor);
        try {

            WriteResult writeResult = alumnosCollection.update(
                    QueryBuilder.start("id").is(estudiante.getId())
                    .put("ejercicios.id").is(idEjercicio)
                    .get(),
                    QueryBuilder.start("$set").is(
                            QueryBuilder.start("ejercicios.$.terminado").is(valor) //valor depende de como se respondio(suboptimo(.7) ó optimo(1))
                            .get()
                    )
                    .get()
            );

            result = writeResult.getN() > 0;

        } catch (Exception ex) {
            System.err.println(ex);
        }

        return result;
    }

    public synchronized String insertOrCreateStudent() {
        String result = "{}";

        try {
            synchronized (alumnosCollection) {
                //Buscamos el id de facebook, en caso de no existir, creamos el objeto y obtenemos el nuevo id
                DBObject dbObject = alumnosCollection.findOne(
                        new BasicDBObject("idFacebook", estudiante.getIdFacebook())
                );
                if (dbObject != null) {
                    estudiante.setId(new Double(dbObject.get("id").toString()).longValue());
                    dbObject.put("token", estudiante.getToken()); //Se actualiza el token de facebook
                } else { //El estudiante es nuevo
                    estudiante.setId(alumnosCollection.count());
                    dbObject = estudiante.convertToDBObject(); //True para guardar
                }

                //Se actualizan o insertan los cambios
                WriteResult write = alumnosCollection.save(dbObject);

                dbObject.removeField("ejercicios");

                result = dbObject.toString();

            }
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

    public boolean createOrUpdateStudentModel() {
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

                for (Object objEjercicio : listEjercicios) {
                    ejercicios.add(new Double(((DBObject) objEjercicio).get("id").toString()).intValue());
                }
            }

            //El campo ejercicio se llena con los id que no tenga el usuario (de ejercicios)
            //Ademas se pone un valor 0 para entender que no esta terminado
            //Id de ejercicios que el alumno no tiene
            DBCursor ejerciciosCursor = ejerciciosCollection.find(QueryBuilder.start("id")
                    .notIn(ejercicios).get(),
                    QueryBuilder.start("_id").is(0)
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

    public void saveAbilityGlobal(Double value) {
        alumnosCollection.update(QueryBuilder.start("id").is(estudiante.getId()).get(),
                QueryBuilder.start("$set").is(
                        QueryBuilder.start("habilidadGlobal").is(value).get()
                ).get()
        );
    }

    public void saveAbilityGlobal() {
        saveAbilityGlobal(getAbilityGlobal());
    }
}
