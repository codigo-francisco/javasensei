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
import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author PosgradoMCC
 */
public class BitacoraEjerciciosDBO implements DBInterface {

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
    
    public BitacoraEjerciciosDBO(JsonObject object){
        idAlumno = object.get("idAlumno").getAsInt();
        emocion = object.get("emocion").getAsString();
        aciertos = object.get("errores").getAsInt();
        errores = object.get("aciertos").getAsInt();
        promedioErrores = object.get("promedioErrores").getAsInt();
        promedioAciertos = object.get("promedioAciertos").getAsInt();
        ejercicioId = object.get("ejercicioId").getAsInt();
        pasoId = object.get("pasoId").getAsInt();
        totalErrores = object.get("totalErrores").getAsInt();
        totalAciertos = object.get("totalAciertos").getAsInt();
        fecha = Date.from(ZonedDateTime.parse(object.get("tiempo").getAsString()).toInstant());
    }
    
    @Override
    public DBObject convertToDBObject() {
        DBObject object = new BasicDBObject();
        
        object.put("idAlumno", idAlumno);
        
        return object;
    }
    
}
