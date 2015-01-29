package javasensei.dominio.kws;

/**
 *
 * @author chess
 */
public class Ejercicio {

     private int id;
    private String url;
    private String titulo;

    public Ejercicio(int id, String url, String titulo) {
        this.id = id;
        this.url = url;
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "Ejercicio{" + "id=" + id + ", url=" + url + ", titulo=" + titulo + '}';
    }
}
