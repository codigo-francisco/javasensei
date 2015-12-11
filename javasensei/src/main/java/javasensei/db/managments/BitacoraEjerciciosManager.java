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
import com.mongodb.Block;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
        
        if(!idAlumno.isEmpty()){
            query.append("idAlumno", Integer.parseInt(idAlumno));
        }
            
        if(!ejercicioId.isEmpty()){
            query.append("ejercicioId", Integer.parseInt(ejercicioId));
        }
        
        if(!sesionId.isEmpty()){
            query.append("sesionId", Integer.parseInt(sesionId));
        }
        //Variables para las fechas
        Date dateI;
        Date dateF;
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
        
        //Lista donde se guardaran las bitacoras a enviar
        List<DBObject> dbo = bitacoras.find().into(new ArrayList<>()); 
        Map<Integer,List<DBObject>> map = new HashMap<>();
        
        
        if(!emocionInicial.isEmpty() && !emocionFinal.isEmpty()){
            FindIterable<DBObject> iterable = bitacoras.find(query).projection(new Document("fotografias",0).append("_id" , 0))
                    .sort(new BasicDBObject("fecha" , 1));
            
            
            //Recorro las bitacoras
            iterable.forEach(new Block<DBObject>() {
                @Override
                public void apply(final DBObject document) {
                    Integer sesionId = Integer.parseInt(document.get("sesionId")+"");
                    
                    List<DBObject> list = new ArrayList<>();
                    
                    if(!map.containsKey(sesionId)){
                        list.add(document);
                        map.put(sesionId, list);
                        
                    }else{
                        map.get(sesionId).add(document);
                    }
                }
            });
            
            List<DBObject> lista = new ArrayList<>();
            Iterator it = map.keySet().iterator();
            
            while(it.hasNext()){
                List<DBObject> l = map.get(it.next());
                String emocionIni = l.get(0).get("emocion").toString().toUpperCase();
                String emocionFin = l.get(l.size()-1).get("emocion").toString().toUpperCase();
                
                if(emocionIni.equals(emocionInicial.toUpperCase()) && emocionFin.equals(emocionFinal.toUpperCase())){
                   lista.addAll(l);  
                }
            }
            dbo = lista;
            
        //Si solo se busca las bitacoras con la emoción inicial
        }else if(!emocionInicial.isEmpty()){
            FindIterable<DBObject> iterable = bitacoras.find(query).projection(new Document("fotografias",0).append("_id" , 0))
                    .sort(new BasicDBObject("fecha" , 1));
            
            
            //Recorro las bitacoras
            iterable.forEach(new Block<DBObject>() {
                @Override
                public void apply(final DBObject document) {
                    Integer sesionId = Integer.parseInt(document.get("sesionId")+"");
                    
                    List<DBObject> list = new ArrayList<>();
                    
                    if(!map.containsKey(sesionId)){
                        list.add(document);
                        map.put(sesionId, list);
                        
                    }else{
                        map.get(sesionId).add(document);
                    }
                }
            });
            
            List<DBObject> lista = new ArrayList<>();
            Iterator it = map.keySet().iterator();
            
            while(it.hasNext()){
                List<DBObject> l = map.get(it.next());
                String emocionIni = l.get(0).get("emocion").toString().toUpperCase();
                
                if(emocionIni.equals(emocionInicial.toUpperCase())){
                   lista.addAll(l);  
                }
            }
            dbo = lista;
            
        //Si solo se busca las bitacoras con la emocion final    
        }else if(!emocionFinal.isEmpty()){
            FindIterable<DBObject> iterable = bitacoras.find(query).projection(new Document("fotografias",0).append("_id" , 0))
                    .sort(new BasicDBObject("fecha" , 1));
            
            
            //Recorro las bitacoras
            iterable.forEach(new Block<DBObject>() {
                @Override
                public void apply(final DBObject document) {
                    Integer sesionId = Integer.parseInt(document.get("sesionId")+"");
                    
                    List<DBObject> list = new ArrayList<>();
                    
                    if(!map.containsKey(sesionId)){
                        list.add(document);
                        map.put(sesionId, list);
                        
                    }else{
                        map.get(sesionId).add(document);
                    }
                }
            });
            
            List<DBObject> lista = new ArrayList<>();
            Iterator it = map.keySet().iterator();
            
            while(it.hasNext()){
                List<DBObject> l = map.get(it.next());
                String emocionFin = l.get(l.size()-1).get("emocion").toString().toUpperCase();
                if(emocionFin.equals(emocionFinal.toUpperCase())){
                   lista.addAll(l);  
                }
            }
            dbo = lista;
           
        }else{
            dbo = bitacoras.find(query).projection(new Document("fotografias",0).append("_id" , 0))
                .sort(new BasicDBObject("fecha" , 1))
                .into(new ArrayList<>());  
        }
               
        return dbo.toString();
    }
}
