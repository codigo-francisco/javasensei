package javasensei.db.managments;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.ReadPreference;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.Connection;
import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author Rock
 */
public class EstudiantesManager {

    private final DBCollection alumnosCollection = Connection.getCollection().get(CollectionsDB.ALUMNOS);
    private final DBCollection ejerciciosCollection = Connection.getCollection().get(CollectionsDB.EJERCICIOS);
    private final DBCollection bitacoraVisitas = Connection.getCollection().get(CollectionsDB.BITACORA_VISITAS);

    private ModeloEstudiante estudiante;

    public EstudiantesManager(){
        
    }
    
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
                
                //Se registra el ingreso del estudiante
                registrarVisita("entrada");

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
    
    public String registrarVisita(String tipoEntrada){
        DBObject objVisita = new BasicDBObject();
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        
        objVisita.put("hora", time.format(DateTimeFormatter.ISO_TIME));
        objVisita.put("fecha", date.format(DateTimeFormatter.ISO_DATE));
        objVisita.put("id", estudiante.getId());
        objVisita.put("tipo", tipoEntrada);
        
        bitacoraVisitas.insert(
                objVisita
        );
        
        return objVisita.toString();
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
            List<Integer> ejerciciosAlumno = new ArrayList<>();
            //List<DBObject> ejerciciosAlumno = new ArrayList<>();

            if (alumno.containsField("ejercicios")) {
                BasicDBList listEjercicios = (BasicDBList) alumno.get("ejercicios");

                for (Object objEjercicio : listEjercicios) {
                    ejerciciosAlumno.add(new Double(((DBObject) objEjercicio).get("id").toString()).intValue());
                }
            }
            
            List<Integer> ejercicios = new ArrayList<>();
            
            ejerciciosCollection.find(
                    new BasicDBObject(),
                    (BasicDBObject)new BasicDBObject("_id", 0)
                    .put("id", 1)
            ).toArray().forEach((Object t) -> {
                ejercicios.add(new Double(((DBObject)t).get("id").toString()).intValue());
            });
            
            //Removemos los ejercicios que ya se encuentran con el alumno
            List<Integer> ejerciciosSobrantes = new ArrayList<>(ejerciciosAlumno);
            ejerciciosSobrantes.removeAll(ejercicios);
            
            //Los ejercicios restantes son removidos
            alumnosCollection.update(new BasicDBObject("id", estudiante.getId()), 
                    new BasicDBObject("$pull",
                        new BasicDBObject("ejercicios",
                            new BasicDBObject("id",
                                new BasicDBObject("$in",ejerciciosSobrantes)
                            )
                        )
                    )
            );
            
            //El campo ejercicio se llena con los id que no tenga el usuario (de ejercicios)
            //Ademas se pone un valor 0 para entender que no esta terminado
            //Id de ejercicios que el alumno no tiene
            DBCursor ejerciciosCursor = ejerciciosCollection.find(QueryBuilder.start("id")
                    .notIn(ejerciciosAlumno).get(),
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
    
    public Boolean saveActivarEmociones(){
        boolean result=false;
        
        alumnosCollection.update(
                new BasicDBObject("id", estudiante.getId()), 
                new BasicDBObject("$set", 
                        new BasicDBObject("activarEmociones",
                                estudiante.getActivarEmociones())
                )
        );
        
        return result;
    }
    
    public void guardarCondiciones(){
        alumnosCollection.update(
                new BasicDBObject("id", estudiante.getId()), 
                new BasicDBObject("$set", 
                        new BasicDBObject("aceptarCondiciones",
                                true)
                )
        );
    }
    
    public void modificarPropiedades(String json){
        DBObject query = new BasicDBObject();
        BasicDBObject object = (BasicDBObject)JSON.parse(json);
        
        query.put("id", object.getLong("id"));
        
        DBObject update = new BasicDBObject();
                
        for(String key : object.keySet()){
           update.put(key, object.get(key));
        }
        
        DBObject set = new BasicDBObject("$set", update);
        
        alumnosCollection.update(query, set);
    }
}
