package javasensei.estudiante;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.time.Instant;
import java.util.Date;
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
    private boolean activarEmociones = true;
    private double tiempo = 0;
    private Date creado = Date.from(Instant.now());

    public ModeloEstudiante() {
        
    }

    public ModeloEstudiante(String jsonEstudiante) {
        BasicDBObject jsonObj = BasicDBObject.parse(jsonEstudiante);

        //Se construye el objeto del estudiante
        id = jsonObj.getLong("id");
        idFacebook = jsonObj.getString("idFacebook").replace("\"", "");
        token = jsonObj.getString("token");
        calidadRespuesta = new Double(jsonObj.getString("calidadRespuesta"));
        emocionActual = Emocion.getEmocion(jsonObj.getString("emocionActual"));
        emocionPrevia = Emocion.getEmocion(jsonObj.getString("emocionPrevia"));
        habilidadGlobal = new Double(jsonObj.getString("habilidadGlobal"));
        activarEmociones = jsonObj.getBoolean("activarEmociones");
        tiempo = jsonObj.getDouble("tiempo");
        creado = jsonObj.getDate("creado", Date.from(Instant.now()));
    }
    
    @Override
    public DBObject convertToDBObject() {
        return convertToDBObject(false);
    }
    
    @Override
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
        dbObject.put("activarEmociones", getActivarEmociones());
        dbObject.put("tiempo", getTiempo());
        dbObject.put("creado", getCreado());

        return dbObject;
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
    
    public boolean getActivarEmociones(){
        return activarEmociones;
    }
    
    public void setActivarEmociones(boolean activarEmociones){
        this.activarEmociones = activarEmociones;
    }
    
    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }
    
    public Date getCreado() {
        return creado;
    }
}
