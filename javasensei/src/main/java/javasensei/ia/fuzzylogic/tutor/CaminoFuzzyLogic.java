package javasensei.ia.fuzzylogic.tutor;

import javasensei.estudiante.ModeloEstudiante;
import javasensei.exceptions.JavaException;
import javasensei.tutor.ResultadoTutor;
import javasensei.util.FileHelper;
import net.sourceforge.jFuzzyLogic.FIS;
/**
 *
 * @author Rock
 */
public abstract class CaminoFuzzyLogic {
    protected ModeloEstudiante estudiante;
    protected String folderIA = "files/logicadifusa/";
    protected boolean emocionesEducativas = false;
    
    public void setEmocionesEducativas(boolean emocionesEducativas){
        this.emocionesEducativas = emocionesEducativas;
    }
    
    public CaminoFuzzyLogic(ModeloEstudiante estudiante, boolean emocionesEducativas){
        this.estudiante = estudiante;
        this.emocionesEducativas = emocionesEducativas;
        folderIA += (estudiante.getActivarEmociones()) ? "conemociones/":"sinemociones/";
        
    }
    
    protected abstract String getFile();
    
    protected String getFile(String name){
        return FileHelper.getInstance().getFile(folderIA.concat(name));
    }
    
    protected abstract FIS getFuzzySystemConEmociones();
    protected abstract FIS getFuzzySystemSinEmociones();
    
    public final ResultadoTutor evaluarEmocion(){
        ResultadoTutor resultado = new ResultadoTutor();
        
        try {
            FIS fis=null;

            if (estudiante.getActivarEmociones()){
                fis = getFuzzySystemConEmociones();

                double emocionActual = estudiante.getEmocionActual().getValue();
                double emocionPrevia = estudiante.getEmocionPrevia().getValue();
                
                if (emocionesEducativas){
                    emocionActual -= 5;
                    emocionPrevia -= 5;
                }
                
                fis.getFuzzyRuleSet().setVariable("emocionactual", emocionActual);
                fis.getFuzzyRuleSet().setVariable("emocionprevia", emocionPrevia);
            }else{
                fis = getFuzzySystemSinEmociones();
                fis.getFuzzyRuleSet().setVariable("tiempo", estudiante.getTiempo());
            }
            
            fis.getFuzzyRuleSet().setVariable("habilidadglobal", estudiante.getHabilidadGlobal());
            fis.getFuzzyRuleSet().setVariable("calidadrespuesta", estudiante.getCalidadRespuesta());
            
            fis.getFuzzyRuleSet().evaluate();
            
            double expresion = fis.getFuzzyRuleSet().getVariable("expresion").getValue();
            double intervencion = fis.getFuzzyRuleSet().getVariable("intervencion").getValue();
            double retroalimentacion = fis.getFuzzyRuleSet().getVariable("retroalimentacion").getValue();
            
            resultado.setExpresionValue(expresion);
            resultado.setIntervencionValue(intervencion);
            resultado.setRetroalimentacionValue(retroalimentacion);
            
        } catch (Exception ex) {
            JavaException.printMessage(ex, System.err);
        }
        
        return resultado;
    }
}
