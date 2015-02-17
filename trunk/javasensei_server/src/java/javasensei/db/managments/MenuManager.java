package javasensei.db.managments;

import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import javasensei.db.connection.Connection;

/**
 *
 * @author PosgradoMCC
 */
public class MenuManager {

    private DBCollection leccionesCollection = Connection.getDb().getCollection("lecciones");
    private DBCollection ejerciciosCollection = Connection.getDb().getCollection("ejercicios");

    public String getCursoMenu() {
        BasicDBList list = new BasicDBList();

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

            object.put("ejercicios",
                    ejerciciosCollection.find(QueryBuilder.start("idLeccion").is(id).get(),
                            QueryBuilder.start("titulo").is(1)
                            .put("id").is(1)
                            .put("url").is(1)
                            .put("_id").is(0)
                            .get()
                    ).toArray()
            );
            
            list.add(object);
        }

        return list.toString();
    }
}
