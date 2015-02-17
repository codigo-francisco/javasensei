package javasensei.db.managments;

import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.connection.Connection;
import javasensei.dominio.kws.Curso;
import javasensei.dominio.kws.Leccion;
import javasensei.util.FileHelper;

/**
 *
 * @author chess
 */
public class CursoManager {
    private final DBCollection cursoCollection = Connection.getDb().getCollection("cursos");
    
    private final Curso curso;
    private final Gson gson;
   
    
    public CursoManager() {
        gson = new Gson();
        
        //TODO: Esto no deberia cargar desde un archivo
        curso = gson.fromJson(FileHelper.getInstance().getContentFile("javasensei/files/ejercicios.json"), Curso.class);
        //System.out.println(toJSON());
    }
    
    public String toJSON() {
        return gson.toJson(curso);
    }
    
    public void insert() {
        for(Leccion item: curso.getLecciones()) {
                insert(item);
            }
    }
    
    public boolean insert(Leccion leccion) {
        try {
            DBObject dbObject = leccion.convertToDBObject();
            dbObject.put("id", leccion.getId());
            dbObject.put("titulo", leccion.getTitulo());
            dbObject.put("ejercicios", leccion.getEjercicios());

            cursoCollection.insert(dbObject);
            //quizCollections.getMongo().close();
            return true;
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }
        return false;
    }

    public String getCurso() {
        StringBuilder leccion = new StringBuilder();
        
        DBCursor cur = cursoCollection.find();

        while (cur.hasNext()) {
            leccion.append(cur.next());
        }
        return leccion.toString();
    }

    public String quizToJSON() {
        return gson.toJson(getCurso());
    }
    
    
}
