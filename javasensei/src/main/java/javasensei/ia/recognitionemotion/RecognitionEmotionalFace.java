package javasensei.ia.recognitionemotion;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.util.ImageHelper;

/**
 *
 * @author Rock
 */
public class RecognitionEmotionalFace {

    private Gson gson = new Gson();
    private JsonArray fotos;
    private String detector;

    public RecognitionEmotionalFace(String fotosJson, String detector) {
        JsonElement element = new JsonParser().parse(fotosJson);
        fotos = element.getAsJsonArray();
        this.detector = detector;
    }

    public Emocion getEmocion() {
        Map<Emocion, Integer> emociones = new HashMap<>();

        for (int index = 0; index < fotos.size(); index++) {
            try {
                Emocion emocion = Emocion.NEUTRAL;
                String datos = fotos.get(index).getAsString();
                BufferedImage image = ImageHelper.decodeToImage(datos);
                //javax.imageio.ImageIO.write(image, "jpg", java.io.File.createTempFile("img", ".jpg", new java.io.File("G:/imagenes")));
                
                if (image != null) {
                    System.out.println("Se decodifico de base64 a imagebuffer");
                    switch (detector) {
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
                            }   break;
                    }
                }

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
