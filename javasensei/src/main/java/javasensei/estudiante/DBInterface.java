package javasensei.estudiante;

import com.mongodb.DBObject;

/**
 *
 * @author Rock
 */
public interface DBInterface {
    DBObject convertToDBObject(boolean save);
    DBObject convertToDBObject();
}
