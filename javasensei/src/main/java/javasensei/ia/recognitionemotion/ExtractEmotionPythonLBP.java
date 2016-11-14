/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.ia.recognitionemotion;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.razorvine.pickle.PickleException;
import net.razorvine.pyro.PyroException;
import net.razorvine.pyro.PyroProxy;

/**
 *
 * @author Francisco
 */
public class ExtractEmotionPythonLBP implements INeuralNetwork<BufferedImage> {

    @Override
    public Emocion processData(BufferedImage datos) {
        Emocion emocion = Emocion.ROSTRO_NO_RECONOCIDO;

        try {
            return traducir(sendRequestPython(datos));
        } catch (Exception ex) {
            Logger.getLogger(ExtractEmotionPythonLBP.class.getName()).log(Level.SEVERE, null, ex);
        }

        return emocion;
    }

    public String processDataString(BufferedImage datos) {
        Emocion emocion = Emocion.ROSTRO_NO_RECONOCIDO;
        try {
            return traducir(sendRequestPython(datos)).toString().toLowerCase();
        } catch (PickleException | PyroException | IOException ex) {
            Logger.getLogger(ExtractEmotionPythonLBP.class.getName()).log(Level.SEVERE, null, ex);
        }

        return emocion.toString();
    }

    private Emocion traducir(String prediction) {
        switch (prediction) {
            case "Engagement":
                prediction = "Enganchado";
                break;
            case "Frustration":
                prediction = "Frustrado";
                break;
            case "Boredom":
                prediction = "Aburrido";
                break;
            case "Excitement":
                prediction = "Emocionado";
                break;
            default:
                prediction = "Rostro_No_Encontrado";
        }

        return Emocion.getEmocion(prediction);
    }

    private String sendRequestPython(BufferedImage datos) throws PickleException, PyroException, IOException {
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File fotografia = File.createTempFile(Long.toString(System.nanoTime()), ".png");
        ImageIO.write(datos, "png", fotografia);
        PyroProxy remoteObject = new PyroProxy("localhost", 25312, "emotion_service");
        //Enviamos ruta del archivo
        Object[] results = (Object[]) remoteObject.call("emotion_face_prediction_java", fotografia.getAbsolutePath());
        remoteObject.close();
        boolean result = (boolean) results[0];
        String prediction = results[1].toString();
        return prediction;
    }
}
