package javasensei.ia.fuzzylogic.tutor;

import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author Rock
 */
public class CaminoCarga extends CaminoFuzzyLogic {
    
    public CaminoCarga(ModeloEstudiante estudiante) {
        super(estudiante);
    }

    @Override
    protected String getFile() {
        return getFile("caminocarga.fcl");
    }
    
}
