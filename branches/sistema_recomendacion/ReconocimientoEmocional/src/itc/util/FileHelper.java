/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itc.util;
/**
 * @author Francisco Gonzalez Hernandez, Ramon Zatarain Cabada, Lucia Barron Estrada, Raul Oramas Bustillos
 * Email Contacto: franciscogonzalez_hernandez@hotmail.com
 */
public class FileHelper {
    /**
     * Obtiene los archivos desde la ruta raiz como recursos
     * @param nameFile Nombre del archivo
     * @return Cadena del archivo
     */
    public static String getFile(String nameFile){
        return ClassLoader.getSystemResource(nameFile).getPath().substring(1).replace("%20", " ");
    }
}
