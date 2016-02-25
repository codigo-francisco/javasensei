package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import javasensei.db.Connection;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Rock
 */
public class BitacoraFotografia {
    
   private DBCollection bitacoraFotografias = Connection.getCollection()
           .get(CollectionsDB.BITACORA_FOTOGRAFIAS);
    
    public void guardarBitacoraFotografia(String datos){
        bitacoraFotografias.save(BasicDBObject.parse(datos));
    }
    
    public void reiniciarSesion(Long idUsuario){
        synchronized(bitacoraFotografias){
            DBObject query = new BasicDBObject("usuario", idUsuario);
            query.put("sesionactual", true);
            DBObject update = new BasicDBObject();
            update.put("$set", new BasicDBObject("sesionactual",false));

            bitacoraFotografias.update(query, update, false, true);
        }
    }
    
    public void categorizarFotografia(ObjectId id, String emocionTutor, String tutor){        
        DBObject set = new BasicDBObject();
        set.put("emocionTutor", emocionTutor);
        set.put("tutor",tutor);
        
        bitacoraFotografias.update(new BasicDBObject("_id", id), 
                new BasicDBObject("$set",set)
        );
    }
    
    public String obtenerFotografiasCategorizadas(Long idUsuario){
        DBObject query = new BasicDBObject();
        query.put("usuario", idUsuario);
        query.put("sesionactual", true);
        query.put("tutor", new BasicDBObject("$ne",""));
        
        return bitacoraFotografias.find(query, new BasicDBObject("fotografia",1)).toArray().toString();
    }
    
    private final static int limiteDocumentos = 3;
    
    public List<DBObject> obtenerFotografiasSinProcesar(Long idUsuario){
        DBObject query = new BasicDBObject("usuario", idUsuario);
        query.put("sesionactual", true);
        return bitacoraFotografias.find(query).limit(limiteDocumentos).
                sort(new BasicDBObject("_id",-1)).toArray();
    }
}
