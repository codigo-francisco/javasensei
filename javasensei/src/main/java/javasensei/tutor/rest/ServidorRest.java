package javasensei.tutor.rest;

import java.time.Instant;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Francisco
 */
@Path("servidor")
public class ServidorRest {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServidorRest
     */
    public ServidorRest() {
    }

    /**
     * Retrieves representation of an instance of javasensei.tutor.rest.ServidorRest
     * @return an instance of java.lang.String
     */
    @GET
    @Path("obtenerHora")
    public String obtenerHora() {
        return new Long(Instant.now().toEpochMilli()).toString();
    }
}
