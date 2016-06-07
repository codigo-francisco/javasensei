/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.ia.recognitionemotion;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.razorvine.pyro.PyroProxy;

/**
 *
 * @author Francisco
 */
public class ExtractEmotionPythonLBP implements INeuralNetwork<BufferedImage> {

    @Override
    public Emocion processData(BufferedImage datos) {
        Emocion emocion = Emocion.NEUTRAL;
        
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(datos, "jpg", baos);
            
            PyroProxy remoteObject = new PyroProxy("localhost", 25312, "emotion_service");
            
            Object[] results = (Object[]) remoteObject.call("emotion_face_prediction_java", baos.toByteArray());
            remoteObject.close();
            
            boolean result = (boolean) results[0];
            String prediction = results[1].toString();
            
            return Emocion.getEmocion(prediction);
        }catch(Exception ex){
            Logger.getLogger(ExtractEmotionPythonLBP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return emocion;
    }
    
}
