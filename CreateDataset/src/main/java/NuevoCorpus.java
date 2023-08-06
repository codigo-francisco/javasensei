
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Rock
 */
public class NuevoCorpus {

    static PrintStream print;

    static {
        try {
            print = new PrintStream("E:\\javasensei\\dataset.csv");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NuevoCorpus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        String carpetaPadre = "E:\\Respaldo\\proyecto RVERK\\RafD_Ordenado\\";
        String[] carpetas = {
            "enojado",
            "feliz",
            "sorpresa",
            "triste",
            "neutral"
        };
        String[] emociones ={
            ",1,0,0,0,0",
            ",0,1,0,0,0",
            ",0,0,1,0,0",
            ",0,0,0,1,0",
            ",0,0,0,0,1"
        };

        for (int index=0; index<carpetas.length; index++) {
            String carpeta = carpetas[index];
            String carpetaEmocion = carpetaPadre + carpeta;
            File directory = new File(carpetaEmocion);

            for (File imagen : directory.listFiles((File dir, String name) -> name.substring(name.lastIndexOf(".")).equals(".jpg"))) {
                BufferedImage image = ImageIO.read(imagen);
                RecognitionFace recognition = new RecognitionFace();
                RecognitionResult result = recognition.processFace(image);
                image = null;
                System.gc();
                if (result.isHayEmocion()) {
                    String formatString = Arrays.toString(result.getCoordenadas()).replace("[", "").replace("]", "").replace(" ", "");
                    print.println(formatString.concat(emociones[index]));
                }
            }
        }
    }
    
}
