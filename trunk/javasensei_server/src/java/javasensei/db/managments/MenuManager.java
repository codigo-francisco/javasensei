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

    private DBCollection leccionesCollection = Connection.getDb().getCollection("lecciones");
    private DBCollection ejerciciosCollection = Connection.getDb().getCollection("ejercicios");
    private DBCollection alumnosCollections = Connection.getDb().getCollection("alumnos");

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
