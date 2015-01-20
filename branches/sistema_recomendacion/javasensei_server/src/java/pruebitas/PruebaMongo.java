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
public class PruebaMongo {
    public static void main(String[] args){
        RankingEjerciciosManager es = new RankingEjerciciosManager();
        ModeloEstudiante estudiante = new ModeloEstudiante();
        estudiante.setId("123bj123kl");
        
        es.colocarRankingDefault(estudiante);
    }
}
