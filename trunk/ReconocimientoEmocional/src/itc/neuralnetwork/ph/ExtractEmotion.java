package itc.neuralnetwork.ph;

import itc.neuralnetwork.Emocion;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
/**
 *
 * @author Rock
 */
public class ExtractEmotion {
    
    public Emocion processData(double[] coordenadas){
        // load saved neural network
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile("C:\\Users\\Rock\\Documents\\NetBeansProjects\\Proyecto Prueba\\Neural Networks\\NeuralRecognitionFace.nnet");
        
        neuralNetwork.setInput(coordenadas);
        
        neuralNetwork.calculate();
        
        int index = 1;
        
        for(Neuron neuron : neuralNetwork.getOutputNeurons()){
            System.out.printf("Salida %s: %s\n", index, neuron.getOutput());
            index++;
        }
        
        return Emocion.ENOJADO;
    }
    
}
