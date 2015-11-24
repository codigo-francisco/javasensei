/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.tutor.rest;

import javasensei.estudiante.ModeloEstudiante;
import javasensei.ia.recognitionemotion.Emocion;
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

    private static TutorEvaluacion getEvaluationAndProcessEmotion(String datosEstudiante, String fotos, String detector){
        //datosTutor es un json que contendra datos que se necesitan del tutor
        ModeloEstudiante modeloEstudiante = new ModeloEstudiante(datosEstudiante);
        RecognitionEmotionalFace recognition = new RecognitionEmotionalFace(fotos, detector);
        
        if (modeloEstudiante.getActivarEmociones()){
            //Cambio de emocion
            modeloEstudiante.setEmocionPrevia(modeloEstudiante.getEmocionActual());
            modeloEstudiante.setEmocionActual(recognition.getEmocion());
        }else{
            modeloEstudiante.setEmocionActual(Emocion.SINEMOCION);
            modeloEstudiante.setEmocionActual(Emocion.SINEMOCION);
        }
        
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
    public String getEstrategiaCaminoErroneo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos, @FormParam("detector") String detector) {
        
        return getEvaluationAndProcessEmotion(datosEstudiante, fotos, detector).obtenerEvaluacionPasoErroneo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminosuboptimo")
    public String getEstrategiaCaminoSubOptimo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos, @FormParam("detector") String detector) {
        return getEvaluationAndProcessEmotion(datosEstudiante, fotos, detector).obtenerEvaluacionPasoSubOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminooptimo")
    public String getEstrategiaCaminoOptimo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos, @FormParam("detector") String detector) {
        return getEvaluationAndProcessEmotion(datosEstudiante, fotos, detector).obtenerEvaluacionPasoOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @param fotos
     * @param detector
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminofinaloptimo")
    public String getEstrategiaCaminoFinalOptimo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos, @FormParam("detector") String detector) {
        return getEvaluationAndProcessEmotion(datosEstudiante, fotos, detector).obtenerEvaluacionPasoFinalOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @param fotos
     * @param detector
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminofinalsuboptimo")
    public String getEstrategiaCaminoFinalSubOptimo(@FormParam("datosestudiante") String datosEstudiante, @FormParam("fotos") String fotos, @FormParam("detector") String detector) {
       return getEvaluationAndProcessEmotion(datosEstudiante, fotos, detector).obtenerEvaluacionPasoFinalSubOptimo();
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
