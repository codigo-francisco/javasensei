/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.rest;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import java.util.List;
import java.util.Map;
import javasensei.db.managments.ExamenesManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
            @FormParam("json") String jsonRespuestas){
              
        
        return new ExamenesManager().calificarExamenPreTest(idFacebook, jsonRespuestas);
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("calificarexamenpostest")
    public String calificarExamenPosTest() throws Exception{
        
        throw new Exception();
    }
}
