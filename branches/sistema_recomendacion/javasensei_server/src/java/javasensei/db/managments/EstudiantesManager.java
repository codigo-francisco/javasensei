package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.collections.AlumnosCollection;
import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author Rock
 */
public class EstudiantesManager {

    private AlumnosCollection userCollections = new AlumnosCollection();

    public String insertOrCreateStudent(ModeloEstudiante estudiante) {
        String result = "{}";

        try {
            DBCollection collection = userCollections.getAlumnosCollection();

            //Buscamos el id, en caso de no existir, creamos el objeto
            DBObject dbObject = collection.findOne(new BasicDBObject("id", estudiante.getId()));
            if (dbObject != null) {
                dbObject.put("token", estudiante.getToken()); //Se actualiza el token de facebook
            } else { //El estudiante es nuevo
                dbObject = estudiante.convertToDBObject();
            }

            //Se actualizan o insertan los cambios
            WriteResult write = collection.save(dbObject);

            result = dbObject.toString();

            userCollections.getMongo().close();
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }

        return result;
    }

    public boolean updateDataStudent(ModeloEstudiante estudiante) {
        boolean result = false;

        DBCollection collection = userCollections.getAlumnosCollection();

        try {
            collection.findAndModify(new BasicDBObject("id", estudiante.getId()), estudiante.convertToDBObject());

            result = true;

        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }

        return result;
    }
}
