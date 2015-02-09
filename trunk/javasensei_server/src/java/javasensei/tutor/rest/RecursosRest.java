package javasensei.tutor.rest;

import com.google.gson.JsonObject;
import javasensei.db.managments.RankingEjerciciosManager;
import javasensei.db.managments.RecursoManager;
import javasensei.estudiante.ModeloEstudiante;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rock
 */
@Path("recursos")
public class RecursosRest {
    
    
    @GET
    @Path("recomendaciones")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecomendaciones(@QueryParam ("idEjercicio") int idEjercicio, @QueryParam("idAlumno") Long idAlumno){
        ModeloEstudiante estudiante = new ModeloEstudiante();
        estudiante.setId(idAlumno);
        
        RankingEjerciciosManager ranking = new RankingEjerciciosManager(estudiante);
        
        return ranking.getRecommenderResources(3, idEjercicio);
    }
    
    @GET
    @Path("todos/{usuario}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecursos(@PathParam("usuario") String usuario){
        ModeloEstudiante estudiante = new ModeloEstudiante(usuario);
        
        RecursoManager recursos = new RecursoManager();
        
        return recursos.obtenerRecursosPorCategoria(estudiante.getId());
    }
    
    @GET
    @Path("setranking")
    public String setRanking(@QueryParam("idRecurso") int idRecurso, @QueryParam("idAlumno") Long idAlumno, @QueryParam("ranking") int ranking){
        RecursoManager manager = new RecursoManager();
        
        Boolean result = manager.setRanking(idRecurso, idAlumno, ranking);
        
        JsonObject json = new JsonObject();
        json.addProperty("id", idRecurso);
        json.addProperty("ranking", ranking);
        json.addProperty("result", result);
        
        return json.toString();
    }
}
