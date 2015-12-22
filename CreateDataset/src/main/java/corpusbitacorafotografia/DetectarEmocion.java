package corpusbitacorafotografia;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Rock
 */
public class DetectarEmocion {

    public static void main(String[] args) {
        try {
            File carpeta = new File("E:\\Programacion\\Trabajo BD\\fotografias_con_rostro");

            for (File imagen : carpeta.listFiles()) {

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ImageIO.write(ImageIO.read(imagen), "jpg", baos);
                    baos.flush();
                    byte[] imageInByte = baos.toByteArray();
                    String campo = "nodetectado";

                    try {
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

                        for (Object object : scores.keySet()) {
                            double valorActual = scores.getDouble(object.toString());
                            if (valorActual > valorMayor) {
                                valorMayor = valorActual;
                                campo = object.toString();
                            }
                        }                        
                    } catch (UnirestException | JSONException ex) {
                        Logger.getLogger(DetectarEmocion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    campo = parse(campo);
                    if (!campo.equals("nodetectado")) {
                        //Creamos la imagen en una carpeta de emociones
                        File imagenDestino = new File("E:\\Programacion\\Trabajo BD\\fotografias_emocion\\"+campo+"\\" + imagen.getName());
                        if (!imagenDestino.exists()){
                            try {
                                Files.copy(imagen.toPath(), imagenDestino.toPath());
                            } catch (Exception ex) {
                                System.out.println("Error en el guardado de la imagen");
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error en la lectura de la imagen");
                }

                Thread.sleep(3010);
                /*[{"faceRectangle":{"top":150,"left":234,"width":171,"height":171},
                 "scores":{"contempt":1.80945121E-4,"surprise":4.52623E-5,"happiness":7.01911631E-4,"neutral":0.0184313282,"sadness":9.762928E-6,"disgust":0.15525125,"anger":0.8253754,"fear":4.179582E-6}}]*/
            }
        }catch(Exception ex){
            System.out.println("Error de procesamiento en la imagen");
        }
    }

    public static String parse(String campo) {
        String resultado = "nodetectado";
        switch (campo) {
            case "contempt":
            case "disgust":
            case "anger":
                resultado = "enojado";
                break;
            case "surprise":
            case "fear":
                resultado = "sorpresa";
                break;
            case "happiness":
                resultado = "feliz";
                break;
            case "neutral":
                resultado = "neutral";
                break;
            case "sadness":
                resultado = "triste";
                break;
        }

        return resultado;
    }
}
