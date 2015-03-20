package javasensei.db.managments;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import javasensei.db.Connection;

/**
 *
 * @author Rock
 */
public class EjerciciosManager {

    private final DBCollection rankingEjerciciosCollection = Connection.getCollection().get(CollectionsDB.RANKING_EJERCICIOS);

    public Integer getRankingEjercicio(Integer idEjercicio, Long idAlumno) {
        Integer ranking = 2; //Default

        DBObject object = rankingEjerciciosCollection.findOne(
                QueryBuilder.start("idEjercicio").is(idEjercicio)
                .put("idAlumno").is(idAlumno)
                .get()
        );

        if (object != null) {
            ranking = new Double(object.get("ranking").toString()).intValue();
        }

        return ranking;
    }

    public Boolean setRankingEjercicio(Integer idEjercicio, Long idAlumno, Integer ranking) {
        boolean result = false;

        try {
            rankingEjerciciosCollection.update(
                    QueryBuilder.start("idEjercicio").is(idEjercicio)
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
}
