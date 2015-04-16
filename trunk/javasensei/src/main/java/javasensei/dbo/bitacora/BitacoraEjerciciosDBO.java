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
import com.google.gson.JsonObject;
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
    private ZonedDateTime fecha;
    
    public BitacoraEjerciciosDBO(String json){
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        idAlumno = object.get("idAlumno").getAsInt();
        emocion = object.get("emocion").getAsString();
        aciertos = object.get("errores").getAsInt();
        errores = object.get("aciertos").getAsInt();
        promedioErrores = object.get("promedioErrores").getAsInt();
        promedioAciertos = object.get("promedioAciertos").getAsInt();
        ejercicioId = object.get("ejercicioId").getAsInt();
        pasoId = object.get("pasoId").getAsInt();
        
        fecha = ZonedDateTime.now();
    }
    
    @Override
    public DBObject convertToDBObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
