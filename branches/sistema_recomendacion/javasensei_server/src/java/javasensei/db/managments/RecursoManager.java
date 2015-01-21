package javasensei.db.managments;

import com.mongodb.DBCollection;
import com.mongodb.QueryBuilder;
import com.mongodb.DBObject;
import javasensei.db.collections.RecursosCollection;
import javasensei.db.collections.LeccionesCollection;
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
    
    public String obtenerRecursosPorCategoria(){
        //db.lecciones.find().map(function(leccion){return {nombre:leccion.nombre,
        //recursos:db.recursos.find({idLeccion:leccion.id},{titulo:1,url:1,_id:0}).toArray()};})
        
        List<DBObject> lista = new ArrayList<>();
        
        for (DBObject object : leccionesCollection.find().toArray()){
            
            BasicDBObjectBuilder build = BasicDBObjectBuilder.start();
            
            build.add("nombre", object.get("nombre"));
            
            build.add("recursos", recursosCollection.find(
                    QueryBuilder.start("idLeccion").is(object.get("id")).get(),
                    QueryBuilder.start("titulo").is(1)
                            .put("url").is(1)
                            .put("_id").is(0)
                            .get()
            ).toArray());
            
            lista.add(build.get());
            
            
        }
        
        return lista.toString();
    }
}
