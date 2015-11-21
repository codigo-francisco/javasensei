/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.tutor.rest;

import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javasensei.db.managments.BitacoraEjerciciosManager;
import javax.ws.rs.POST;

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
