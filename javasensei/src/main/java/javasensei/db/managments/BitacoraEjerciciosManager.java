/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.db.managments;

import com.mongodb.DBCollection;
import javasensei.db.Connection;

/**
 *
 * @author PosgradoMCC
 */
public class BitacoraEjerciciosManager {
    private DBCollection bitacoraEjercicios = Connection.getCollection().get(CollectionsDB.BITACORA_EJERCICIOS);
    
    public void guardarBitacora(String logBitacora){
        
    }
}
