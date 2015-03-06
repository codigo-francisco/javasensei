package javasensei.dominio.kws;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;
import javasensei.db.DBInterface;

/**
 *
 * @author chess
 */
public class Leccion implements java.io.Serializable, DBInterface {

    private int id;
    private String titulo;
    private ArrayList<Ejercicio> ejercicios;

    
    public Leccion(int id, String titulo, ArrayList<Ejercicio> ejercicios) {
        this.id = id;
        this.titulo = titulo;
        this.ejercicios = ejercicios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    @Override
    public String toString() {
        return "Leccion{" + "id=" + id + ", titulo=" + titulo + ", ejercicios=" + ejercicios + '}';
    }

    @Override
    public DBObject convertToDBObject() {
        DBObject dbObject = new BasicDBObject();
        
        dbObject.put("id", getId());
        dbObject.put("titulo", getTitulo());
        dbObject.put("ejercicios",getEjercicios());

        return dbObject;
    }
}
