package javasensei.ia.fuzzylogic.tutor;

import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author Rock
 */
public class CaminoErroneoFuzzyLogic extends CaminoFuzzyLogic{    
    public CaminoErroneoFuzzyLogic(ModeloEstudiante estudiante){
        super(estudiante);
    }

    @Override
    protected String getFile() {
        return getFile("caminoerroneo.fcl");
    }
}
