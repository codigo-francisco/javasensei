// Clase de prueba.
package javasensei.db.managments;

import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.collections.QuizCollection;
import javasensei.dominio.kws.Ejercicio;

/**
 *
 * @author chess
 */
public class QuizManager {

    private final QuizCollection quizCollections = new QuizCollection();

    //private Ejercicio ejercicio;
    private final Gson gson = new Gson();

    public boolean insert(Ejercicio ejercicio) {
        try {
            DBCollection collection = quizCollections.getQuizCollection();
            DBObject dbObject = ejercicio.convertToDBObject();
            dbObject.put("id", ejercicio.getId());
            dbObject.put("url", ejercicio.getUrl());
            dbObject.put("titulo", ejercicio.getTitulo());

            collection.insert(dbObject);
            //quizCollections.getMongo().close();
            return true;
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }
        return false;
    }

    public String getReactivos() {
        StringBuilder reactivo = new StringBuilder();
        DBCollection tabla = quizCollections.getQuizCollection();
        DBCursor cur = tabla.find();

        while (cur.hasNext()) {
            reactivo.append(cur.next());
        }
        return reactivo.toString();
    }

    public String quizToJSON() {
        return gson.toJson(getReactivos());
    }

}
