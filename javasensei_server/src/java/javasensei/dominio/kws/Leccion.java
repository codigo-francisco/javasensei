/*
 * Leccion.java
 */
package javasensei.dominio.kws;

import java.util.List;

/**
 *
 * @author chess
 */
public class Leccion {

    private int numLeccion;
    private String nombreLeccion;
    private List<Ejercicio> ejercicios;

    public Leccion() {
    }

    public Leccion(int numLeccion, String nombreLeccion, List ejercicios) {
        this.numLeccion = numLeccion;
        this.nombreLeccion = nombreLeccion;
        this.ejercicios = ejercicios;
    }

    public String getNombreLeccion() {
        return nombreLeccion;
    }

    public void setNombreLeccion(String nombreLeccion) {
        this.nombreLeccion = nombreLeccion;
    }

    /**
     *
     * @return
     */
    public List getLeccion() {
        return ejercicios;
    }

    public void setLeccion(List leccion) {
        this.ejercicios = leccion;
    }

    public int getNumLeccion() {
        return numLeccion;
    }

    public void setNumLeccion(int numLeccion) {
        this.numLeccion = numLeccion;
    }

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
