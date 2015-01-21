package javasensei.tutor.rest;

import javasensei.db.managments.RecursoManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rock
 */
@Path("recursos")
public class Recursos {
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecursos(){
        RecursoManager recursos = new RecursoManager();
        
        return recursos.obtenerRecursosPorCategoria();
    }
}
