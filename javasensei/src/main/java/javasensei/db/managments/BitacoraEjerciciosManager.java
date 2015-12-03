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
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

import java.util.ArrayList;
import java.util.List;
import javasensei.db.Connection;
import javasensei.dbo.bitacora.BitacoraEjerciciosDBO;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
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
                                  String emocionInicial, String emocionFinal) {
        
        
        MongoCollection<DBObject> bitacoras = Connection.getDBV3().getCollection("bitacora_ejercicios", DBObject.class);
        Document query = new Document();
        
        Date dateI;
        Date dateF;
        
        
        if(!idAlumno.isEmpty()){
            query.append("idAlumno", Integer.parseInt(idAlumno));
        }
            
        if(!ejercicioId.isEmpty()){
            query.append("ejercicioId", Integer.parseInt(ejercicioId));
        }
        
        if(!sesionId.isEmpty()){
            query.append("sesionId", Integer.parseInt(sesionId));
        }    
        
        if(!fechaFinal.isEmpty() && !fechaInicial.isEmpty()){
            dateI = Date.from(LocalDateTime.parse(fechaInicial).toInstant(ZoneOffset.UTC));
            dateF = Date.from(LocalDateTime.parse(fechaFinal).toInstant(ZoneOffset.UTC));
            query.append("fecha", new Document("$gte", dateI).append("$lte", dateF));
            
        }else if(!fechaInicial.isEmpty()){
            dateI = Date.from(LocalDateTime.parse(fechaInicial).toInstant(ZoneOffset.UTC));
            query.append("fecha", new Document("$gte", dateI));
            
        }else if(!fechaFinal.isEmpty()){
            dateF = Date.from(LocalDateTime.parse(fechaFinal).toInstant(ZoneOffset.UTC));
            query.append("fecha", new Document("$lte", dateF));
        }    
        
        System.out.println(query.toJson(new JsonWriterSettings(JsonMode.SHELL))); 
        
        List<DBObject> dbo = bitacoras.find(query).projection(new Document("fotografias",0).append("_id" , 0)).into(new ArrayList<DBObject>());
        
        return dbo.toString();
    }
}
