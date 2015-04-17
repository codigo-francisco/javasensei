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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import javasensei.db.Connection;
import javasensei.dbo.bitacora.BitacoraEjerciciosDBO;

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
                sesionId = new Long(cursor.next().get("sesionId").toString());
            }

            for (JsonElement object : array) {
                bitacoras.add(
                        BitacoraEjerciciosDBO.createDbObject(
                                object.getAsJsonObject(),
                                sesionId
                            )
                );
            }
            
            bitacoraEjercicios.insert(bitacoras);
            
            return bitacoras.toString();
        }
    }
}
