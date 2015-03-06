package javasensei.ia.fuzzylogic.tutor;

import javasensei.estudiante.ModeloEstudiante;
import javasensei.tutor.ResultadoTutor;
import javasensei.util.FileHelper;
import net.sourceforge.jFuzzyLogic.FIS;
/**
 *
 * @author Rock
 */
public abstract class CaminoFuzzyLogic {
    protected ModeloEstudiante estudiante;
    protected String folderIA = "files/";
    
    public CaminoFuzzyLogic(ModeloEstudiante estudiante){
        this.estudiante = estudiante;
    }
    
    protected abstract String getFile();
    
    protected String getFile(String name){
        return FileHelper.getInstance().getFile(folderIA.concat(name));
    }
    
    public final ResultadoTutor evaluarEmocion(){
        ResultadoTutor resultado = new ResultadoTutor();
        
        try {
            //String fileName = Thread.currentThread().getContextClassLoader().getResource("E:\\Java Sensei\\java-sensei\\javasensei_server\\src\\java\\javasensei\\ia\\fuzzylogic\\tutor").getFile();
            FIS fis = FIS.load(getFile());
            
            fis.setVariable("emocionactual", estudiante.getEmocionActual().getValue());
            fis.setVariable("emocionprevia", estudiante.getEmocionPrevia().getValue());
            fis.setVariable("habilidadglobal", estudiante.getHabilidadGlobal());
            fis.setVariable("calidadrespuesta", estudiante.getCalidadRespuesta());

            fis.evaluate();
            
            double expresion = fis.getVariable("expresion").getValue();
            double intervencion = fis.getVariable("intervencion").getValue();
            double retroalimentacion = fis.getVariable("retroalimentacion").getValue();
            
            resultado.setExpresionValue(expresion);
            resultado.setIntervencionValue(intervencion);
            resultado.setRetroalimentacionValue(retroalimentacion);
            
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        
        return resultado;
    }
}
