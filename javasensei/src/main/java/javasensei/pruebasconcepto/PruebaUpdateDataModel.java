package javasensei.pruebasconcepto;

import javasensei.db.managments.EstudiantesManager;
import javasensei.estudiante.ModeloEstudiante;

/**
 *
 * @author PosgradoMCC
 */
public class PruebaUpdateDataModel {
    public static void main(String[] args){
        ModeloEstudiante estudiante = new ModeloEstudiante();
        estudiante.setId(new Long("729806967073530"));
        
        EstudiantesManager manager = new EstudiantesManager(estudiante);
        
        manager.createOrUpdateStudentModel();
        
        manager.finalizarEjercicio(1);
        
        System.out.println("Habilidad Global: "+manager.getAbilityGlobal());
    }
}
