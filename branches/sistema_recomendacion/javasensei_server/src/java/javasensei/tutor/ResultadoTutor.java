package javasensei.tutor;

/**
 *
 * @author Rock
 */
public class ResultadoTutor {

    private double opcion1value;
    private double opcion2value;
    private double opcion3value;
    private double expresionValue;
    private double intervencionValue;
    private double retroalimentacionValue;

    public double getOpcion1value() {
        return opcion1value;
    }
    
    public String getOpcion1(){
        return OpcionValue.getOpcionString(opcion1value);
    }

    public void setOpcion1value(double opcion1value) {
        this.opcion1value = opcion1value;
    }

    public double getOpcion2value() {
        return opcion2value;
    }
    
    public String getOpcion2(){
        return OpcionValue.getOpcionString(opcion2value);
    }

    public void setOpcion2value(double opcion2value) {
        this.opcion2value = opcion2value;
    }

    public double getOpcion3value() {
        return opcion3value;
    }
    
    public String getOpcion3(){
        return OpcionValue.getOpcionString(opcion3value);
    }
    
    public void setOpcion3value(double opcion3value) {
        this.opcion3value = opcion3value;
    }

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
