package javasensei.tutor.rest;

import com.google.gson.JsonObject;
import javasensei.db.managments.EstudiantesManager;
import javasensei.db.managments.RankingManager;
import javasensei.estudiante.ModeloEstudiante;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Rock
 */
@Path("estudiantes")
public class EstudianteRest {
    @Context
    private UriInfo context;
    
    public EstudianteRest(){
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getorcreatestudent")
    public String getOrCreateEstudiante(
            @QueryParam("idFacebook") String idFacebook, 
            @QueryParam("token") String token,
            @QueryParam("nombre") String nombre,
            @QueryParam("fotografia") String fotografia){
        
        ModeloEstudiante estudiante = new ModeloEstudiante();
        estudiante.setIdFacebook(idFacebook);
        estudiante.setToken(token);
        estudiante.setNombre(nombre);
        estudiante.setFotografia(fotografia);
        
        EstudiantesManager estudiantes = new EstudiantesManager(estudiante);
        String result = estudiantes.insertOrCreateStudent();
        
        estudiantes.createOrUpdateStudentModel();
        
        //Se pasa a colocar el ranking
        RankingManager ranking = new RankingManager(estudiante);
        ranking.colocarRankingDefault();
        
        return result; //JSON de respuesta
    }
    
    @GET
    @Path("activarEmociones")
    public String activarEmociones(@QueryParam("idAlumno") Long idAlumno, @QueryParam("activarEmociones") Boolean activarEmociones){
        ModeloEstudiante modeloEstudiante = new ModeloEstudiante();
        modeloEstudiante.setId(idAlumno);
        modeloEstudiante.setActivarEmociones(activarEmociones);
        
        return new EstudiantesManager(modeloEstudiante).saveActivarEmociones().toString();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("finalizarEjercicio")
    public String finalizarEjercicio(@QueryParam("idAlumno") Long idAlumno, @QueryParam("idEjercicio") Integer idEjercicio, @QueryParam ("valor") double valor){
        ModeloEstudiante estudiante = new ModeloEstudiante();
        estudiante.setId(idAlumno);
        
        EstudiantesManager manager = new EstudiantesManager(estudiante);
        
        JsonObject json = new JsonObject();
        
        Double value = manager.getAbilityGlobal();
        
        json.addProperty("result", manager.finalizarEjercicio(idEjercicio,valor));
        json.addProperty("habilidadGlobal", value);
        
        
        manager.saveAbilityGlobal(value);
        
        return json.toString();
    }
}
