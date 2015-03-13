package javasensei.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Francisco Gonzalez Hernandez, Ramon Zatarain Cabada, Lucia Barron
 * Estrada, Raul Oramas Bustillos Email Contacto:
 * franciscogonzalez_hernandez@hotmail.com
 */
public class FileHelper {

    private FileHelper() {
    }

    private static FileHelper fileHelper = new FileHelper();

    public static FileHelper getInstance() {
        return fileHelper;
    }

    /**
     * Obtiene los archivos desde la ruta raiz como recursos
     *
     * @param nameFile Nombre del archivo
     * @return Cadena del archivo
     */
    public String getFile(String nameFile) {
        return getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replace("WEB-INF/classes/javasensei/util/FileHelper.class", nameFile).replace("%20", " ");
    }

    public String getContentFile(String nameFile) {
        String pathFile = getFile(nameFile);
        StringBuilder result=new StringBuilder();
        try{
            for (String line : Files.readAllLines(new File(pathFile).toPath()))
                result.append(line);
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result.toString();
    }
}
