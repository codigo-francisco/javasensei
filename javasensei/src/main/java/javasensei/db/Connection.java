package javasensei.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javasensei.db.managments.CollectionsDB;

/**
 *
 * @author Rock
 */
public class Connection {

    protected final static MongoClient mongo;
    protected final static DB db;
    protected static MongoDatabase dbV3;

    private final static Map<CollectionsDB, DBCollection> collections;

    public static DB getConnection() {
        return db;
    }

    public static MongoDatabase getDBV3() {
        return dbV3;
    }

    public static Map<CollectionsDB, DBCollection> getCollection() {
        return collections;
    }

    static {
        List<MongoCredential> credentials = new ArrayList<>();
        credentials.add(MongoCredential.createCredential("root", "java_sensei",
                "root2560".toCharArray()));
        mongo = new MongoClient(new ServerAddress(), credentials);
        mongo.setWriteConcern(WriteConcern.SAFE);
        db = mongo.getDB("java_sensei");
        dbV3 = mongo.getDatabase("java_sensei");

        collections = new HashMap<>();
        collections.put(CollectionsDB.ALUMNOS, db.getCollection("alumnos"));
        collections.put(CollectionsDB.EJERCICIOS,
                db.getCollection("ejercicios"));
        collections.put(CollectionsDB.LECCIONES,
                db.getCollection("lecciones"));
        collections.put(CollectionsDB.RANKING_EJERCICIOS,
                db.getCollection("ranking_ejercicios"));
        collections.put(CollectionsDB.RANKING_RECURSOS,
                db.getCollection("ranking_recursos"));
        collections.put(CollectionsDB.RECURSOS,
                db.getCollection("recursos"));
        collections.put(CollectionsDB.TEMAS,
                db.getCollection("temas"));
        collections.put(CollectionsDB.BITACORA_EJERCICIOS,
                db.getCollection("bitacora_ejercicios"));
        collections.put(CollectionsDB.BITACORA_FOTOGRAFIAS,
                db.getCollection("bitacora_fotografias"));
    }
}
