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
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import javasensei.db.Connection;
import javasensei.dbo.bitacora.BitacoraEjerciciosDBO;

/**
 *
 * @author PosgradoMCC
 */
public class BitacoraEjerciciosManager {
    private DBCollection bitacoraEjercicios = Connection.getCollection().get(CollectionsDB.BITACORA_EJERCICIOS);
    
    public void guardarBitacora(String logBitacora){
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(logBitacora).getAsJsonArray();
        List<DBObject> bitacoras = new ArrayList<>();
        
        for(JsonElement object : array){
            bitacoras.add(
                    new BitacoraEjerciciosDBO(
                            object.getAsJsonObject())
                            .convertToDBObject()
            );
        }
    }
}
