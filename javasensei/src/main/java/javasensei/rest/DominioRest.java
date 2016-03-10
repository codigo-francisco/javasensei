package javasensei.rest;

import javasensei.db.managments.MenuManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author chess
 */
@Path("dominioEstudiante")
public class DominioRest {
    
    @Context
    private UriInfo context;

    public DominioRest() {
    }

    /**
     * Retrieves representation of an instance of javasensei.tutor.DominioRest
     * @param idAlumno
     * @return an instance of java.lang.String
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getDominioEstudiante/{idAlumno}")
    public String getDominioEstudiante(@PathParam("idAlumno") Long idAlumno) {
        
        MenuManager menu = new MenuManager();
        
        return menu.getCursoMenu(idAlumno);
        
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getDataGraphics")
    public String getDataGraphics(@QueryParam("idAlumno") Long idAlumno) {
        
        MenuManager menu = new MenuManager();
        
        return menu.getDataGraphics(idAlumno);
        
    }
    
   
}
