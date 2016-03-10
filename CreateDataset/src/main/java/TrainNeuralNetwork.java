import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.nnet.learning.KohonenLearning;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.nnet.learning.ResilientPropagation;
/**
 *
 * @author Rock
 */
public class TrainNeuralNetwork {
    static double ultimoError = 0.08177429190816557;
    static double actualError = Double.MAX_VALUE;
    
    public static void main(String[] args){
        String pathNeural = "E:\\javasensei\\Red Neuronal NeuroPH\\javasensei\\Neural Networks\\";
        DataSet dataSet = DataSet.createFromFile("E:\\javasensei\\dataset.csv", 10, 5, ",");
        NeuralNetwork neural = NeuralNetwork.createFromFile(pathNeural+"NewNeuralNetwork1.nnet");
        
        BackPropagation train = new BackPropagation();        
        
        /*train.setMinLearningRate(.1);
        train.setMaxLearningRate(0.3);
        train.setLearningRateChange(.1);
        
        train.setMomentumChange(.1);
        train.setMinMomentum(0.1);
        train.setMaxMomentum(0.8);        
        train.setMomentum(0.9);*/
        
        //train.setMaxIterations(10000);
        
        train.setLearningRate(0.9);
        train.setMaxError(0.01);
        train.addListener(new LearningEventListener() {
            @Override
            public void handleLearningEvent(LearningEvent le) {                
                System.out.print("Learning Rate:" + train.getLearningRate());
                //System.out.print(", Momentum:" + train.getMomentum());
                System.out.print(", Error:" + train.getTotalNetworkError());
                System.out.print(", Iteration:" + train.getCurrentIteration());
                
                neural.save(pathNeural+"respaldo.nnet");
                System.out.println(", Red guardada en respaldo");
            }
        });
        neural.calculate();
        neural.reset();
        neural.randomizeWeights();
        neural.setLearningRule(train);
        neural.learn(dataSet);
    }
}
