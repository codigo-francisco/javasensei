/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.dbo.bitacora;

import com.mongodb.DBObject;
import java.time.ZonedDateTime;
import javasensei.db.DBInterface;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Base64;
/**
 *
 * @author PosgradoMCC
 */
public class BitacoraEjerciciosDBO implements DBInterface {

    private static final Base64.Decoder decoder = Base64.getDecoder();
            
    private int idAlumno;
    private String emocion;
    private int errores;
    private int aciertos;
    private double promedioErrores;
    private double promedioAciertos;
    private int ejercicioId;
    private int pasoId;
    private int totalErrores, totalAciertos;
    private Date fecha;
    private String tipoPaso;
    private int segundos;
    private List<byte[]> fotografias = new ArrayList<>();
    private long sesionId;
    
    public void setSesionId(long sesionId){
        this.sesionId = sesionId;
    }
    
    public BitacoraEjerciciosDBO(JsonObject object, long sesionId){
        idAlumno = object.get("idAlumno").getAsInt();
        emocion = object.get("emocion").getAsString();
        aciertos = object.get("errores").getAsInt();
        errores = object.get("aciertos").getAsInt();
        promedioErrores = object.get("promedioErrores").getAsDouble();
        promedioAciertos = object.get("promedioAciertos").getAsDouble();
        ejercicioId = object.get("ejercicioId").getAsInt();
        pasoId = object.get("pasoId").getAsInt();
        totalErrores = object.get("totalErrores").getAsInt();
        totalAciertos = object.get("totalAciertos").getAsInt();
        fecha = Date.from(ZonedDateTime.parse(object.get("fecha").getAsString()).toInstant());
        tipoPaso = object.get("tipoPaso").getAsString();
        segundos = object.get("segundos").getAsInt();
        if (object.get("fotografias")!=null){ //Las fotografias se guardan completamente en el servidor
            for (JsonElement element : object.get("fotografias").getAsJsonArray()){
                fotografias.add(decoder.decode(element.getAsString()));
            }
        }
        //fotografias = new Gson().fromJson(object.get("fotografias").getAsJsonArray(), ArrayList.class);
        this.sesionId = sesionId;
    }
    
    public static DBObject createDbObject(JsonObject object, long sesionId){
        return new BitacoraEjerciciosDBO(object, sesionId).convertToDBObject();
    }
    
    @Override
    public DBObject convertToDBObject() {
        DBObject object = new BasicDBObject();
        
        object.put("idAlumno", idAlumno);
        object.put("emocion", emocion);
        object.put("aciertos", aciertos);
        object.put("errores", errores);
        object.put("promedioErrores", promedioErrores);
        object.put("promedioAciertos", promedioAciertos);
        object.put("ejercicioId",ejercicioId);
        object.put("pasoId",pasoId);
        object.put("totalErrores",totalErrores);
        object.put("totalAciertos",totalAciertos);
        object.put("fecha",fecha);
        object.put("fotografias", fotografias);
        object.put("tipoPaso", tipoPaso);
        object.put("segundos",segundos);
        object.put("sesionId",sesionId);
        
        return object;
    }
    
}
