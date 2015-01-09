/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.ia.fuzzylogic.tutor;

import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author Rock
 */
public class CaminoFinalOptimoFuzzyLogic extends CaminoFuzzyLogic {
    
    public CaminoFinalOptimoFuzzyLogic(ModeloEstudiante estudiante) {
        super(estudiante);
    }

    @Override
    protected String getFile() {
        return getFile("caminofinaloptimo.fcl");
    }    
}
