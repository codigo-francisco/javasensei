package javasensei.tutor.rest;

import javasensei.db.managments.EstudiantesManager;
import javasensei.db.managments.RankingEjerciciosManager;
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
    public String getOrCreateEstudiante(@QueryParam("id") String id, @QueryParam("token") String token){
        
        ModeloEstudiante estudiante = new ModeloEstudiante();
        estudiante.setId(id);
        estudiante.setToken(token);
        
        EstudiantesManager estudiantes = new EstudiantesManager();
        String result = estudiantes.insertOrCreateStudent(estudiante);
        //Se pasa a colocar el ranking
        RankingEjerciciosManager ranking = new RankingEjerciciosManager();
        ranking.colocarRankingDefault(estudiante);
        
        return "jsonpCallback("+result+")"; //JSON de respuesta
    }
}
