package pruebitas;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javasensei.db.managments.CursoManager;
import javasensei.db.managments.QuizManager;
import javasensei.dominio.kws.Ejercicio;

public class TestMongo {
//    public static void main(String[] args) {
//        example3();
//    }
//    
//    public static void example1() {
//        try {
//            Mongo mongo = new Mongo("localhost", 27017);
//            DB db = mongo.getDB("testdb");
//            DBCollection table = db.getCollection("oramas");
//
//            BasicDBObject document = new BasicDBObject();
//            document.put("nombre", "Raul");
//            document.put("edad", 40);
//            table.insert(document);
//
//            BasicDBObject searchQuery = new BasicDBObject();
//            searchQuery.put("edad", 40);
//            DBCursor cursor = table.find(searchQuery);
//
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next());
//            }
//
//            BasicDBObject query = new BasicDBObject();
//            query.put("nombre", "Raul");
//
//            BasicDBObject newDocument = new BasicDBObject();
//            newDocument.put("nombre", "Raul Oramas");
//
//            BasicDBObject updateObj = new BasicDBObject();
//            updateObj.put("$set", newDocument);
//
//            table.update(query, updateObj);
//
//            BasicDBObject searchQuery2
//                    = new BasicDBObject().append("nombre", "Raul Oramas");
//
//            DBCursor cursor2 = table.find(searchQuery2);
//
//            while (cursor2.hasNext()) {
//                System.out.println(cursor2.next());
//            }
//
//            System.out.println("Done");
//
//        } catch (UnknownHostException | MongoException e) {
//            System.out.println("Error");
//        }
//    }
//    
//    public static void example2() {
//        try {
//            QuizManager qm = new QuizManager();
//            
//            ArrayList<Ejercicio> ejercicios = new ArrayList();
//            ejercicios.add(new Ejercicio(1,"1","1"));
//            ejercicios.add(new Ejercicio(2,"2","2"));
//            ejercicios.add(new Ejercicio(3,"3","3"));
//            
//            ejercicios.stream().forEach((item) -> {
//                System.out.println(qm.insert(item) + item.toString());
//            }); 
//
//            System.out.println(qm.getReactivos());
//            System.out.println(qm.quizToJSON());
//        } catch (MongoException e) {
//            System.out.println("Error");
//        }
//    }
//    
//    public static void example3() {
//        CursoManager cm = new CursoManager();
//        //cm.insert();
//        System.out.println(cm.getCurso());
//    }
}
