package javasensei.db.managments;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import javasensei.db.collections.EjerciciosCollection;
import javasensei.db.collections.TemasCollection;

/**
 *
 * @author PosgradoMCC
 */
public class MenuManager {

    private DBCollection temasCollection = new TemasCollection().getTemasCollection();
    private DBCollection ejerciciosCollection = new EjerciciosCollection().getEjerciciosCollection();

    public String getCursoMenu() {
        BasicDBList list = new BasicDBList();

        DBCursor cursor = temasCollection.find(QueryBuilder.start().get(),
                QueryBuilder.start("nombre").is(1)
                .put("id").is(1)
                .put("_id").is(1)
                .get()
        );

        while (cursor.hasNext()) {
            DBObject object = cursor.curr();
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
        }

        return list.toString();
    }
}
