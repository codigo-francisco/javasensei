/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.rest;

import javasensei.ia.recognitionemotion.RecognitionEmotionalFace;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Francisco
 */
@Path("emociones")
public class EmocionesRest {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of EmocionesRest
     */
    public EmocionesRest() {
    }


    @POST
    public String putJson(@FormParam("fotografia") String fotografia) {
        //Suponemos que content es la fotografía
        return new RecognitionEmotionalFace().getEmocionSinProcesar(fotografia);
    }
}
