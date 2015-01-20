package javasensei.db.managments;

import javasensei.estudiante.ModeloEstudiante;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

/**
 *
 * @author Rock
 */
public class RecomendacionesManager {
    public String recomendacionEjercicio(ModeloEstudiante estudiante){
        String resultado = "{}";
        
        //TODO: ES UN DUMMY, CAMBIARLO
        BasicDBList list = new BasicDBList();
        
        for(int index = 1; index<11;index++){
            list.add(
                    new BasicDBObject().
                            append("id", index).
                            append("url", "http://www.google.com").
                            append("titulo", "Titulo "+index)
            );
        }
        
        return list.toString();
    }
}
