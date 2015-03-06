package javasensei.ia.recognitionemotion;

/**
 *
 * @author Rock
 */
public class RecognitionResult {
    private double[] coordenadas;
    private boolean isEmocion = false;

    public double[] getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(double[] coordenadas) {
        this.coordenadas = coordenadas;
    }

    public boolean isHayEmocion() {
        return isEmocion;
    }

    public void setHayEmocion(boolean isEmocion) {
        this.isEmocion = isEmocion;
    }
    
    
}
