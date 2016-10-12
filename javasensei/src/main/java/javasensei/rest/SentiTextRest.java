package javasensei.rest;

import javasensei.ia.sentitext.ExtractPolarityTextPython;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author oramas
 */
@Path("sentiText")
public class SentiTextRest {

    @Context
    private UriInfo context;
    private String cadTexto;
    
    /**
     * Creates a new instance of SentiTextRest
     */
    public SentiTextRest() {
    }

    /**
     * Retrieves representation of an instance of javasensei.rest.SentiTextRest
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of SentiTextRest
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
 
    @GET
    @Path("sentiText")
    public void obtenerTexto(@FormParam("cadTexto") String texto) {
        cadTexto = texto;
    }
    
    @Path("sentiText")
    @POST
    public String obtenerPolaridadTexto() {
        ExtractPolarityTextPython p = new ExtractPolarityTextPython();
        return(p.processData(cadTexto));
    }
       
}
