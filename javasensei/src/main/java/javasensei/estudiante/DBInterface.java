package javasensei.estudiante;

import com.mongodb.DBObject;

/**
 *
 * @author Rock
 */
public interface DBInterface extends javasensei.db.DBInterface {
    DBObject convertToDBObject(boolean save);
}
