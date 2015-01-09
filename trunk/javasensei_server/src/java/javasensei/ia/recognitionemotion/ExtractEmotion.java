package javasensei.ia.recognitionemotion;

import javasensei.util.FileHelper;
import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import weka.classifiers.functions.*;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * @author Francisco Gonzalez Hernandez, Ramon Zatarain Cabada, Lucia Barron
 * Estrada, Raul Oramas Bustillos Email Contacto:
 * franciscogonzalez_hernandez@hotmail.com
 */
public class ExtractEmotion {

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
            File inputFile = new File(FileHelper.getInstance().getFile("javasensei/files/datosemociones.arff"));
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

    public Emocion redNeuronal(double[] values) {
        Emocion emocion = Emocion.FELIZ;

        try {
            //Creamos los datos de prueba
            Instances testData = createData(values);

            double resultIndex = multilayer.classifyInstance(testData.firstInstance());

            emocion = Emocion.valueOf((int) resultIndex);
        } catch (Exception ex) {
            System.out.println("Mensaje:    " + ex.getMessage());
            System.out.println("Causa:    " + ex.getCause());
        }

        return emocion;
    }

    private static Instances createData(double[] values) {
        ArrayList<Attribute> atributos = new ArrayList<>();
        atributos.add(new Attribute("1"));
        atributos.add(new Attribute("2"));
        atributos.add(new Attribute("3"));
        atributos.add(new Attribute("4"));
        atributos.add(new Attribute("5"));
        atributos.add(new Attribute("6"));
        atributos.add(new Attribute("7"));
        atributos.add(new Attribute("8"));
        atributos.add(new Attribute("9"));
        atributos.add(new Attribute("10"));
        ArrayList<String> valoresAtributo = new ArrayList<>();
        valoresAtributo.add(Emocion.FELIZ.toString());
        valoresAtributo.add(Emocion.SORPRESA.toString());
        valoresAtributo.add(Emocion.TRISTE.toString());
        valoresAtributo.add(Emocion.ENOJADO.toString());
        valoresAtributo.add(Emocion.NEUTRAL.toString());
        atributos.add(new Attribute("emocion", valoresAtributo));

        Instances data = new Instances("emocionesdatos", atributos, 0);
        data.setClassIndex(data.numAttributes() - 1);

        //Ahora insertamos los datos
        Instance instance = new DenseInstance(11);
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
        instance.setClassValue(Emocion.FELIZ.toString());

        data.add(instance);

        return data;
    }
}
