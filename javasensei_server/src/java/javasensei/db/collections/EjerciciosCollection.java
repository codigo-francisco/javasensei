/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.db.collections;

import com.mongodb.DBCollection;
import javasensei.db.connection.Connection;

/**
 *
 * @author Rock
 */
public class EjerciciosCollection extends Connection{
    
    public DBCollection getEjerciciosCollection(){
        return db.getCollection("ejercicios");
    }
}
