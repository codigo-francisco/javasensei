package javasensei.tutor.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author Rock
 */
@Path("recomendacion")
public class Recomendacion {
    
    @Path("ejercicios/{usuario}")
    @GET
    public String ejercicios(@PathParam("usuario") String usuario){
        
        return "";
    }
}
