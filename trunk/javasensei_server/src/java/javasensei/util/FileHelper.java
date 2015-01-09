package javasensei.util;
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
}
