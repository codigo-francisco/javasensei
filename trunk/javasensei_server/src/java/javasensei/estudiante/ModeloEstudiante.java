package javasensei.estudiante;

import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import javasensei.ia.recognitionemotion.Emocion;
import javasensei.tutor.Habilidad;
import com.google.gson.JsonObject;

/**
 *
 * @author Rock
 */
public class ModeloEstudiante implements java.io.Serializable, DBInterface {

    private long id;
    private String token;
    private Emocion emocionActual = Emocion.NEUTRAL;
    private Emocion emocionPrevia = Emocion.NEUTRAL;
    private Habilidad habilidadGlobal = Habilidad.MALA;
    private double calidadRespuesta = 0; //Parametros internos al crear un estudiante nuevo
    
    public ModeloEstudiante(){
        
    }
    
    public ModeloEstudiante(String jsonEstudiante){
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(jsonEstudiante).getAsJsonObject();

            //Se construye el objeto del estudiante
            id = jsonObj.get("id").getAsLong();
            token = jsonObj.get("token").getAsString();
            calidadRespuesta = jsonObj.get("calidadRespuesta").getAsDouble();
            emocionActual = Emocion.getEmocion(jsonObj.get("emocionActual").getAsString());
            emocionPrevia = Emocion.getEmocion(jsonObj.get("emocionPrevia").getAsString());
            habilidadGlobal = Habilidad.getHabilidad(jsonObj.get("habilidadGlobal").getAsString());
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Emocion getEmocionActual() {
        return emocionActual;
    }

    public void setEmocionActual(Emocion emocionActual) {
        this.emocionActual = emocionActual;
    }

    public Emocion getEmocionPrevia() {
        return emocionPrevia;
    }

    public void setEmocionPrevia(Emocion emocionPrevia) {
        this.emocionPrevia = emocionPrevia;
    }

    public Habilidad getHabilidadGlobal() {
        return habilidadGlobal;
    }

    public void setHabilidadGlobal(Habilidad habilidadGlobal) {
        this.habilidadGlobal = habilidadGlobal;
    }

    public double getCalidadRespuesta() {
        return calidadRespuesta;
    }

    public void setCalidadRespuesta(double calidadRespuesta) {
        this.calidadRespuesta = calidadRespuesta;
    }

    @Override
    public DBObject convertToDBObject() {
        DBObject dbObject = new BasicDBObject();
        
        dbObject.put("id", getId());
        dbObject.put("token", getToken());

        dbObject.put("emocionActual", getEmocionActual().toString());
        dbObject.put("emocionPrevia", getEmocionPrevia().toString());
        dbObject.put("habilidadGlobal", getHabilidadGlobal().toString());
        dbObject.put("calidadRespuesta", getCalidadRespuesta());

        return dbObject;
    }

}
