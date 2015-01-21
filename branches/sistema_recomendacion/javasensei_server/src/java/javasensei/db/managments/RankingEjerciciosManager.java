/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.db.managments;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.collections.EjerciciosCollection;
import javasensei.db.collections.RankingEjerciciosCollection;
import javasensei.estudiante.ModeloEstudiante;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.RandomRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

/**
 *
 * @author Rock
 */
public class RankingEjerciciosManager {

    private RankingEjerciciosCollection rankingCollection = new RankingEjerciciosCollection();
    private EjerciciosCollection ejerciciosCollection = new EjerciciosCollection();
    private ModeloEstudiante estudiante;

    protected static GenericUserBasedRecommender recommender;
    protected static RandomRecommender randomRecommender;

    public RankingEjerciciosManager(ModeloEstudiante estudiante) {
        this.estudiante = estudiante;
        if (recommender == null) {
            updateDataModel();
        }
    }

    public String getRecommenders() {
        String result = "{}";

        List<RecommendedItem> recommenders;
        try {
            List<Long> array = new ArrayList();

            recommenders = recommender.recommend(estudiante.getIdLong(), 5); //5 Recomendaciones

            //Se agrega un item aleatorio...
            if (recommenders.size() < 1) { //RandomRecommender no funciona....
                DBCollection collection = rankingCollection.getRankingEjerciciosCollection();
                
                double number = collection.count();
                
                DBObject object = collection.find().limit(1).skip((int)Math.floor(Math.random()*number)).next();
                
                array.add(
                        Math.round(Double.parseDouble(object.get("idEjercicio").toString()))
                );
            }

            //Las recomendaciones se transforman en json array
            for (RecommendedItem item : recommenders) {
                array.add(item.getItemID());
            }

            //Se crea un json array con los id obtenidos de los ejercicios
            result = ejerciciosCollection.getEjerciciosCollection().find(
                    QueryBuilder.start("id").in(array).get(),
                    QueryBuilder.start("_id").is(0)
                    .put("titulo").is(1)
                    .put("url").is(1)
                    .get()
            ).toArray().toString();
        } catch (TasteException ex) {
            Logger.getLogger(RankingEjerciciosManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public void updateDataModel() {
        try {
            //Creamos el archivo csv sobre el que vamos a escribir
            File csv = File.createTempFile("dataset", ".csv");
            PrintWriter writer = new PrintWriter(csv);

            //Obtenemos todos los ranking actuales y los almacenamos en el archivo csv
            DBCursor cursor = rankingCollection.getRankingEjerciciosCollection().find();

            if (cursor.count() > 0) {

                while (cursor.hasNext()) {
                    DBObject object = cursor.next();
                    long idAlumno = estudiante.getIdLong();
                    int idEjercicio = (int) Double.parseDouble(object.get("idEjercicio").toString());

                    String cadena = String.format("%s,%s,%s",
                            idAlumno,
                            idEjercicio,
                            object.get("ranking"));
                    writer.println(cadena);
                    System.out.println(cadena);
                }

                writer.close();

                //El archivo es pasado al dataModel
                DataModel dataModel = new FileDataModel(csv);
                //Creamos la correlacion de pearson
                PearsonCorrelationSimilarity correlation = new PearsonCorrelationSimilarity(dataModel);
                ThresholdUserNeighborhood neigh = new ThresholdUserNeighborhood(3, correlation, dataModel);
                recommender = new GenericUserBasedRecommender(dataModel, neigh, correlation);
                randomRecommender = new RandomRecommender(dataModel);

                csv.deleteOnExit();
            }
        } catch (IOException | TasteException ex) {
            Logger.getLogger(RankingEjerciciosManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean colocarRankingDefault() {
        boolean result = false;

        try {
            DBCollection collection = ejerciciosCollection.getEjerciciosCollection();
            DBCollection ranking = rankingCollection.getRankingEjerciciosCollection();

            QueryBuilder query = QueryBuilder.start("id")
                    .notIn(ranking.distinct("idEjercicio",
                                    QueryBuilder.start("idAlumno").
                                    is(estudiante.getIdLong()).get()
                            ));

            //Obtenemos los id de ejercicios que no estan rankeados por el alumno
            //Se colocara un valor 2 de default para dicho ranking, de acuerdo a la escala de LIKERT
            List<DBObject> objects = collection.find(query.get()).toArray();
            List<DBObject> listObjects = new ArrayList<>();

            if (!objects.isEmpty()) {
                for (DBObject object : objects) {
                    listObjects.add(
                            BasicDBObjectBuilder.start()
                            .add("idEjercicio", object.get("id"))
                            .add("idAlumno", estudiante.getIdLong())
                            .add("ranking", 2) //2 De acuerdo a la escala
                            .get()
                    );
                }

                ranking.insert(listObjects);

                updateDataModel();
            }

            result = true;

        } catch (Exception ex) {
            System.err.println(ex);
        }

        return result;
    }
}
