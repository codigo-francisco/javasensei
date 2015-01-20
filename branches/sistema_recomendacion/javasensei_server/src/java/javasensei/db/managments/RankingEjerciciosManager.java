/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.db.managments;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasensei.db.collections.EjerciciosCollection;
import javasensei.db.collections.RankingEjerciciosCollection;
import javasensei.estudiante.ModeloEstudiante;
import org.apache.mahout.cf.taste.model.DataModel;

/**
 *
 * @author Rock
 */
public class RankingEjerciciosManager{

    private RankingEjerciciosCollection rankingCollection = new RankingEjerciciosCollection();
    private EjerciciosCollection ejerciciosCollection = new EjerciciosCollection();
    
    protected static DataModel dataModel;
    
    public RankingEjerciciosManager(){
        if (dataModel==null){
            
            try {
                //Generamos el archivo CSV temporal
                //TODO
                File.createTempFile("datamodel", ".csv");
                //dataModel =  new GenericJDBCDataModel(null);
            } catch (IOException ex) {
                Logger.getLogger(RankingEjerciciosManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean colocarRankingDefault(ModeloEstudiante estudiante) {
        boolean result = false;

        try {
            DBCollection collection = ejerciciosCollection.getEjerciciosCollection();
            DBCollection ranking = rankingCollection.getRankingEjerciciosCollection();

            QueryBuilder query = QueryBuilder.start("id")
                    .notIn(ranking.distinct("idEjercicio", 
                                    QueryBuilder.start("idAlumno").
                                            is(estudiante.getId()).get()
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
                            .add("idAlumno", estudiante.getId())
                            .add("ranking", 2) //2 De acuerdo a la escala
                            .get()
                    );
                }
            }
            
            ranking.insert(listObjects);
            
            result = true;

        } catch (Exception ex) {
            System.err.println(ex);
        }

        return result;
    }
}
