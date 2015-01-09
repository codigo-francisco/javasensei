package itc.run;

/**
 * @author Francisco Gonzalez Hernandez, Ramon Zatarain Cabada, Lucia Barron Estrada, Raul Oramas Bustillos
 * Email Contacto: franciscogonzalez_hernandez@hotmail.com
 */

import itc.neuralnetwork.Emocion;
import itc.neuralnetwork.ExtractEmotion;
import itc.recognition.RecognitionFace;
import itc.util.ImageHelper;
import java.awt.image.BufferedImage;

public class Execution {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String fileImage = "com/files/rostro.jpg";
        BufferedImage image = ImageHelper.transformFileImage(fileImage);
        RecognitionFace recognition = new RecognitionFace(image);
        double[] coordenadas  = recognition.processFace();
        ExtractEmotion extract = new ExtractEmotion();
        Emocion emocion = extract.redNeuronal(coordenadas);
        
        System.out.println("\nEmocion Detectada: "+emocion);
        System.out.println();
    }
    
    public Emocion getEmocion(BufferedImage image) throws Exception{
        RecognitionFace recognition = new RecognitionFace(image);
        double[] coordenadas  = recognition.processFace();
        ExtractEmotion extract = new ExtractEmotion();
        Emocion emocion = extract.redNeuronal(coordenadas);
        
        return emocion;        
    }    
}
