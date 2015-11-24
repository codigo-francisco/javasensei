package javasensei.ia.recognitionemotion;

/**
 *
 * @author Rock
 * @param <T>
 */
public interface INeuralNetwork<T> {
    public Emocion processData(T datos);
}
