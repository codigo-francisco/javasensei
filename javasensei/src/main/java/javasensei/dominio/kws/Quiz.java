package javasensei.dominio.kws;

public class Quiz {

    private String pregunta;
    private ListaReactivo listaR;

    public Quiz() {

    }

    public Quiz(String pregunta, ListaReactivo listaR) {
        this.pregunta = pregunta;
        this.listaR = listaR;
    }

    public ListaReactivo getListaR() {        
        return listaR;
    }

    public void setListaR(ListaReactivo listaR) {
        this.listaR = listaR;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    @Override
    public String toString() {
        return "Quiz{" + "pregunta=" + pregunta + ", listaR=" + listaR + '}';
    }

}
