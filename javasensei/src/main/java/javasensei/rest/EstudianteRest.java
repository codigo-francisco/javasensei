package javasensei.rest;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import javasensei.db.managments.EstudiantesManager;
import javasensei.db.managments.RankingManager;
import javasensei.estudiante.ModeloEstudiante;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
    @Path("studentExists")
    public String studentExists(@QueryParam("idFacebook") String idFacebook){
        ModeloEstudiante estudiante = new ModeloEstudiante();
        estudiante.setIdFacebook(idFacebook);
        
        EstudiantesManager estudiantes = new EstudiantesManager(estudiante);
        return estudiantes.existeEstudiante();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getorcreatestudent")
    public String getOrCreateEstudiante(@QueryParam("datos") String datos){
        
        ModeloEstudiante estudiante = new ModeloEstudiante();
        BasicDBObject object = BasicDBObject.parse(datos);
        boolean nuevo = object.getBoolean("nuevo");
        estudiante.setIdFacebook(object.getString("idFacebook"));
        estudiante.setToken(object.getString("token"));
        estudiante.setNombreFacebook(object.getString("nombreFacebook"));
        estudiante.setFotografia(object.getString("fotografia"));
        
        if (nuevo){
            estudiante.setSexo(object.getString("sexo"));
            estudiante.setEdad(Integer.parseInt(object.getString("edad")));
            estudiante.setNombre(object.getString("nombre"));
        }
        
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
        
        DBObject json = new BasicDBObject();
        
        Double value = manager.getAbilityGlobal();
        
        json.put("result", manager.finalizarEjercicio(idEjercicio,valor));
        json.put("habilidadGlobal", value);
        
        
        manager.saveAbilityGlobal(value);
        
        return json.toString();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registrarSalida")
    public String registrarSalida(@QueryParam("idAlumno") Long idAlumno){
        ModeloEstudiante estudiante = new ModeloEstudiante();
        estudiante.setId(idAlumno);
        
        EstudiantesManager manager = new EstudiantesManager(estudiante);
        
        return manager.registrarVisita("salida");
    }
    
    @POST
    @Path("modificarPropiedad")
    public String modificarPropiedad(@FormParam("json") String json){
        new EstudiantesManager().modificarPropiedades(json);
        return "{result:true}";
    }    
}
