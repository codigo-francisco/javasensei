package javasensei.db.collections;
import com.mongodb.DBCollection;
import javasensei.db.connection.Connection;
/**
 *
 * @author Rock
 */
public class AlumnosCollection extends Connection{
    
    public DBCollection getAlumnosCollection(){
        return db.getCollection("alumnos");
    }
}
