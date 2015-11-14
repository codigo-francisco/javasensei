package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.Date;
import javasensei.db.Connection;

/**
 *
 * @author Rock
 */
public class ChatManager {
    private DBCollection chatCollection = Connection.getCollection().get(CollectionsDB.CHAT);
    
    public Boolean agregarMensaje(String message, String nombreUsuario, String idUsuario, Date fecha){
        BasicDBObject mensaje = new BasicDBObject("message", message);
        mensaje.put("nombreUsuario", nombreUsuario);
        mensaje.put("idUsuario", idUsuario);
        mensaje.put("fecha", fecha);
        
        return chatCollection.insert(mensaje).getN()>0;
    }
}
