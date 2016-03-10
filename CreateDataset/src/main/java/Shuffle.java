
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Rock
 */
public class Shuffle {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        File file = new File("E:\\javasensei\\dataset.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> cadenas = new ArrayList<>();
        
        while(reader.ready()){
            cadenas.add(reader.readLine());
        }
        reader.close();
        
        Random random = new SecureRandom();
        
        for(int index=0; index < cadenas.size(); index++){
            int index2 = random.nextInt(cadenas.size());
            
            //Swap
            String aux = cadenas.get(index2);
            cadenas.set(index2, cadenas.get(index));
            cadenas.set(index, aux);
        }
        
        //Escribimos en un nuevo DataSet
        File fileShuffle = new File("E:\\javasensei\\datasetShuffle.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileShuffle));
        
        Iterator<String> iterador = cadenas.iterator();
        while(iterador.hasNext()){
            writer.append(iterador.next());
            writer.newLine();
        }
        
        writer.close();
    }
}
