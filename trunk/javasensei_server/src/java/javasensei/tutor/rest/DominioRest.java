package javasensei.tutor.rest;

import javasensei.db.managments.MenuManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
     * @param dominioEstudiante
     * @return an instance of java.lang.String
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getDominioEstudiante/{dominioestudiante}")
    public String getDominioEstudiante(@PathParam("idUsuario") String dominioEstudiante) {
        //return FileHelper.getInstance().getContentFile("javasensei/files/ejercicios.json");
        MenuManager menu = new MenuManager();
        
        return menu.getCursoMenu();
        
    }
    
   
}
