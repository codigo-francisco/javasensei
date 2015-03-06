package javasensei.tutor;

/**
 *
 * @author Rock
 */
public class ResultadoTutor {

    private double expresionValue;
    private double intervencionValue;
    private double retroalimentacionValue;

    public double getExpresionValue() {
        return expresionValue;
    }
    
    public String getExpresion(){
        return ExpresionValue.getExpresionString(expresionValue);
    }

    public void setExpresionValue(double expresionValue) {
        this.expresionValue = expresionValue;
    }
    
    public double getIntervencionValue(){
        return intervencionValue;
    }
    
    public String getRetroalimentacion(){
        return RetroalimentacionValue.getStringRetroalimentacion(retroalimentacionValue);
    }
    
    public void setRetroalimentacionValue(double retroalimentacionValue){
        this.retroalimentacionValue = retroalimentacionValue;
    }
    
    public double getRetroalimentacionValue(){
        return retroalimentacionValue;
    }
    
    public void setIntervencionValue(double intervencionValue){
        this.intervencionValue = intervencionValue;
    }
    
    public Boolean getIntervencion(){
        return intervencionValue>=1; //Si es 1, requiere intervencion
    }
}
