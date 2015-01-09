package javasensei.dominio.kws;

public class ListaReactivo {
    private String id;
    //private List<Reactivo> reactivos;

    public ListaReactivo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ListaReactivo{" + "id=" + id + '}';
    }
    
    
}
