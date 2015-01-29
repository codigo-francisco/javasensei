package javasensei.dominio.kws;

import java.util.ArrayList;

/**
 *
 * @author chess
 */
public class Leccion {

    private int id;
    private String titulo;
    private ArrayList<Ejercicio> ejercicios;

    public Leccion(int id, String titulo, ArrayList<Ejercicio> ejercicios) {
        this.id = id;
        this.titulo = titulo;
        this.ejercicios = ejercicios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    @Override
    public String toString() {
        return "Leccion{" + "id=" + id + ", titulo=" + titulo + ", ejercicios=" + ejercicios + '}';
    }
}
