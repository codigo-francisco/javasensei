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
import java.util.stream.IntStream;
import javasensei.db.collections.EjerciciosCollection;
import javasensei.db.collections.RankingEjerciciosCollection;
import javasensei.db.collections.RankingRecursosCollection;
import javasensei.db.collections.RecursosCollection;
import javasensei.estudiante.ModeloEstudiante;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.AbstractRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

/**
 *
 * @author Rock
 */
public class RankingManager {

    private DBCollection ejercicios = new EjerciciosCollection().getEjerciciosCollection();
    private DBCollection rankingEjercicios = new RankingEjerciciosCollection().getRankingEjerciciosCollection();

    private DBCollection recursos = new RecursosCollection().getRecursosCollection();
    private DBCollection rankingRecursos = new RankingRecursosCollection().getRankingRecursos();

    private ModeloEstudiante estudiante;

    protected static GenericUserBasedRecommender recommenderEjercicios;
    //protected static RandomRecommender randomRecommender; //No funciona el recommender no se porque FGH
    protected static GenericUserBasedRecommender recommenderRecursos;

    public RankingManager(ModeloEstudiante estudiante) {
        this.estudiante = estudiante;
        if (recommenderEjercicios == null) {
            updateDataModelEjercicios();
        }
        if (recommenderRecursos == null) {
            updateDataModelRecursos();
        }
    }

    public String getRecommenderResources(int cantidad, boolean random) {
        return getRecommenders(rankingRecursos, recommenderRecursos, cantidad, random);
    }

    private int getIdLeccion(int idEjercicio) {
        return new Double(ejercicios.findOne(
                QueryBuilder.start("id").is(idEjercicio).get(),
                QueryBuilder.start("_id").is(0)
                .put("idLeccion").is(1)
                .get()
        ).get("idLeccion").toString()).intValue();
    }

    public String getRecommenderResources(int cantidad, int idEjercicio) {
        String result = "[]";

        try {
            int idLeccionPrincipal = getIdLeccion(idEjercicio);

            List<DBObject> listaRecursos = recursos.find(
                    QueryBuilder.start("id").in(
                            getRecommendersItemsIDAsArray(recommenderRecursos, cantidad))
                    .put("idLeccion").is(idLeccionPrincipal)
                    .get()
            ).toArray();

            listaRecursos.replaceAll((DBObject object) -> {
                object.put("ranking", rankingRecursos.findOne(
                        QueryBuilder.start("idRecurso")
                        .is(object.get("id"))
                        .get(),
                        QueryBuilder.start("_id").is(0)
                        .put("ranking").is(1)
                        .get()
                ).get("ranking"));

                return object;
            });

            result = listaRecursos.toString();
        } catch (TasteException ex) {
            Logger.getLogger(RankingManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public String getRecommendersExercises(int cantidad, boolean random) {
        return getRecommenders(rankingEjercicios, recommenderEjercicios, cantidad, random);
    }

    protected Integer[] getRecommendersItemsIDAsArray(AbstractRecommender recommender, int cantidad) throws TasteException{
        List<Integer> listId = new ArrayList<>();
        
        for(RecommendedItem item : getRecommendersItems(recommender, cantidad)){
            listId.add(new Long(item.getItemID()).intValue());
        }
        
        return listId.toArray(new Integer[listId.size()]);
    }
    
    protected List<RecommendedItem> getRecommendersItems(AbstractRecommender recommender, int cantidad) throws TasteException {
        return recommender.recommend(estudiante.getId(), cantidad);
    }

    protected String getRecommenders(DBCollection collection, AbstractRecommender recommender, int cantidad, boolean random) {
        String result = "{}";

        List<RecommendedItem> recommenders;
        try {
            List<Long> array = new ArrayList();

            recommenders = getRecommendersItems(recommender, cantidad);//recommenderEjercicios.recommend(estudiante.getId(), cantidad); //5 Recomendaciones

            //Se agrega un item aleatorio...
            if (random && recommenders.size() < 1) { //RandomRecommender no funciona....

                double number = collection.count();

                DBObject object = collection.find().limit(1).skip((int) Math.floor(Math.random() * number)).next();

                array.add(
                        Math.round(Double.parseDouble(object.get("idEjercicio").toString()))
                );
            }

            //Las recomendaciones se transforman en json array
            for (RecommendedItem item : recommenders) {
                array.add(item.getItemID());
            }

            //Se crea un json array con los id obtenidos de los ejercicios
            result = ejercicios.find(
                    QueryBuilder.start("id").in(array).get()
            ).toArray().toString();
        } catch (TasteException ex) {
            Logger.getLogger(RankingManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public void updateDataModelEjercicios() {
        try {
            //Creamos el archivo csv sobre el que vamos a escribir
            File csv = File.createTempFile("dataset", ".csv");
            PrintWriter writer = new PrintWriter(csv);

            //Obtenemos todos los ranking actuales y los almacenamos en el archivo csv
            DBCursor cursor = rankingEjercicios.find();

            if (cursor.count() > 0) {

                while (cursor.hasNext()) {
                    DBObject object = cursor.next();
                    long idAlumno = estudiante.getId();
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
                ThresholdUserNeighborhood neigh = new ThresholdUserNeighborhood(0.1, correlation, dataModel);
                recommenderEjercicios = new GenericUserBasedRecommender(dataModel, neigh, correlation);
                //randomRecommender = new RandomRecommender(dataModel);

                csv.deleteOnExit();
            }
        } catch (Exception ex) {
            Logger.getLogger(RankingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateDataModelRecursos() {
        try {
            //Creamos el archivo csv sobre el que vamos a escribir
            File csv = File.createTempFile("dataset", ".csv");
            PrintWriter writer = new PrintWriter(csv);

            //Obtenemos todos los ranking actuales y los almacenamos en el archivo csv
            DBCursor cursor = rankingRecursos.find();

            if (cursor.count() > 0) {

                while (cursor.hasNext()) {
                    DBObject object = cursor.next();
                    long idAlumno = estudiante.getId();
                    int idRecurso = (int) Double.parseDouble(object.get("idRecurso").toString());

                    String cadena = String.format("%s,%s,%s",
                            idAlumno,
                            idRecurso,
                            object.get("ranking"));
                    writer.println(cadena);
                    System.out.println(cadena);
                }

                writer.close();

                //El archivo es pasado al dataModel
                DataModel dataModel = new FileDataModel(csv);
                //Creamos la correlacion de pearson
                PearsonCorrelationSimilarity correlation = new PearsonCorrelationSimilarity(dataModel);
                ThresholdUserNeighborhood neigh = new ThresholdUserNeighborhood(0.1, correlation, dataModel);
                recommenderRecursos = new GenericUserBasedRecommender(dataModel, neigh, correlation);

                csv.deleteOnExit();
            }
        } catch (IOException | TasteException ex) {
            Logger.getLogger(RankingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean colocarRankingDefault() {
        boolean result = false;

        try {
            QueryBuilder query = QueryBuilder.start("id")
                    .notIn(rankingEjercicios.distinct("idEjercicio",
                                    QueryBuilder.start("idAlumno").
                                    is(estudiante.getId()).get()
                            ));

            //Obtenemos los id de ejercicios que no estan rankeados por el alumno
            //Se colocara un valor 2 de default para dicho ranking, de acuerdo a la escala de LIKERT
            List<DBObject> objects = ejercicios.find(query.get()).toArray();

            if (!objects.isEmpty()) {
                List<DBObject> listObjects = new ArrayList<>();

                for (DBObject object : objects) {
                    listObjects.add(
                            BasicDBObjectBuilder.start()
                            .add("idEjercicio", object.get("id"))
                            .add("idAlumno", estudiante.getId())
                            .add("ranking", 2) //2 De acuerdo a la escala
                            .get()
                    );
                }

                rankingEjercicios.insert(listObjects);

                updateDataModelEjercicios();
            }

            //Mismo proceso para ranking de recursos
            QueryBuilder queryRecursos = QueryBuilder.start("id")
                    .notIn(rankingRecursos.distinct("idRecurso",
                                    QueryBuilder.start("idAlumno")
                                    .is(estudiante.getId()).get()));

            List<DBObject> idRecursos = recursos.find(queryRecursos.get()).toArray();

            if (!idRecursos.isEmpty()) {
                List<DBObject> listRankingRecursosSave = new ArrayList<>();

                for (DBObject object : idRecursos) {
                    listRankingRecursosSave.add(
                            BasicDBObjectBuilder.start()
                            .add("idRecurso", object.get("id"))
                            .add("idAlumno", estudiante.getId())
                            .add("ranking", 2) //2 De acuerdo a la escala
                            .get()
                    );
                }

                rankingRecursos.insert(listRankingRecursosSave);

                updateDataModelRecursos();
            }

            result = true;

        } catch (Exception ex) {
            System.err.println(ex);
        }

        return result;
    }
}
