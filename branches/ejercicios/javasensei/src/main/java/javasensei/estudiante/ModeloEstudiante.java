package javasensei.estudiante;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import javasensei.ia.recognitionemotion.Emocion;

/**
 *
 * @author Rock
 */
public class ModeloEstudiante implements java.io.Serializable, DBInterface {

    private long id;
    private String idFacebook;
    private String token;
    private Emocion emocionActual = Emocion.NEUTRAL;
    private Emocion emocionPrevia = Emocion.NEUTRAL;
    private double habilidadGlobal = 0;
    private double calidadRespuesta = 0; //Parametros internos al crear un estudiante nuevo

    public ModeloEstudiante() {

    }

    public ModeloEstudiante(String jsonEstudiante) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(jsonEstudiante).getAsJsonObject();

        //Se construye el objeto del estudiante
        id = jsonObj.get("id").getAsLong();
        idFacebook = jsonObj.get("idFacebook").toString().replace("\"", "");
        token = jsonObj.get("token").getAsString();
        calidadRespuesta = jsonObj.get("calidadRespuesta").getAsDouble();
        emocionActual = Emocion.getEmocion(jsonObj.get("emocionActual").getAsString());
        emocionPrevia = Emocion.getEmocion(jsonObj.get("emocionPrevia").getAsString());
        habilidadGlobal = jsonObj.get("habilidadGlobal").getAsDouble();
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

    public Double getHabilidadGlobal() {
        return habilidadGlobal;
    }

    public void setHabilidadGlobal(Double habilidadGlobal) {
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
        return convertToDBObject(false);
    }
    
    public DBObject convertToDBObject(boolean save) {
        DBObject dbObject = new BasicDBObject();

        dbObject.put("idFacebook", getIdFacebook());
        if (save) {
            dbObject.put("id", "ultimoIdAlumno()");
        } else {
            dbObject.put("id", getId());
        }
        dbObject.put("token", getToken());

        dbObject.put("emocionActual", getEmocionActual().toString());
        dbObject.put("emocionPrevia", getEmocionPrevia().toString());
        dbObject.put("habilidadGlobal", getHabilidadGlobal().toString());
        dbObject.put("calidadRespuesta", getCalidadRespuesta());

        return dbObject;
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

}
