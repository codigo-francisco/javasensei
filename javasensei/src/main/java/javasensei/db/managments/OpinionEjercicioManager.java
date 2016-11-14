package javasensei.db.managments;

import javasensei.db.Connection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author oramas
 */
public class OpinionEjercicioManager {
    private static final DBCollection opinionCollection = Connection.getCollection().get(CollectionsDB.OPINION_EJERCICIO);
    
    public String saveOpinionEjercicio(String idFacebook, String idEjercicio, String opinion, String polaridad) {
        DBObject insert = new BasicDBObject();
        
        insert.put("idFacebook", idFacebook);
        insert.put("idEjercicio", idEjercicio);
        insert.put("opinion", opinion);
        insert.put("polaridad", polaridad);
        LocalDateTime dateTime = LocalDateTime.now();
        insert.put("fecha", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        insert.put("hora", dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));

        //falta determinar la opinion eval
        
        opinionCollection.save(insert);
        
        return insert.toString();
    }
    
    
}
