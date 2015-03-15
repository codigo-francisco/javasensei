/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.ia.fuzzylogic.tutor;

import javasensei.estudiante.ModeloEstudiante;
import net.sourceforge.jFuzzyLogic.FIS;

/**
 *
 * @author Rock
 */
public class CaminoOptimoFuzzyLogic extends CaminoFuzzyLogic {

    public CaminoOptimoFuzzyLogic(ModeloEstudiante estudiante) {
        super(estudiante);
        if (fuzzySystem==null)
            fuzzySystem = FIS.load(getFile());
    }
    
    protected static FIS fuzzySystem;
    
    @Override
    protected FIS getFuzzySystem(){
        return fuzzySystem;
    }

    @Override
    protected String getFile() {
        return getFile("caminooptimo.fcl");
    }    
}
