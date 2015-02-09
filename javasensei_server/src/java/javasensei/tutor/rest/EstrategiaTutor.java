/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.tutor.rest;

import javasensei.estudiante.ModeloEstudiante;
import javasensei.tutor.TutorEvaluacion;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminoerroneo/{datosestudiante}")
    public String getEstrategiaCaminoErroneo(@PathParam("datosestudiante") String datosEstudiante) {
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
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminosuboptimo/{datosestudiante}")
    public String getEstrategiaCaminoSubOptimo(@PathParam("datosestudiante") String datosEstudiante) {
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
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminooptimo/{datosestudiante}")
    public String getEstrategiaCaminoOptimo(@PathParam("datosestudiante") String datosEstudiante) {
        
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
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminofinaloptimo/{datosestudiante}")
    public String getEstrategiaCaminoFinalOptimo(@PathParam("datosestudiante") String datosEstudiante) {
        
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
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("caminofinalsuboptimo/{datosestudiante}")
    public String getEstrategiaCaminoFinalSubOptimo(@PathParam("datosestudiante") String datosEstudiante) {
        
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
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("emociontutor/{datosestudiante}")
    public String getEstrategiaEmocionTutor(@PathParam("datosestudiante") String datosEstudiante) {
        //TODO: checar este metodo
        
        //datosTutor es un json que contendra datos que se necesitan del tuturo
        TutorEvaluacion evaluacion = new TutorEvaluacion(new ModeloEstudiante(datosEstudiante));        
        //String resultado = evaluacion.obtenerEvaluacionPasoErroneo();
        
        return evaluacion.obtenerEmocionCarga();
    }
}
