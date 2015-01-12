package javasensei.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Francisco Gonzalez Hernandez, Ramon Zatarain Cabada, Lucia Barron Estrada, Raul Oramas Bustillos
 * Email Contacto: franciscogonzalez_hernandez@hotmail.com
 */
public class FileHelper {
    
    private FileHelper(){ }
    
    private static FileHelper fileHelper = new FileHelper();
    
    public static FileHelper getInstance(){
        return fileHelper;
    }
    
    /**
     * Obtiene los archivos desde la ruta raiz como recursos
     * @param nameFile Nombre del archivo
     * @return Cadena del archivo
     */
    public String getFile(String nameFile){
        return getClass().getClassLoader().getResource(nameFile).getPath().substring(1).replace("%20", " ");
    }
    
    public String getContentFile(String nameFile){
        String pathFile = getFile(nameFile);
        String result="[]";
        try{
            result = Arrays.toString(Files.readAllLines(new File(pathFile).toPath()).toArray());
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
