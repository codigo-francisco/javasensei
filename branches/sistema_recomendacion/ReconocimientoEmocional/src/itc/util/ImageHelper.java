package itc.util;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Rock
 */
public class ImageHelper {
    public static BufferedImage transformFileImage(String archivoImagen){
        
        BufferedImage image=null;
        
        try{
            image = ImageIO.read(new File(FileHelper.getFile(archivoImagen)));
            
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        
        return image;
    }
}
