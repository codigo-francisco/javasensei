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
public class CaminoFinalOptimoFuzzyLogic extends CaminoFuzzyLogic {
    
    public CaminoFinalOptimoFuzzyLogic(ModeloEstudiante estudiante) {
        super(estudiante);
        if (estudiante.getActivarEmociones() && fuzzySystemConEmociones==null)
            fuzzySystemConEmociones = FIS.load(getFile());
        else if (fuzzySystemSinEmociones==null)
            fuzzySystemSinEmociones = FIS.load(getFile());
    }
    
    protected static FIS fuzzySystemConEmociones;
    protected static FIS fuzzySystemSinEmociones;
    
    @Override
    protected FIS getFuzzySystemConEmociones(){
        return fuzzySystemConEmociones;
    }

    @Override
    protected FIS getFuzzySystemSinEmociones() {
        return fuzzySystemSinEmociones;
    }

    @Override
    protected String getFile() {
        return getFile("caminofinaloptimo.fcl");
    }    
}
