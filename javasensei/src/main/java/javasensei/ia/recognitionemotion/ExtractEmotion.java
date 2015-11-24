package javasensei.ia.recognitionemotion;

import java.io.File;
import java.util.ArrayList;
import javasensei.exceptions.JavaException;
import javasensei.util.FileHelper;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * @author Francisco Gonzalez Hernandez, Ramon Zatarain Cabada, Lucia Barron
 * Estrada, Raul Oramas Bustillos Email Contacto:
 * franciscogonzalez_hernandez@hotmail.com
 */
public class ExtractEmotion implements INeuralNetwork<double[]> {

    private static MultilayerPerceptron multilayer;

    private static boolean loadFiles = false;

    public ExtractEmotion() {
        if (!loadFiles) { //Carga de 
            loadFiles();
            loadFiles = true;
        }
    }

    public final void loadFiles() {
        //Obtenemos los datos de entrenamiento
        try {
            File inputFile = new File(FileHelper.getInstance().getFile("files/datosemociones.arff"));
            ArffLoader arffLoader = new ArffLoader();
            arffLoader.setSource(inputFile);
            Instances trainData = arffLoader.getDataSet();
            trainData.setClassIndex(trainData.numAttributes() - 1);

            //Construimos el esquema de entrenamiento, multilayer perceptron
            multilayer = new MultilayerPerceptron();
            String[] opciones = {"-L", "0.3", "-M", "0.2", "-N", "2000", "-V", "25", "-S", "0", "-E", "20", "-H", "a"};
            /* 
             L : Tasa de aprendizaje
             M : Tasa de momentum para el algoritmo de propagación hacia atrás
             N : Número de epochs
             V : Porcentaje del tamaño del conjunto de validación
             S : Semilla
             E : Umbral para el número consecutivo de errores
             H : Numero de nodos en cada capa a: para (atributos + clases)/2
             i: para atributos
             o: para clases
             t: para atributos+clases
             */
            multilayer.setOptions(opciones);
            multilayer.buildClassifier(trainData);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    @Override
    public Emocion processData(double[] datos) {
        Emocion emocion = Emocion.NEUTRAL;

        try {
            //Creamos los datos de prueba
            Instances testData = createData(datos);

            double resultIndex = multilayer.classifyInstance(testData.firstInstance());

            emocion = Emocion.getEmocion((int) resultIndex);
        } catch (Exception ex) {
            JavaException.printMessage(ex, System.err);
        }

        return emocion;
    }

    private static Instances createData(double[] values) {
        FastVector atributos = new FastVector();
        atributos.addElement(new Attribute("1"));
        atributos.addElement(new Attribute("2"));
        atributos.addElement(new Attribute("3"));
        atributos.addElement(new Attribute("4"));
        atributos.addElement(new Attribute("5"));
        atributos.addElement(new Attribute("6"));
        atributos.addElement(new Attribute("7"));
        atributos.addElement(new Attribute("8"));
        atributos.addElement(new Attribute("9"));
        atributos.addElement(new Attribute("10"));
        FastVector valoresAtributo = new FastVector();
        valoresAtributo.addElement(Emocion.FELIZ.toString());
        valoresAtributo.addElement(Emocion.SORPRESA.toString());
        valoresAtributo.addElement(Emocion.TRISTE.toString());
        valoresAtributo.addElement(Emocion.ENOJADO.toString());
        valoresAtributo.addElement(Emocion.NEUTRAL.toString());
        atributos.addElement(new Attribute("emocion", valoresAtributo));

        Instances data = new Instances("emocionesdatos", atributos, 0);
        data.setClassIndex(data.numAttributes() - 1);

        //Ahora insertamos los datos
        Instance instance = new Instance(11); //DenseInstance(11);
        instance.setDataset(data);

        instance.setValue(0, values[0]);
        instance.setValue(1, values[1]);
        instance.setValue(2, values[2]);
        instance.setValue(3, values[3]);
        instance.setValue(4, values[4]);
        instance.setValue(5, values[5]);
        instance.setValue(6, values[6]);
        instance.setValue(7, values[7]);
        instance.setValue(8, values[8]);
        instance.setValue(9, values[9]);
        instance.setClassValue(Emocion.NEUTRAL.toString());

        data.add(instance);

        return data;
    }
}
