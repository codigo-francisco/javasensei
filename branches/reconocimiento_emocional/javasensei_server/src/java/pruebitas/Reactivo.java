
package pruebitas;

/**
 *
 * @author chess
 */
public class Reactivo {
    private String pregunta;
    private int opcionCorrecta;

    public Reactivo(String pregunta, int opcionCorrecta) {
        this.pregunta = pregunta;
        this.opcionCorrecta = opcionCorrecta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public int getOpcionCorrecta() {
        return opcionCorrecta;
    }

    public void setOpcionCorrecta(int opcionCorrecta) {
        this.opcionCorrecta = opcionCorrecta;
    }
    
    
}
