package javasensei.tutor.rest;

import javasensei.db.managments.RankingManager;
import javasensei.estudiante.ModeloEstudiante;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Rock
 */
@Path("recomendacion")
public class RecomendacionRest {
    
    @Context
    private UriInfo context;
    
    /**
     * RecomendacionRest de ejercicios
     * @param usuario
     * @return 
     */
    @Path("ejercicios/{usuario}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String ejercicios(@PathParam("usuario") String usuario){
        ModeloEstudiante estudiante = new ModeloEstudiante(usuario);
        
        RankingManager manager = new RankingManager(estudiante);
        
        return manager.getRecommendersExercises(5, true);
    }
}
