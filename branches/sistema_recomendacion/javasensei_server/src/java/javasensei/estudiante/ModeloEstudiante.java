package javasensei.estudiante;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import javasensei.ia.recognitionemotion.Emocion;
import javasensei.tutor.Habilidad;

/**
 *
 * @author Rock
 */
public class ModeloEstudiante implements java.io.Serializable, DBInterface {

    private String id;
    private String token;
    private Emocion emocionActual = Emocion.NEUTRAL;
    private Emocion emocionPrevia = Emocion.NEUTRAL;
    private Habilidad habilidadGlobal = Habilidad.MALA;
    private double calidadRespuesta = 0; //Parametros internos al crear un estudiante nuevo

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
