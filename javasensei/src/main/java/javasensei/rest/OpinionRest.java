package javasensei.rest;

import javasensei.db.managments.OpinionEjercicioManager;
import javasensei.db.managments.OpinionSenseiManager;
import javasensei.ia.sentitext.ExtractPolarityTextPython;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author oramas
 */
@Path("opinion")
public class OpinionRest {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of OpinionRest
     */
    public OpinionRest() {
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("guardar_eval_sensei")
    public String guardarEvalSensei(
            @FormParam("idFacebook") String idFacebook,
            @FormParam("json") String jsonRespuestas){
        return new OpinionSenseiManager().saveOpinionSensei(idFacebook, jsonRespuestas);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("realizo_eval_sensei")
    public String realizoEvalSensei(
            @QueryParam("idFacebook") String idFacebook){
        return new OpinionSenseiManager().realizoEvalSensei(idFacebook);
    }

    @POST
    
    @Path("guardar_opinion_ejercicio")
    public String guardarOpinionEjercicio(
            @FormParam("idFacebook") String idFacebook,
            @FormParam("idEjercicio") String idEjercicio,
            @FormParam("opinion") String opinion) {
        
        //Marca un error en el log aunque no afecta al guardado
        ExtractPolarityTextPython p = new ExtractPolarityTextPython();
        return new OpinionEjercicioManager().saveOpinionEjercicio(idFacebook, idEjercicio, opinion, p.processData(opinion));
    }
    
}
