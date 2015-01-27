package javasensei.tutor.rest;

import javasensei.db.managments.RecursoManager;
import javasensei.estudiante.ModeloEstudiante;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rock
 */
public class RecursosRest {
    
    
    @GET
    @Path("recursos/{usuario}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecursos(@PathParam("usuario") String usuario){
        ModeloEstudiante estudiante = new ModeloEstudiante(usuario);
        
        RecursoManager recursos = new RecursoManager();
        
        return recursos.obtenerRecursosPorCategoria(estudiante.getId());
    }
}
