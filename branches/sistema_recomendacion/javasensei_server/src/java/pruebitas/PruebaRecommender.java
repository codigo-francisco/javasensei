/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebitas;

import javasensei.db.managments.RankingEjerciciosManager;
import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author Rock
 */
public class PruebaRecommender {
    public static void main(String[] args){
        ModeloEstudiante estudiante = new ModeloEstudiante();
        
        estudiante.setId("123bj123kl");
        
        RankingEjerciciosManager es = new RankingEjerciciosManager(estudiante);
        
        es.colocarRankingDefault();
        
        es.getRecommenders();
    }
}
