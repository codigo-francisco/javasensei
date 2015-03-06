package javasensei.tutor.rest;

import javasensei.ia.recognitionemotion.RecognitionEmotionalFace;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @deprecated La clase se encuentra obsoleta ya que no se puede usar OpenCV con ella (el restful no carga la clase, solucion pendiente, parchado con servlet)
 * @see javasensei.tutor.rest.EmocionRest
 * @author Rock
 */
@Path("emocion")
public class EmocionRest {
    
    @Context
    private UriInfo context;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("obteneremocion")
    public String obtenerEmocion(@FormParam("fotos") String fotos){
        RecognitionEmotionalFace recognition = new RecognitionEmotionalFace(fotos);
        return recognition.getEmocion();
    }
}
