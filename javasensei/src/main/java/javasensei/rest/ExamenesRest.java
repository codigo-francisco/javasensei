/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.rest;

import javasensei.db.managments.ExamenesManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author oramas
 */
@Path("examenes")
public class ExamenesRest {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ExamenesRest
     */
    public ExamenesRest() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("calificarexamenpretest")
    public String calificarExamenPreTest(
            @FormParam("idFacebook") String idFacebook,
            @FormParam("json") String jsonRespuestas,
            @FormParam("tipoExamen") String tipoExamen){
        
        return new ExamenesManager().calificarExamenPreTest(idFacebook, jsonRespuestas,tipoExamen);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("realizoExamenPreTest")
    public String realizoExamenPreTest(
            @QueryParam("idFacebook") String idFacebook){
        
        
        return new ExamenesManager().realizoExamenPreTest(idFacebook);
        
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("calificarexamenpostest")
    public String calificarExamenPosTest() throws Exception{
        
        throw new Exception();
    }
}
