package javasensei.tutor.rest;

import javasensei.db.managments.RecursoManager;
import javasensei.estudiante.ModeloEstudiante;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rock
 */
@Path("recursos")
public class RecursosRest {
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecursos(@PathParam("alumno") String alumno){
        ModeloEstudiante estudiante = new ModeloEstudiante(alumno);
        
        RecursoManager recursos = new RecursoManager();
        
        return recursos.obtenerRecursosPorCategoria(estudiante.getId());
    }
}
