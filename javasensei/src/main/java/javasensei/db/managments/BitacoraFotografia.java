package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import javasensei.db.Connection;
import org.bson.Document;

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
            DBObject query = new BasicDBObject("idUsuario", idUsuario);
            query.put("sesionactual", true);
            DBObject update = new BasicDBObject();
            update.put("$set", new BasicDBObject("sesionactual",false));

            bitacoraFotografias.updateMulti(query, update);
        }
    }
    
    private static int limiteDocumentos = 3;
    
    public List<DBObject> obtenerFotografiasSinProcesar(Long idUsuario){
        DBObject query = new BasicDBObject("idUsuario", idUsuario);
        query.put("sesionactual", true);
        return bitacoraFotografias.find(query).limit(limiteDocumentos).
                sort(new BasicDBObject("_id",-1)).toArray();
    }
}
