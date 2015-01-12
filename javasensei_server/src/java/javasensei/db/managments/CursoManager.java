package javasensei.db.managments;

import com.google.gson.Gson;
import javasensei.db.collections.QuizCollection;
import javasensei.dominio.kws.Quiz;

/**
 *
 * @author chess
 */
public class CursoManager {
    private final QuizCollection quizCollections = new QuizCollection();    
    
    private Quiz quiz;
    private final Gson gson = new Gson();
}
