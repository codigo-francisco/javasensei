
package javasensei.ia.recognitionemotion;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.json.JSONObject;

/**
 *
 * @author Rock
 * 
 */
public class ExtractEmotionMicrosoft implements INeuralNetwork<BufferedImage> {

    @Override
    public Emocion processData(BufferedImage datos) {
        Emocion emocion = Emocion.NEUTRAL;
        try {
            System.out.println("Detector emocional oxford");
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(datos, "jpg", baos );
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                JSONObject scores = Unirest.post("https://api.projectoxford.ai/emotion/v1.0/recognize")
                    .header("Content-Type", "application/octet-stream")
                    .header("Ocp-Apim-Subscription-Key", "cf9480a09e0943008021a0c1c46e1697")
                    .body(
                            imageInByte
                    ).asJson()
                    .getBody()
                    .getArray()
                    .getJSONObject(0)
                    .getJSONObject("scores");
                
                double valorMayor = Double.MIN_VALUE;
                String campo = "neutral";
                
                
                for(Object object : scores.keySet()){
                    double valorActual = scores.getDouble(object.toString());
                    if (valorActual>valorMayor){
                        valorMayor = valorActual;
                        campo = object.toString();
                    }
                }
                
                emocion = parse(campo);
            }
            /*[{"faceRectangle":{"top":150,"left":234,"width":171,"height":171},
            "scores":{"contempt":1.80945121E-4,"surprise":4.52623E-5,"happiness":7.01911631E-4,"neutral":0.0184313282,"sadness":9.762928E-6,"disgust":0.15525125,"anger":0.8253754,"fear":4.179582E-6}}]*/
        } catch (IOException | UnirestException ex) {
            Logger.getLogger(ExtractEmotionMicrosoft.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emocion;
    } //secondary key: 9226e746fa964cafb677799b9ee784d1
    
    public Emocion parse(String campo){
        Emocion resultado = Emocion.NEUTRAL;
        switch(campo){
            case "contempt":
            case "disgust":
            case "anger":
                resultado = Emocion.ENOJADO;
                break;
            case "surprise":
            case "fear":
                resultado = Emocion.SORPRESA;
                break;
            case "happiness":
                resultado = Emocion.FELIZ;
                break;
            case "neutral":
                resultado = Emocion.NEUTRAL;
                break;
            case "sadness":
                resultado = Emocion.TRISTE;
                break;
        }
        
        return resultado;
    }
}