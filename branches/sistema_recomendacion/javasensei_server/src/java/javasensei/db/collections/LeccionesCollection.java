package javasensei.db.collections;

import com.mongodb.DBCollection;
import javasensei.db.connection.Connection;

/**
 *
 * @author Rock
 */
public class LeccionesCollection extends Connection {
    public DBCollection getLeccionesCollection(){
        return db.getCollection("lecciones");
    }
}
