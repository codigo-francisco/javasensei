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
        return recognition.getEmocionString();
    }
}
