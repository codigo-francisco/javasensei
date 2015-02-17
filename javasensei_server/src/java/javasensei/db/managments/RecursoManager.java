package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import javasensei.db.connection.Connection;

/**
 *
 * @author Rock
 */
public class RecursoManager {

    DBCollection recursosCollection = Connection.getDb().getCollection("recursos");
    DBCollection leccionesCollection = Connection.getDb().getCollection("lecciones");
    DBCollection rankingCollection = Connection.getDb().getCollection("ranking_recursos");

    public double getRanking(int idRecurso, Long idAlumno){
        return new Double(recursosCollection.findOne(
                new BasicDBObject("idAlumno", idAlumno)
                .append("idRecurso", idRecurso)
            ).get("ranking").toString()
        );
    }
    
    public Boolean setRanking(int idRecurso, Long idAlumno, int ranking) {
        boolean result = false;

        try {
            rankingCollection.update(
                    QueryBuilder.start("idRecurso").is(idRecurso)
                    .put("idAlumno").is(idAlumno)
                    .get(), QueryBuilder.start("$set").is(
                            QueryBuilder.start("ranking").is(ranking).get()
                    ).get()
            );
            
            result = true;
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return result;
    }

    public String obtenerRecursosPorCategoria(long idUsuario) {

        List<DBObject> lista = new ArrayList<>();

        for (DBObject object : leccionesCollection.find().toArray()) {

            BasicDBObjectBuilder build = BasicDBObjectBuilder.start();

            build.add("nombre", object.get("nombre"));

            List<DBObject> recursos = recursosCollection.find(
                    QueryBuilder.start("idLeccion").is(object.get("id")).get()
            ).toArray();

            List<DBObject> recursos2 = new ArrayList<>();

            for (DBObject recurso : recursos) {

                DBObject ranking = rankingCollection.findOne(
                        QueryBuilder.start("idRecurso").is(recurso.get("id"))
                        .put("idAlumno").is(idUsuario)
                        .get());

                //Obtener ranking y colocarlo en el objeto
                recurso.put("ranking", ranking.get("ranking"));

                recursos2.add(recurso);

            }

            build.add("recursos", recursos2);

            lista.add(build.get());
        }

        System.out.println(lista.toString());

        return lista.toString();
    }
}
