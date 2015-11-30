package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import javasensei.db.Connection;

/**
 *
 * @author Rock
 */
public class BitacoraFotografia {
    
   private DBCollection bitacoraFotografias = Connection.getCollection().get(CollectionsDB.BITACORA_FOTOGRAFIAS);
    
    public void guardarBitacoraFotografia(String datos){
        bitacoraFotografias.save(BasicDBObject.parse(datos));
    }
}
