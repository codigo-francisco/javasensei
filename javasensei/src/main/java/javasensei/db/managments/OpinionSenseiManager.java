package javasensei.db.managments;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javasensei.db.Connection;

/**
 *
 * @author oramas
 */
public class OpinionSenseiManager {
    private static final DBCollection evalSenseiCollection = Connection.getCollection().get(CollectionsDB.OPINION_SENSEI);
    
    public String saveOpinionSensei(String idFacebook, String jsonRespuestas) {
        BasicDBList evaluacion = (BasicDBList) com.mongodb.util.JSON.parse(jsonRespuestas);
        DBObject insert = new BasicDBObject();
        
        if (evaluacion.size() > 0) {
            insert.put("idFacebook", idFacebook);
            insert.put("evaluacion", evaluacion);
            LocalDateTime dateTime = LocalDateTime.now();
            insert.put("fecha", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
            insert.put("hora", dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));

            evalSenseiCollection.save(insert);
        }
        return insert.toString();
    }
    
    public String realizoEvalSensei(String idFacebook) {
        DBObject result = evalSenseiCollection.findOne(
                new BasicDBObject("idFacebook", idFacebook)
        );
        return new BasicDBObject("realizado", result != null).toJson();
    }
    
    
}
