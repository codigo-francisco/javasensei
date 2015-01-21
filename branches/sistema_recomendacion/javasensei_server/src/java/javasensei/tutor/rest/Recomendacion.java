package javasensei.tutor.rest;

import javasensei.db.managments.RecomendacionesManager;
import javasensei.estudiante.ModeloEstudiante;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Rock
 */
@Path("recomendacion")
public class Recomendacion {
    
    @Context
    private UriInfo context;
    
    /**
     * Recomendacion de ejercicios
     * @param usuario
     * @return 
     */
    @Path("ejercicios/{usuario}")
    @GET
    public String ejercicios(@PathParam("usuario") String usuario){
        ModeloEstudiante estudiante = new ModeloEstudiante(usuario);
        
        RecomendacionesManager recomendacionManager = new RecomendacionesManager();
        String respuesta = recomendacionManager.recomendacionEjercicio(estudiante);
        
        return respuesta;
    }
}
