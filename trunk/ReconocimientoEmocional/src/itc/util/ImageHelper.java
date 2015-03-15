package itc.util;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Rock
 */
public class ImageHelper {    
    public static BufferedImage transformFileImageAbsolutePath(String file){
        BufferedImage image=null;
        
        try{
            image = ImageIO.read(new File(file));
            
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        
        return image;
    }
    
    public static BufferedImage transformFileImage(String archivoImagen){
        return transformFileImageAbsolutePath(FileHelper.getFile(archivoImagen));
    }
}
