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
        ModeloEstudiante estudiante = new ModeloEstudiante();
        
        RankingEjerciciosManager es = new RankingEjerciciosManager(estudiante);
        estudiante.setId(6464465);
        
        es.colocarRankingDefault();
    }
}
