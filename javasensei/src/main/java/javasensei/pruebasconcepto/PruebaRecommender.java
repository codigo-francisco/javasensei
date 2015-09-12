/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.pruebasconcepto;

import javasensei.db.managments.RankingManager;
import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author Rock
 */
public class PruebaRecommender {
    public static void main(String[] args){
        ModeloEstudiante estudiante = new ModeloEstudiante();
        
        estudiante.setId(564465645);
        
        RankingManager es = new RankingManager(estudiante);
        
        es.colocarRankingDefault();
        
        es.getRecommendersExercises(5, true);
    }
}
