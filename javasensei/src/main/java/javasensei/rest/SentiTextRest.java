package javasensei.rest;

import javasensei.ia.sentitext.ExtractPolarityTextPython;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author oramas
 */
@Path("sentitext")
public class SentiTextRest {

    //@Context
    // private UriInfo context;
    // private String cadTexto;
    
    /**
     * Creates a new instance of SentiTextRest
     */
    public SentiTextRest() {
    }

    @Path("evalpolaridad")
    @POST
    public String obtenerPolaridadTexto(@FormParam("textoEval") String texto) {
        ExtractPolarityTextPython p = new ExtractPolarityTextPython();
        return(p.processData(texto));
    }
       
}
