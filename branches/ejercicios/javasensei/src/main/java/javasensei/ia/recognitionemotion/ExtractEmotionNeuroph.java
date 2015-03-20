package javasensei.ia.recognitionemotion;

import org.neuroph.core.NeuralNetwork;
import javasensei.util.FileHelper;

/**
 *
 * @author Rock
 */
public class ExtractEmotionNeuroph implements INeuralNetwork {

    private static NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(FileHelper.getInstance().getFile("files\\NeuralRecognitionFace.nnet"));
    
    @Override
    public Emocion processData(double[] coordenadas) {
        Emocion emocion = Emocion.NEUTRAL;
        
        neuralNetwork.setInput(coordenadas);
        neuralNetwork.calculate();
        
        double valorMasAlto=0;
        int posicionMasAlta = 0;
        int posicion = 0;
        
        for (double output : neuralNetwork.getOutput()){
            if (output>valorMasAlto){
                valorMasAlto = output;
                posicionMasAlta = posicion;
            }
            posicion++;
        }
        
        emocion = Emocion.getEmocion(posicionMasAlta);
        
        return emocion;
    }
    
}
