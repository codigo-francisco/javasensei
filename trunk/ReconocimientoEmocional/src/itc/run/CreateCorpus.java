/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itc.run;

import itc.recognition.RecognitionFace;
import itc.util.ImageHelper;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;

/**
 *
 * @author Rock
 */
public class CreateCorpus {

    public static void main(String[] args) throws FileNotFoundException, Exception {
        String folder = "E:\\Respaldo\\proyecto RVERK\\RafD_Ordenado";
        PrintStream print = new PrintStream(new File("E:\\corpus.csv"));
        RecognitionFace recognition = new RecognitionFace();
        
        String[] folderEmociones = new String[]{"enojado", "feliz", "sorpresa", "triste", "neutral"};
        
        int posicionNominal = 0;

        for (String folderEmocion : folderEmociones) {
            //Obtenemos el listado de archivo jpg
            File carpeta = new File(folder + "\\" + folderEmocion);
            if (carpeta.exists() && carpeta.isDirectory()) {
                File[] imagenes = carpeta.listFiles((File pathname)
                        -> !pathname.isDirectory()
                        && pathname.getName().endsWith(".jpg")
                );
                
                Integer[] nominals = new Integer[folderEmociones.length];
                Arrays.fill(nominals, 0);
                
                nominals[posicionNominal] = 1;
                
                String nominalString = Arrays.toString(nominals);
                nominalString = nominalString.substring(1, nominalString.length()-1).replace(" ", "");
                
                System.out.printf("La emocion %s tiene el valor %s\n",folderEmocion, nominalString);
                
                for (File imagen : imagenes) {
                    //Se ejecuta el extractor de caracteristicas y se guarda en el csv final
                    BufferedImage image = ImageHelper.transformFileImageAbsolutePath(imagen.getAbsolutePath());
                    double[] coordenadas = recognition.processFace(image);
                    for (double coordenada : coordenadas){
                        print.print(coordenada+",");
                    }
                    print.println(nominalString);
                }
                
                posicionNominal++;
            }
        }

        print.close();
    }
}
