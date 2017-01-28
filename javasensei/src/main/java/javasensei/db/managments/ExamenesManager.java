package javasensei.db.managments;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javasensei.db.Connection;
import javasensei.util.FileHelper;

/**
 *
 * @author oramas
 */
public class ExamenesManager {
    
    public enum TipoExamen{
        PRETEST,
        POSTTEST
    }

    public static final DBCollection pretestCollection = Connection.getCollection().get(CollectionsDB.PRETEST);
    public static final DBCollection posttestCollection = Connection.getCollection().get(CollectionsDB.POSTTEST);
    
    private static final Map<String,DBObject> examenesPreTest = new HashMap<>();
    private static final Map<String,DBObject> examenesPostTest = new HashMap<>();
    //Respuestas de los cuestionarios
    static{
        //Pretest
        examenesPreTest.put("A", (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_pretest.json")));
        
        examenesPreTest.put("B", (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_pretestB.json")));
        
        examenesPreTest.put("C", (DBObject) (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_posttest.json")));
        
        //Posttest
        examenesPostTest.put("A", (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_posttest.json")));
        examenesPostTest.put("B", (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_posttestB.json")));
        examenesPostTest.put("C", (DBObject) JSON.parse(FileHelper.getInstance()
                .getContentFile("examenes/respuestas_posttest.json")));        
    }
    
    public String calificarExamen(String idFacebook, String jsonRespuestas, String tipoCuestionario, TipoExamen tipoExamen) {

        Map<String, DBObject> coleccionRespuestas = null;
                
        switch(tipoExamen){
            case PRETEST:
                coleccionRespuestas = examenesPreTest;
                break;
            case POSTTEST:
                coleccionRespuestas = examenesPostTest;
                break;
        }
        
        DBObject jsonRespuestasBuenas = coleccionRespuestas.get(tipoCuestionario);

        DBObject respuestasBuenasDiagnostico = (DBObject)jsonRespuestasBuenas.get("diagnostico");
        double cantidadPreguntasDiagnostico = respuestasBuenasDiagnostico.keySet().size();
        DBObject respuestasBuenasPrueba = (DBObject)jsonRespuestasBuenas.get("prueba");
        double cantidadPreguntasPrueba = (respuestasBuenasPrueba).keySet().size();
        double cantidadTotalPreguntas = cantidadPreguntasDiagnostico+cantidadPreguntasPrueba;

        BasicDBList examen = (BasicDBList) com.mongodb.util.JSON.parse(jsonRespuestas);

        DBObject insert = new BasicDBObject();

        if (examen.size() > 0) {

            double totalRespuestasCorrectasDiagnostico = 0;
            double totalRespuestasCorrectasPrueba = 0;

            for (Object jsonRespuesta : examen.toArray()) {
                DBObject json = (DBObject) jsonRespuesta;
                
                String pregunta = json.get("pregunta").toString();
                String respuesta = json.get("respuesta").toString();

                if (pregunta.startsWith("d")){
                    if (respuestasBuenasDiagnostico.get(pregunta).equals(respuesta)){
                        totalRespuestasCorrectasDiagnostico++;
                    }
                }else if (pregunta.startsWith("q")){
                    if (respuestasBuenasPrueba.get(pregunta).equals(respuesta)){
                        totalRespuestasCorrectasPrueba++;
                    }
                }
            }

            double promedioDiagnostico = totalRespuestasCorrectasDiagnostico / cantidadPreguntasDiagnostico;
            double promedioPrueba = totalRespuestasCorrectasPrueba / cantidadPreguntasPrueba;

            insert.put("idFacebook", idFacebook);
            insert.put("examen", examen);
            insert.put("tipoExamen", tipoCuestionario);
            insert.put("totalRespuestasCorrectasDiagnostico", totalRespuestasCorrectasDiagnostico);
            insert.put("promedioDiagnostico", promedioDiagnostico);
            insert.put("cantidadPreguntasDiagnostico", cantidadPreguntasDiagnostico);
            insert.put("totalRespuestasCorrectasPrueba", totalRespuestasCorrectasPrueba);
            insert.put("promedioPrueba", promedioPrueba);
            insert.put("cantidadPreguntasPrueba", cantidadPreguntasPrueba);
            insert.put("cantidadTotalPreguntas", cantidadTotalPreguntas);
            LocalDateTime dateTime = LocalDateTime.now();
            insert.put("fecha", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
            insert.put("hora", dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
            
            if(tipoExamen == TipoExamen.PRETEST){
                pretestCollection.save(insert);
            }else if (tipoExamen == TipoExamen.POSTTEST){
                posttestCollection.save(insert);
            }
        }

        return insert.toString();
    }
    
    public String realizoExamen(String idFacebook, DBCollection examenCollection) {
        DBObject result = examenCollection.findOne(
                new BasicDBObject("idFacebook", idFacebook)
        );
        return new BasicDBObject("realizado", result != null).toJson();
    }
}
