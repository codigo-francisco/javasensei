/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.db.managments;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;
import javasensei.db.Connection;
import javasensei.dbo.bitacora.BitacoraEjerciciosDBO;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
/**
 *
 * @author PosgradoMCC
 */
public class BitacoraEjerciciosManager {

    private final DBCollection bitacoraEjercicios = Connection.getCollection().get(CollectionsDB.BITACORA_EJERCICIOS);

    public String guardarBitacoras(String logBitacoras) {
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(logBitacoras).getAsJsonArray();
        List<DBObject> bitacoras = new ArrayList<>();

        synchronized (bitacoraEjercicios) {

            long sesionId = 1;

            //Sesion id
            DBCursor cursor = bitacoraEjercicios.find().sort(
                    QueryBuilder.start("sesionId")
                    .is(-1)
                    .get()).limit(1);
            
            if (cursor != null && cursor.hasNext()){
                sesionId = new Long(cursor.next().get("sesionId").toString()) + 1;
            }

            if (array.size()>0){
                for (JsonElement object : array) {
                    bitacoras.add(
                            BitacoraEjerciciosDBO.createDbObject(
                                    object.getAsJsonObject(),
                                    sesionId
                                )
                    );
                }

                bitacoraEjercicios.insert(bitacoras);
            }
            
            return bitacoras.toString();
        }
    }
    
    public String obtenerBitacora(String idAlumno, String ejercicioId, 
                                  String fechaInicial, String fechaFinal, String sesionId,
                                  String emocionInicial, String emocionFinal){
        
        //Query para hacer la consulta a la base de datos
        Bson query = new BsonDocument();
        MongoCollection<DBObject> bitacoras = Connection.getDBV3().getCollection("bitacora_ejercicios", DBObject.class);
        
        
        Date dateI=null;
        Date dateF=null;
        
        if(!idAlumno.isEmpty())
            
            eq("idAlumno",Integer.parseInt(idAlumno));
            //builder.put("idAlumno").is(Integer.parseInt(idAlumno));
        
        if(!ejercicioId.isEmpty())
            builder.put("ejercicioId").is(Integer.parseInt(ejercicioId));
        
        if(!fechaInicial.isEmpty()){
            dateI = Date.from(LocalDateTime.parse(fechaInicial).toInstant(ZoneOffset.UTC));
            builder.put("fecha").greaterThan(dateI);
        }    
        
        if(!fechaFinal.isEmpty()){
            dateF = Date.from(LocalDateTime.parse(fechaFinal).toInstant(ZoneOffset.UTC));
            builder.put("fecha").lessThan(dateF);
        }
        
        if(!sesionId.isEmpty())
            builder.put("sesionId").is(Integer.parseInt(sesionId));
            
        List<DBObject> dbo = bitacoras.find(
                and(
                    gt("fecha", dateI),
                    lt("fecha", dateF)
                )
        ).projection(
                and(
                        eq("_id",0),
                        eq("fotografias",0)
                )
        ).into(new ArrayList<DBObject>());
        //bitacoraEjercicios.find(builder.get(), new BasicDBObject("fotografias",0).append("_id", 0)).toArray();
        
        return dbo.toString();
    }
}
