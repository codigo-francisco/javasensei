package javasensei.db.managments;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javasensei.db.collections.QuizCollection;
import javasensei.dominio.kws.Quiz;

/**
 *
 * @author chess
 */
public class QuizManager {
    private final QuizCollection quizCollections = new QuizCollection();    
    
    private Quiz quiz;
    private final Gson gson = new Gson();
    
    private final String strJson = 
        "{\"pregunta\":\"Este es un examen diagnostico.\","
       + "\"reactivos\":\"Aqui van un conjunto de reactivos.\"}";
    
    
    //Lee un archivo JSON y lo carga en memoria...marca error
    public void readJSON(File jsonArchivo) throws IOException {  
        
        JsonParser parser = new JsonParser();
        //JsonElement datos = parser.parse(newArchivo.toString());
        //quiz = gson.fromJson(new FileReader(jsonArchivo), Quiz.class);
        quiz = gson.fromJson(strJson, Quiz.class);
        System.out.println(quizToJSON());
    }
    
    // Pasa a un archivo de texto un archivo JSON...no graba el archivo
    public void writeJSON(String archivo) throws IOException {
        File newArchivo = new File(archivo);
        FileWriter writer = new FileWriter(newArchivo.toString());
        writer.write(quizToJSON());
        System.out.println(quizToJSON()); 
    } 
   
    public void addQuestion() {     
        //quiz = new Quiz("Pregunta 12", "Respuesta 12");  
        
        BasicDBObject document = new BasicDBObject();
        document.put("pregunta", quiz.getPregunta());
        //document.put("respuesta", quiz.getRespuesta());
        
        DBCollection tabla = quizCollections.getQuizCollection();
        tabla.insert(document);
        System.out.println(document.toString());
    }
    
    /*public void addQuestion(Reactivo reactivo) {
        DBCollection tabla = db.getCollection("quiz");

        BasicDBObject document = new BasicDBObject();
        document.put("pregunta", reactivo.getPregunta());
        String[] opcion = reactivo.getOpcion();
        document.put("opcionA", opcion[0]);
        document.put("opcionB", opcion[1]);
        document.put("opcionC", opcion[2]);
        document.put("opcionD", opcion[3]);
        document.put("respuesta", reactivo.getRespuesta());
        tabla.insert(document);
    }*/

    public String getReactivos() {
        StringBuilder reactivo = new StringBuilder();
        DBCollection tabla = quizCollections.getQuizCollection();
        DBCursor cur = tabla.find();
        Quiz r = new Quiz();
        while (cur.hasNext()) {  
            reactivo.append(cur.next());
        }
        return reactivo.toString();
    }

    public String quizToJSON() {
        return gson.toJson(quiz);
    }
}
