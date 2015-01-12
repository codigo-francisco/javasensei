package javasensei.dominio.kws;

import java.util.ArrayList;

/**
 *
 * @author chess
 */
public class Curso {
    private String nombreCurso;
    private ArrayList<Leccion> lecciones;

    public Curso(String nombreCurso, ArrayList<Leccion> lecciones) {
        this.nombreCurso = nombreCurso;
        this.lecciones = lecciones;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public ArrayList<Leccion> getLecciones() {
        return lecciones;
    }

    public void setLecciones(ArrayList<Leccion> lecciones) {
        this.lecciones = lecciones;
    }
    
    @Override
    public String toString() {
        return "Curso{" + "nombreCurso=" + nombreCurso + ", lecciones=" + lecciones + '}';
    }
}
