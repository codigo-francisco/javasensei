package javasensei.db.managments;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javasensei.db.Connection;

/**
 *
 * @author PosgradoMCC
 */
public class MenuManager {

    private DBCollection leccionesCollection = Connection.getCollection().get(CollectionsDB.LECCIONES);
    private DBCollection alumnosCollections = Connection.getCollection().get(CollectionsDB.ALUMNOS);

    public String getDataGraphics(Long idAlumno) {        
        List<String> lecciones = new ArrayList<>();
        List<Long> listaEjercicios = new ArrayList<>();

        long maximo = 0;

        List<DBObject> ejercicios = Arrays.stream(((BasicDBList) alumnosCollections.findOne(
                new BasicDBObject("id", idAlumno)
        ).get("ejercicios")).toArray()).map((ejercicioObj) -> (DBObject) ejercicioObj).collect(Collectors.toList());

        DBCursor cursor = leccionesCollection.find(QueryBuilder.start().get(),
                QueryBuilder.start("nombre").is(1)
                .put("id").is(1)
                .put("_id").is(0)
                .get()
        );

        while (cursor.hasNext()) {
            DBObject object = cursor.next();

            lecciones.add(object.get("nombre").toString());

            Integer id = new Double(object.get("id").toString()).intValue();

            List<DBObject> ejerciciosLeccion = ejercicios.stream().filter((ejercicio)
                    -> Integer.parseInt(ejercicio.get("idLeccion").toString()) == id
            ).collect(Collectors.toList());
            
            long cantidadTotalEjercicios = ejerciciosLeccion.stream().count();
            if (cantidadTotalEjercicios>maximo)
                maximo = cantidadTotalEjercicios;
            
            long cantidadEjercicios = ejerciciosLeccion.stream().filter(ejercicio->
                    Integer.parseInt(ejercicio.get("terminado").toString()) == 1
            ).count();
            
            listaEjercicios.add(cantidadEjercicios);
        }

        DBObject resultado = BasicDBObjectBuilder.start("maximo", maximo)
                .add("lecciones", lecciones)
                .add("listaEjercicios", listaEjercicios)
                .get();
        
        return resultado.toString();
    }

    public String getCursoMenu(Long idAlumno) {
        BasicDBList list = new BasicDBList();

        List<DBObject> ejercicios = Arrays.stream(((BasicDBList) alumnosCollections.findOne(
                new BasicDBObject("id", idAlumno)
        ).get("ejercicios")).toArray()).map((ejercicioObj) -> (DBObject) ejercicioObj).collect(Collectors.toList());

        DBCursor cursor = leccionesCollection.find(QueryBuilder.start().get(),
                QueryBuilder.start("nombre").is(1)
                .put("id").is(1)
                .put("_id").is(0)
                .get()
        );

        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            object.put("nombre", object.get("nombre"));

            Integer id = new Double(object.get("id").toString()).intValue();

            object.put("id", id);

            object.put("ejercicios", ejercicios.stream().filter((ejercicio)
                    -> Integer.parseInt(ejercicio.get("idLeccion").toString()) == id
            ).collect(Collectors.toList()));

            list.add(object);
        }

        return list.toString();
    }
}
