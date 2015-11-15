package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.Arrays;
import java.util.Date;
import javasensei.db.Connection;

/**
 *
 * @author Rock
 */
public class ChatManager {
    private DBCollection chatCollection = Connection.getCollection().get(CollectionsDB.CHAT);
    
    public Boolean agregarMensaje(String message, String nombreUsuario, String idUsuario, Long fecha){
        BasicDBObject mensaje = new BasicDBObject("message", message);
        mensaje.put("nombreUsuario", nombreUsuario);
        mensaje.put("idUsuario", idUsuario);
        mensaje.put("fecha", fecha);

        return chatCollection.insert(mensaje)!=null;
    }
    
    public String leerMensajes(Long fecha){
        DBObject query = QueryBuilder.start("fecha").is(new BasicDBObject("$gt", fecha)).get();
        
        return chatCollection.find(query).toArray().toString();
    }
}
