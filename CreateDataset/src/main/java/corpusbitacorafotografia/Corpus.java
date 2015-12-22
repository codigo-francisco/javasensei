package corpusbitacorafotografia;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.bson.Document;

/**
 *
 * @author Rock
 */
public class Corpus {
    static int cantidad=0;
    
    public static void main(String[] args){
        //Extracción de las fotografias en el disco duro
        MongoClient connection = new MongoClient();
        MongoDatabase database = connection.getDatabase("java_sensei_produccion");
        //Collection bitacora_fotografia
        MongoCollection bitacoraFotografias = database.getCollection("bitacora_fotografias");
        
        //Se extraen todas las fotografias
        FindIterable iterable = bitacoraFotografias.find();
        
        iterable.forEach(new Block<Document>(){
            @Override
            public void apply(Document t) {
                try {
                    cantidad++;
                    String nombreArchivo =
                            String.format("%s_%s_%s_%s_%s_%s.jpg",
                                    t.getObjectId("_id").toString(),
                                    t.get("usuario"),
                                    t.get("ejercicioId"),
                                    t.get("pasoId"),
                                    t.get("tipoPaso"),
                                    t.getString("fecha").substring(0, 10)
                            );
                    
                    File fotografiaObj = new File("E:\\Programacion\\Trabajo BD\\fotografias\\"+nombreArchivo);
                    
                    if (!fotografiaObj.exists()){
                        
                        String fotografiaBase64 = t.getString("fotografia");
                        byte[] buffer = Base64.getDecoder().decode(fotografiaBase64);
                        try (ByteArrayInputStream bis = new ByteArrayInputStream(buffer)) {
                            BufferedImage buffered = ImageIO.read(bis);
                            ImageIO.write(buffered, "jpg", fotografiaObj );
                            buffered = null;
                        }
                    }
                    
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            
        });
        
        System.out.println(cantidad);
    }
}
