/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.tutor.rest;

import java.time.ZonedDateTime;
import java.util.Date;
import javasensei.db.managments.ChatManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Rock
 */
@Path("chat")
public class Chat {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Chat
     */
    public Chat() {
    }

    @GET
    @Path("agregarmensaje")
    @Produces(MediaType.TEXT_HTML)
    public String agregarMensaje(@QueryParam("message") String message, 
            @QueryParam("nombreUsuario") String nombreUsuario, 
            @QueryParam("idUsuario") String idUsuario,
            @QueryParam("fecha") String fecha){
        return new ChatManager().agregarMensaje(message, nombreUsuario, idUsuario, 
                Date.from(ZonedDateTime.parse(fecha).toInstant()))
                .toString();
    }
}
