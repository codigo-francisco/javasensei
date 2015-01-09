package javasensei.dominio.kws;

import java.util.List;

/**
 *
 * @author chess
 */
public class Curso {
    private String nombreCurso;
    private List<Leccion> lecciones;

    public Curso(String nombreCurso, List<Leccion> lecciones) {
        this.nombreCurso = nombreCurso;
        this.lecciones = lecciones;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public List<Leccion> getLecciones() {
        return lecciones;
    }

    public void setLecciones(List<Leccion> lecciones) {
        this.lecciones = lecciones;
    }
    
    
}
