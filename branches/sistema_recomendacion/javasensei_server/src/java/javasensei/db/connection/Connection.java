package javasensei.db.connection;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rock
 */
public class Connection {
    protected static Mongo mongo;
    protected static DB db;
    
    public static Mongo getMongo(){
        return mongo;
    }
    
    public static DB getDb(){
        return Connection.db;
    }
    
    static {
        try {
            mongo = new Mongo(); //Localhost, default port
            mongo.setWriteConcern(WriteConcern.SAFE);
            db = mongo.getDB("java_sensei");
        } catch (UnknownHostException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
