/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.tutor.rest;

import javasensei.estudiante.ModeloEstudiante;
import javasensei.tutor.TutorEvaluacion;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminoerroneo")
    public String getEstrategiaCaminoErroneo(@FormParam("datosestudiante") String datosEstudiante) {
        //datosTutor es un json que contendra datos que se necesitan del tuturo
        TutorEvaluacion evaluacion = new TutorEvaluacion(new ModeloEstudiante(datosEstudiante));        
        //String resultado = evaluacion.obtenerEvaluacionPasoErroneo();
        
        return evaluacion.obtenerEvaluacionPasoErroneo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminosuboptimo")
    public String getEstrategiaCaminoSubOptimo(@FormParam("datosestudiante") String datosEstudiante) {
        //datosTutor es un json que contendra datos que se necesitan del tuturo
        TutorEvaluacion evaluacion = new TutorEvaluacion(new ModeloEstudiante(datosEstudiante));        
        //String resultado = evaluacion.obtenerEvaluacionPasoErroneo();
        
        return evaluacion.obtenerEvaluacionPasoSubOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminooptimo")
    public String getEstrategiaCaminoOptimo(@FormParam("datosestudiante") String datosEstudiante) {
        
        //datosTutor es un json que contendra datos que se necesitan del tuturo
        TutorEvaluacion evaluacion = new TutorEvaluacion(new ModeloEstudiante(datosEstudiante));        
        //String resultado = evaluacion.obtenerEvaluacionPasoErroneo();
        
        return evaluacion.obtenerEvaluacionPasoOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminofinaloptimo")
    public String getEstrategiaCaminoFinalOptimo(@FormParam("datosestudiante") String datosEstudiante) {
        
        //datosTutor es un json que contendra datos que se necesitan del tuturo
        TutorEvaluacion evaluacion = new TutorEvaluacion(new ModeloEstudiante(datosEstudiante));        
        //String resultado = evaluacion.obtenerEvaluacionPasoErroneo();
        
        return evaluacion.obtenerEvaluacionPasoFinalOptimo();
    }
    
    /**
     * Retrieves representation of an instance of javasensei.tutor.EstrategiaTutor
     * @param datosEstudiante
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminofinalsuboptimo")
    public String getEstrategiaCaminoFinalSubOptimo(@FormParam("datosestudiante") String datosEstudiante) {
        
        //datosTutor es un json que contendra datos que se necesitan del tuturo
        TutorEvaluacion evaluacion = new TutorEvaluacion(new ModeloEstudiante(datosEstudiante));        
        //String resultado = evaluacion.obtenerEvaluacionPasoErroneo();
        
        return evaluacion.obtenerEvaluacionPasoFinalSubOptimo();
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
        //TODO: checar este metodo
        
        //datosTutor es un json que contendra datos que se necesitan del tuturo
        TutorEvaluacion evaluacion = new TutorEvaluacion(new ModeloEstudiante(datosEstudiante));        
        //String resultado = evaluacion.obtenerEvaluacionPasoErroneo();
        
        return evaluacion.obtenerEmocionCarga();
    }
}
