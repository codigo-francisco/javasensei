package javasensei.dominio.kws;

/**
 *
 * @author chess
 */
public class Reactivo {

    private String pregunta;
    private String respuesta;

    public Reactivo() {

    }

    public Reactivo(String pregunta, String respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    @Override
    public String toString() {
        return "Reactivo{" + "pregunta=" + pregunta + ", respuesta=" + respuesta + '}';
    }

}
