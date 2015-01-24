package javasensei.tutor.rest;

import javasensei.db.managments.RankingEjerciciosManager;
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
    public String ejercicios(@PathParam("usuario") String usuario){
        ModeloEstudiante estudiante = new ModeloEstudiante(usuario);
        
        RankingEjerciciosManager manager = new RankingEjerciciosManager(estudiante);
        
        return manager.getRecommenders();
    }
}
