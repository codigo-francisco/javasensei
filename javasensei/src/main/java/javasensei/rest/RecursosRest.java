package javasensei.rest;

import com.google.gson.JsonObject;
import javasensei.db.managments.EjerciciosManager;
import javasensei.db.managments.RankingManager;
import javasensei.db.managments.RecursoManager;
import javasensei.estudiante.ModeloEstudiante;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
        
        RankingManager ranking = new RankingManager(estudiante);
        
        return ranking.getRecommenderResources(3, idEjercicio);
    }
    
    @POST
    @Path("todos")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecursos(@FormParam("usuario") String usuario){
        ModeloEstudiante estudiante = new ModeloEstudiante(usuario);
        
        RecursoManager recursos = new RecursoManager();
        
        return recursos.obtenerRecursosPorCategoria(estudiante.getId());
    }
    
    @GET
    @Path("getrankingrecursos")
    public String getRankingRecursos(@QueryParam("idRecurso") int idRecurso, @QueryParam("idAlumno") Long idAlumno){
        RecursoManager manager = new RecursoManager();
        
        Double ranking = manager.getRanking(idRecurso, idAlumno);
        
        JsonObject json = new JsonObject();
        json.addProperty("ranking", ranking);
        
        return json.toString();
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
    
    @GET
    @Path("getrankingejercicio")
    public String getRankingEjercicio(@QueryParam("idEjercicio") Integer idEjercicio, @QueryParam("idAlumno") Long idAlumno){
        EjerciciosManager manager = new EjerciciosManager();
        
        Integer ranking = manager.getRankingEjercicio(idEjercicio, idAlumno);
        
        JsonObject json = new JsonObject();
        json.addProperty("id", idEjercicio);
        json.addProperty("ranking", ranking);
        
        return json.toString();
    }
    
    @GET
    @Path("setrankingejercicio")
    public String setRankingEjercicio(@QueryParam("idEjercicio") Integer idEjercicio, @QueryParam("idAlumno") Long idAlumno, @QueryParam("ranking") Integer ranking){
        EjerciciosManager manager = new EjerciciosManager();
        
        Boolean result = manager.setRankingEjercicio(idEjercicio, idAlumno, ranking);
        
        JsonObject json = new JsonObject();
        json.addProperty("id", idEjercicio);
        json.addProperty("ranking", ranking);
        json.addProperty("result", result);
        
        return json.toString();
    }  
    @GET
    @Path("getJSONList")
    public String getJSONList(@QueryParam("coleccion") int coleccion){
        RecursoManager manager = new RecursoManager();
        return manager.getJSONList(coleccion);
    }
    @GET
    @Path("guardar")
    public String guardar(@QueryParam("coleccion") int coleccion, @QueryParam("json") String json, @QueryParam("oldJson") String oldJson){
        RecursoManager manager = new RecursoManager(); 
        return manager.guardar(coleccion, json, oldJson);
    }
}