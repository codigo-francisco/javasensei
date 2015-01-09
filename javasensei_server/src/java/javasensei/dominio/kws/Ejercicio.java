/*
 * Leccion.java
 */
package javasensei.dominio.kws;

/**
 *
 * @author chess
 */
public class Ejercicio {

    private int numEjercicio;
    private String urlEjercicio;

    public Ejercicio() {
    }

    public Ejercicio(int numLeccion, String tituloLeccion) {
        this.numEjercicio = numLeccion;
        this.urlEjercicio = tituloLeccion;
    }

    public int getNumEjercicio() {
        return numEjercicio;
    }

    public void setNumEjercicio(int numEjercicio) {
        this.numEjercicio = numEjercicio;
    }

    public String getUrlEjercicio() {
        return urlEjercicio;
    }

    public void setUrlEjercicio(String urlEjercicio) {
        this.urlEjercicio = urlEjercicio;
    }

}
