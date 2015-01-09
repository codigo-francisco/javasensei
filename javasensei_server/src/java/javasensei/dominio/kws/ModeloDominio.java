package javasensei.dominio.kws;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chess
 */
public class ModeloDominio {

    private Curso curso;
    private final Gson gson;

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public ModeloDominio() {
        gson = new Gson();
    }

    //Solo para ver el procedimiento que se tendría que seguir
    //la inicialización del dominio...es un metodo DUMMY
    public void inicializarDominio() {
        List<Ejercicio> ejercicios = new ArrayList();
        ejercicios.add(new Ejercicio(1, "Ejercicio1"));
        ejercicios.add(new Ejercicio(2, "Ejercicio2"));
        List<Leccion> lecciones = new ArrayList();
        lecciones.add(new Leccion(1, "Introducción a Java", ejercicios));
        lecciones.add(new Leccion(2, "Variables y cálculos", ejercicios));
        lecciones.add(new Leccion(3, "Instrucciones de selección", ejercicios));
        lecciones.add(new Leccion(4, "Instrucciones de repetición", ejercicios));

        curso = new Curso("Aprenda Java", lecciones);
    }

    //Lee un archivo JSON y lo carga en memoria
    public void readJSON(String archivo) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        File newArchivo = new File(archivo);
        JsonElement datos = parser.parse(newArchivo.toString());
        curso = gson.fromJson(datos, Curso.class);
    }

    //Pasa a un archivo de texto un archivo JSON
    public void writeJSON(String archivo) throws IOException {
        File newArchivo = new File(archivo);
        FileWriter writer = new FileWriter(newArchivo.toString());
        writer.write(getJSON());
    }

    public String getJSON() {
        return gson.toJson(curso);
    }

}
