package javasensei.dominio.kws;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public void inicializarDominio() {
    }

    // Lee un archivo JSON y lo carga en memoria
    public void readJSON(String archivo) throws FileNotFoundException, IOException {
        InputStream is = ModeloDominio.class.getClassLoader().getResourceAsStream(archivo);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
            curso = gson.fromJson(bufferedReader, Curso.class);
            //System.out.println(curso);
        }
    }

    // Pasa a un archivo de texto un archivo JSON
    public void writeJSON(String archivo) throws IOException {
        File newArchivo = new File(archivo);
        FileWriter writer = new FileWriter(newArchivo.toString());
        writer.write(getJSON());
    }

    public String getJSON() {
        return gson.toJson(curso);
    }
}
