package corpusbitacorafotografia;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import javax.imageio.ImageIO;

/**
 *
 * @author Rock
 */
public class DetectarRostro {

    public static void main(String[] args) {

        //8b6729c870e04928b87d9ef29bd86035
        File carpeta = new File("E:\\Programacion\\Trabajo BD\\fotografias");

        for (File imagen : carpeta.listFiles()) {

            File imagenDestino = new File("E:\\Programacion\\Trabajo BD\\fotografias_con_rostro\\" + imagen.getName());

            if (!imagenDestino.exists()) {

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ImageIO.write(ImageIO.read(imagen), "jpg", baos);
                    baos.flush();
                    byte[] imageInByte = baos.toByteArray();
                    JsonNode array = Unirest.post("https://api.projectoxford.ai/face/v1.0/detect")
                            .header("Content-Type", "application/octet-stream")
                            .header("Ocp-Apim-Subscription-Key", "8b6729c870e04928b87d9ef29bd86035")
                            .body(
                                    imageInByte
                            ).asJson()
                            .getBody();

                    if (array.isArray()) {
                        if (array.getArray().getJSONObject(0).has("faceId")) {
                            Files.copy(imagen.toPath(), imagenDestino.toPath());
                        }
                    }
                    
                    Thread.sleep(3010);

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}
