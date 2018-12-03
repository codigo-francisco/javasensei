package javasensei.estudiante;

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
    private boolean aceptarCondiciones = false;
    private boolean mostrarTutorialPrincipal  = false;
    private boolean mostrarTutorialEjercicio  = false;
    private double tiempo = 0;
    private Date creado = Date.from(Instant.now());
    private String nombre;
    private String nombreFacebook;
    private int edad;
    private String fotografia;
    private String sexo;

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
        aceptarCondiciones = jsonObj.getBoolean("aceptarCondiciones");
        mostrarTutorialEjercicio = jsonObj.getBoolean("mostrarTutorialEjercicio");
        mostrarTutorialPrincipal = jsonObj.getBoolean("mostrarTutorialPrincipal");
        nombre = jsonObj.getString("nombre");
        nombreFacebook = jsonObj.getString("nombreFacebook");
        edad = jsonObj.getInt("edad");
        sexo = jsonObj.getString("sexo");
        fotografia = jsonObj.getString("fotografia");        
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
        dbObject.put("aceptarCondiciones", getAceptarCondiciones());
        dbObject.put("mostrarTutorialPrincipal", getMostrarTutorialPrincipal());
        dbObject.put("mostrarTutorialEjercicio", getMostrarTutorialEjercicio());
        dbObject.put("nombre", getNombre());
        dbObject.put("nombreFacebook", getNombreFacebook());
        dbObject.put("edad", getEdad());
        dbObject.put("fotografia", getFotografia());
        dbObject.put("sexo", getSexo());
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
    
     public boolean getAceptarCondiciones() {
        return aceptarCondiciones;
    }

    public void setAceptarCondiciones(boolean aceptarCondiciones) {
        this.aceptarCondiciones = aceptarCondiciones;
    }

    /**
     * @return the mostrarTutorialPrincipal
     */
    public boolean getMostrarTutorialPrincipal() {
        return mostrarTutorialPrincipal;
    }

    /**
     * @param mostrarTutorialPrincipal the mostrarTutorialPrincipal to set
     */
    public void setMostrarTutorialPrincipal(boolean mostrarTutorialPrincipal) {
        this.mostrarTutorialPrincipal = mostrarTutorialPrincipal;
    }

    /**
     * @return the mostrarTutorialEjercicio
     */
    public boolean getMostrarTutorialEjercicio() {
        return mostrarTutorialEjercicio;
    }

    /**
     * @param mostrarTutorialEjercicio the mostrarTutorialEjercicio to set
     */
    public void setMostrarTutorialEjercicio(boolean mostrarTutorialEjercicio) {
        this.mostrarTutorialEjercicio = mostrarTutorialEjercicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreFacebook() {
        return nombreFacebook;
    }

    public void setNombreFacebook(String nombreFacebook) {
        this.nombreFacebook = nombreFacebook;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
