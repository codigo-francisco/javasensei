
package javasensei.db.collections;

import com.mongodb.DBCollection;
import javasensei.db.connection.Connection;

public class CursoCollection extends Connection {
    
    public DBCollection getCursoCollection(){
        return db.getCollection("curso_java");
    }
}
