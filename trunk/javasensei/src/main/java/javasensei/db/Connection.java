package javasensei.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.managments.CollectionsDB;

/**
 *
 * @author Rock
 */
public class Connection {
    protected static Mongo mongo;
    protected static DB db;
    
    /*public static Mongo getMongo(){
        return mongo;
    }
    
    public static DB getDb(){
        return Connection.db;
    }*/
    
    private static Map<CollectionsDB,DBCollection> collections;
    
    public static Map<CollectionsDB, DBCollection> getCollection(){
        return collections;
    }
    
    static {
        try {
            mongo = new MongoClient();
            mongo.setWriteConcern(WriteConcern.SAFE);
            db = mongo.getDB("java_sensei");
            
            collections = new HashMap<>();
            collections.put(CollectionsDB.ALUMNOS, db.getCollection("alumnos"));
            collections.put(CollectionsDB.EJERCICIOS, db.getCollection("ejercicios"));
            collections.put(CollectionsDB.LECCIONES, db.getCollection("lecciones"));
            collections.put(CollectionsDB.RANKING_EJERCICIOS, db.getCollection("ranking_ejercicios"));
            collections.put(CollectionsDB.RANKING_RECURSOS, db.getCollection("ranking_recursos"));
            collections.put(CollectionsDB.RECURSOS, db.getCollection("recursos"));
            collections.put(CollectionsDB.TEMAS, db.getCollection("temas"));
        } catch (UnknownHostException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
