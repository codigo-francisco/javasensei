package javasensei.db.collections;

import com.mongodb.DBCollection;
import javasensei.db.connection.Connection;

/**
 *
 * @author PosgradoMCC
 */
public class TemasCollection extends Connection{
    
    public DBCollection getTemasCollection(){
        return db.getCollection("temas");
    }
    
}
