package javasensei.db.managments;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javasensei.db.Connection;
import javasensei.util.FileHelper;

/**
 *
 * @author oramas
 */
public class ExamenesManager {
    
    private static final DBCollection pretestCollection = Connection.getCollection().get(CollectionsDB.PRETEST);
    
    public String calificarExamenPreTest(String idFacebook, String jsonRespuestas){
        
         DBObject jsonRespuestasPreTest = (DBObject)JSON.parse(FileHelper.getInstance()
                    .getContentFile("examenes/respuestas_pretest.json"));
    
        double cantidadPreguntas = jsonRespuestasPreTest.keySet().size();
        
        BasicDBList examen = (BasicDBList)com.mongodb.util.JSON.parse(jsonRespuestas);
        
        double totalRespuestasCorrectas = 0;
        
        for(Object jsonRespuesta : examen.toArray()) {
            DBObject json = (DBObject) jsonRespuesta;
            
            String pregunta = json.get("pregunta").toString();
            String respuesta = json.get("respuesta").toString();
            
            if (jsonRespuestasPreTest.get(pregunta).equals(respuesta)){
                totalRespuestasCorrectas++;
            }
        }
        
        double promedio = totalRespuestasCorrectas / cantidadPreguntas;
        
        
        DBObject insert = new BasicDBObject();
        insert.put("idFacebook", idFacebook);
        insert.put("examen", examen);
        insert.put("totalRespuestasCorrectas", totalRespuestasCorrectas);
        insert.put("promedio", promedio);
        insert.put("cantidadPreguntas", cantidadPreguntas);
        LocalDateTime dateTime = LocalDateTime.now();
        insert.put("fecha", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        insert.put("hora", dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        
        pretestCollection.save(insert);
        
        return insert.toString();
    }

    public String realizoExamenPreTest(String idFacebook) {
        DBObject result = pretestCollection.findOne(
                new BasicDBObject("idFacebook", idFacebook)
        );
        
        return new BasicDBObject("realizado", result!=null).toJson();
    }
}
