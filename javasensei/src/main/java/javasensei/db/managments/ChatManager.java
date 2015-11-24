package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import javasensei.db.Connection;

/**
 *
 * @author Rock
 */
public class ChatManager {
    private DBCollection chatCollection = Connection.getCollection().get(CollectionsDB.CHAT);
    
    public Long agregarMensaje(String message, String nombreUsuario, String idUsuario, Integer idEjercicio, String color){
        Long fecha = Instant.now().toEpochMilli();
        
        BasicDBObject mensaje = new BasicDBObject("message", message);
        mensaje.put("nombreUsuario", nombreUsuario);
        mensaje.put("idUsuario", idUsuario);
        mensaje.put("idEjercicio",idEjercicio);
        mensaje.put("fecha", fecha);
        mensaje.put("color", color);
        chatCollection.insert(mensaje);
        
        return fecha;
    }
    
    public String leerMensajes(Long fecha, Integer idEjercicio){
        DBObject query = QueryBuilder.start("fecha")
                .is(new BasicDBObject("$gt", fecha))
                .put("idEjercicio")
                .is(idEjercicio)
                .get();
        
        return chatCollection.find(query).toArray().toString();
    }
}
