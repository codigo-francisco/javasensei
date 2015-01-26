package javasensei.db.managments;

import com.mongodb.DBCollection;
import com.mongodb.QueryBuilder;
import com.mongodb.DBObject;
import javasensei.db.collections.RecursosCollection;
import javasensei.db.collections.LeccionesCollection;
import javasensei.db.collections.RankingEjerciciosCollection;
import com.mongodb.BasicDBObjectBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rock
 */
public class RecursoManager {
    
    private DBCollection recursosCollection = new RecursosCollection().getRecursosCollection();
    private DBCollection leccionesCollection = new LeccionesCollection().getLeccionesCollection();
    private DBCollection rankingCollection = new RankingEjerciciosCollection().getRankingEjerciciosCollection();
    
    public String obtenerRecursosPorCategoria(long idUsuario){        
        List<DBObject> lista = new ArrayList<>();
        
        for (DBObject object : leccionesCollection.find().toArray()){
            
            BasicDBObjectBuilder build = BasicDBObjectBuilder.start();
            
            build.add("nombre", object.get("nombre"));
            
            List<DBObject> recursos = recursosCollection.find(
                    QueryBuilder.start("idLeccion").is(object.get("id")).get()
            ).toArray();
            
            List<DBObject> recursos2 = new ArrayList<DBObject>();
            
            for (DBObject recurso : recursos){
                
                //Obtener ranking y colocarlo en el objeto
                recurso.put("ranking", rankingCollection.findOne(
                        QueryBuilder.start("idEjercicio").is(object.get("id"))
                                .put("idAlumno").is(idUsuario)
                                .get()
                ));
                
                recursos2.add(recurso);
                
            }
            
            build.add("recursos", recursos2);
            
            lista.add(build.get());
        }
        
        System.out.println(lista.toString());
        
        return lista.toString();
    }
}
