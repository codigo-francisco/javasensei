/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.rest;

import com.mongodb.BasicDBList;
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
            @FormParam("idUsuario") Long idUsuario,
            @FormParam("json") String jsonRespuestas){
        BasicDBList respuestas = (BasicDBList)com.mongodb.util.JSON.parse(jsonRespuestas);
        System.out.println(respuestas.toString());
                
        /*for(int i = 0; i < respuestas.size(); i++)
		System.out.println((respuestas.get(i)));
        return respuestas.toString();*/
        return "hola";
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("calificarexamenpostest")
    public String calificarExamenPosTest() throws Exception{
        
        throw new Exception();
    }
}
