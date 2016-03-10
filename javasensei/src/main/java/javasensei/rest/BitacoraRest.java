/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.rest;

import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javasensei.db.managments.BitacoraEjerciciosManager;
import javasensei.db.managments.BitacoraFotografia;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import org.bson.types.ObjectId;

/**
 * REST Web Service
 *
 * @author Rock
 */
@Path("bitacora")
public class BitacoraRest {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of BitacoraRest
     */
    public BitacoraRest() {
    }

    /**
     * Retrieves representation of an instance of javasensei.tutor.rest.BitacoraRest
     * @param logBitacoras
     * @return an instance of java.lang.String
     */
    @Path("guardarbitacoras")
    @POST
    public String guardarBitacoras(@FormParam("logBitacoras") String logBitacoras) {
        BitacoraEjerciciosManager manager = new BitacoraEjerciciosManager();
        
        return manager.guardarBitacoras(logBitacoras);
    }
    
    @Path("categorizarfotografiausuario")
    @GET
    public String categorizarFotografiaUsuario(
            @QueryParam("idFotografia") String idFotografia,
            @QueryParam("idUsuario") String idUsuario,
            @QueryParam("emocion") String emocion){
        
        BitacoraFotografia bitacoraFotografia = new BitacoraFotografia();
        
        ObjectId idFoto = new ObjectId(idFotografia);
        bitacoraFotografia.categorizarFotografiaUsuario(idFoto, idUsuario, emocion);
        
        return "{result:true}";
    }
    
    @Path("obtenerbitacoracategorizacion")
    @POST
    public String obtenerBitacoraCategorizacion(@FormParam("idUsuario") Long idUsuario) {
        //Ultimas fotografias de las seccion activa
        return new BitacoraFotografia().obtenerFotografiasCategorizadas(idUsuario);
    }
    
    @Path("bitacorafotografia")
    @Produces("application/json")
    @POST
    public String guardarBitacoraFotografia(@FormParam("datos") String datos){
        new BitacoraFotografia().guardarBitacoraFotografia(datos);
        return "{result:true}";
    }
    
    @Path("reiniciarsesionfotografia")
    @GET
    public String reiniciarSesionFotografia(@QueryParam("id") Long idAlumno){
        new BitacoraFotografia().reiniciarSesion(idAlumno);
        return "{result:true}";
    }
    
    @Path("obtenerbitacora")
    @Produces("application/json")
    @POST
    public String obtenerBitacora(@FormParam("idAlumno")String idAlumno,
                                  @FormParam("ejercicioId")String ejercicioId,
                                  @FormParam("fechaInicial")String fechaIncial,
                                  @FormParam("fechaFinal")String fechaFinal,
                                  @FormParam("sesionId")String sesionId,
                                  @FormParam("emocionInicial")String emocionInicial,
                                  @FormParam("emocionFinal")String emocionFinal){
        
        
        BitacoraEjerciciosManager manager = new BitacoraEjerciciosManager();
        
        return manager.obtenerBitacora(idAlumno, ejercicioId, fechaIncial, fechaFinal, sesionId, emocionInicial, emocionFinal);
                
    }
}
