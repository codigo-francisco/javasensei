package pruebitas;

import javasensei.dominio.kws.ModeloDominio;

public class TestJSON {
    public static void main(String args[]) throws java.io.IOException {
        ModeloDominio md = new ModeloDominio();
        //md.inicializarDominio();
        
        md.readJSON("C:\\Users\\chess\\Documents\\entrada.json");
        System.out.println(md.getJSON());
        md.writeJSON("C:\\Users\\chess\\Documents\\salida.json");
        
    }
}
