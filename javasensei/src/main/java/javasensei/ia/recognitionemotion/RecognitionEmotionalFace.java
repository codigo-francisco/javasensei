package javasensei.ia.recognitionemotion;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.DBObject;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.managments.BitacoraFotografia;
import javasensei.util.ImageHelper;
import org.bson.types.ObjectId;

/**
 *
 * @author Rock
 */
public class RecognitionEmotionalFace {

    private Gson gson = new Gson();
    private String detector;
    private Long idUsuario;

    public RecognitionEmotionalFace(String detector, long idUsuario) {
        this.detector = detector;
        this.idUsuario = idUsuario;
    }

    public Emocion getEmocion() {
        Map<Emocion, Integer> emociones = new HashMap<>();
        //Las fotos son una lista obtenida de mongo, son las ultimas fotografias del usuario sin procesar
        BitacoraFotografia bitacoraFotografia = new BitacoraFotografia();
        List<DBObject> fotos = new BitacoraFotografia().obtenerFotografiasSinProcesar(idUsuario);
        
        for (int index = 0; index < fotos.size(); index++) {
            try {
                Emocion emocion = Emocion.NEUTRAL;
                boolean emocionEncontrada = true;
                DBObject foto = fotos.get(index);
                
                String datos = foto.get("fotografia").toString();
                BufferedImage image = ImageHelper.decodeToImage(datos);
                
                //javax.imageio.ImageIO.write(image, "jpg", java.io.File.createTempFile("img", ".jpg", new java.io.File("D:/imagenes")));
                
                if (image != null) {
                    System.out.println("Se decodifico de base64 a imagebuffer");
                    switch (detector) {
                        case "indico":
                            System.out.println("Se llama detecto indico");
                            emocion = new ExtractEmotionIndico().processData(image);
                            System.out.println("Rostro procesado, indico: "+emocion);
                            break;
                        case "oxford":
                            System.out.println("Se llama detector de oxford");
                            emocion = new ExtractEmotionMicrosoft().processData(image);
                            System.out.println("Rostro procesado, oxford");
                            break;
                        case "neuroph":
                        default:
                            RecognitionResult result = new RecognitionFace().processFace(image);
                            if (result.isHayEmocion()) { //Si hay una emocion se ejecuta la red neuronal, en caso contrario se desecha
                                System.out.println("Se encontro un rostro, opencv");
                                emocion = new ExtractEmotionNeuroph().processData(result.getCoordenadas());
                                System.out.println("Emocion procesada: NeuroPH");
                            }else{
                                emocionEncontrada = false;
                            }
                            break;
                    }
                }
                
                if (emocionEncontrada)
                    bitacoraFotografia.categorizarFotografia( new ObjectId(foto.get("_id").toString()), emocion.toString(), detector );
                
                if (emociones.containsKey(emocion)) {
                    emociones.put(emocion, emociones.get(emocion) + 1);
                } else {
                    emociones.put(emocion, 1);
                }

            } catch (Exception ex) {
                Logger.getLogger(RecognitionEmotionalFace.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return findEmotion(emociones);
    }

    public String getEmocionString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("emocion", getEmocion().toString());

        return gson.toJson(jsonObject);
    }

    protected Emocion findEmotion(Map<Emocion, Integer> emociones) {

        Emocion emocion = Emocion.NEUTRAL;

        if (emociones.size() > 0) {

            Entry<Emocion, Integer> entry = null;

            for (Entry<Emocion, Integer> item : emociones.entrySet()) {

                if (entry == null) {
                    entry = item;
                } else if (item.getValue() > entry.getValue()) {
                    entry = item;
                }

            }

            emocion = entry.getKey();
        }

        return emocion;
    }
}
