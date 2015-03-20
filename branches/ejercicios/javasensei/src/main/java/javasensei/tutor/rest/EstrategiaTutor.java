/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.tutor.rest;

import javasensei.estudiante.ModeloEstudiante;
import javasensei.ia.recognitionemotion.RecognitionEmotionalFace;
import javasensei.tutor.TutorEvaluacion;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
/**
 * REST Web Service
 *
 * @author Rock
 */
@Path("estrategiatutor")
public class EstrategiaTutor {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of EstrategiaTutor
     */
    public EstrategiaTutor() {
    }

    private static TutorEvaluacion getEvaluationAndProcessEmotion(String datosEstudiante, String fotos){
        //datosTutor es un json que contendra datos que se necesitan del tutor
        ModeloEstudiante modeloEstudiante = new ModeloEstudiante(datosEstudiante);
        RecognitionEmotionalFace recognition = new RecognitionEmotionalFace(fotos);
        
        //Cambio de emocion
        modeloEstudiante.setEmocionPrevia(modeloEstudiante.getEmocionActual());
        modeloEstudiante.setEmocionActual(recognition.getEmocion());
        
        return new TutorEvaluacion(modeloEstudiante);
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminoerroneo")
    public String getEstrategiaCaminoErroneo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos) {
        
        return getEvaluationAndProcessEmotion(datosEstudiante, fotos).obtenerEvaluacionPasoErroneo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminosuboptimo")
    public String getEstrategiaCaminoSubOptimo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos) {
        return getEvaluationAndProcessEmotion(datosEstudiante, fotos).obtenerEvaluacionPasoSubOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminooptimo")
    public String getEstrategiaCaminoOptimo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos) {
        return getEvaluationAndProcessEmotion(datosEstudiante, fotos).obtenerEvaluacionPasoOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminofinaloptimo")
    public String getEstrategiaCaminoFinalOptimo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos) {
        return getEvaluationAndProcessEmotion(datosEstudiante, fotos).obtenerEvaluacionPasoFinalOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminofinalsuboptimo")
    public String getEstrategiaCaminoFinalSubOptimo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos) {
       return getEvaluationAndProcessEmotion(datosEstudiante, fotos).obtenerEvaluacionPasoFinalSubOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("emociontutor")
    public String getEstrategiaEmocionTutor(@FormParam("datosestudiante") String datosEstudiante) {        
        //datosTutor es un json que contendra datos que se necesitan del tuturo
        TutorEvaluacion evaluacion = new TutorEvaluacion(new ModeloEstudiante(datosEstudiante));        
        
        return evaluacion.obtenerEmocionCarga();
    }
}
