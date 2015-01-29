package javasensei.db.collections;

import com.mongodb.DBCollection;
import javasensei.db.connection.Connection;

public class QuizCollection extends Connection{
    
    public DBCollection getQuizCollection(){
        return db.getCollection("quizzes204");
    }
}
