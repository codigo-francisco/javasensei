package javasensei.db.managments;

import com.google.gson.Gson;
import javasensei.db.collections.QuizCollection;
import javasensei.dominio.kws.Curso;
import javasensei.util.FileHelper;

/**
 *
 * @author chess
 */
public class CursoManager {
    private final QuizCollection quizCollections = new QuizCollection();    
    
    private final Curso curso;
    private final Gson gson = new Gson();
   
    
    public CursoManager() {
        curso = gson.fromJson(FileHelper.getInstance().getContentFile("javasensei/files/ejercicios.json"), Curso.class);
        System.out.println(toJSON());
    }
    
    public String toJSON() {
        return gson.toJson(curso);
    }
    
    
}
