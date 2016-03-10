package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Collections;
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
   
   private DBCollection bitacoraFotografiasUsuario = Connection.getCollection()
           .get(CollectionsDB.BITACORA_FOTOGRAFIAS_USUARIO);
    
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
    
    public void categorizarFotografia(
            ObjectId id, 
            String emocionTutor, 
            String tutor){        
        DBObject set = new BasicDBObject();
        set.put("emocionTutor", emocionTutor);
        set.put("tutor",tutor);
        
        bitacoraFotografias.update(new BasicDBObject("_id", id), 
                new BasicDBObject("$set",set)
        );
    }
    
     public void categorizarFotografiaUsuario(
             ObjectId id, 
             String usuario,
             String emocion){        
        DBObject object = new BasicDBObject();
        object.put("_id", id);
        object.put("usuario",usuario);
        object.put("emocion", emocion);
        
        bitacoraFotografiasUsuario.save(object);
    }
    
    public String obtenerFotografiasCategorizadas(Long idUsuario){
        DBObject query = new BasicDBObject();
        query.put("usuario", idUsuario);
        query.put("sesionactual", true);
        query.put("tutor", new BasicDBObject("$ne",""));
        
        List<DBObject> fotografias = bitacoraFotografias.find(query, new BasicDBObject("fotografia",1)).toArray();
        
        //Se extraen 5 fotografias en caso de que sea una coleccion más grande
        if (fotografias.size()>5){
            //Elegir aleatoriamente las fotografias
            Collections.shuffle(fotografias);
            fotografias = fotografias.subList(0, 4);
        }
        
        return fotografias.toString();
    }
    
    private final static int limiteDocumentos = 3;
    
    public List<DBObject> obtenerFotografiasSinProcesar(Long idUsuario){
        DBObject query = new BasicDBObject("usuario", idUsuario);
        query.put("sesionactual", true);
        return bitacoraFotografias.find(query).limit(limiteDocumentos).
                sort(new BasicDBObject("_id",-1)).toArray();
    }
}
