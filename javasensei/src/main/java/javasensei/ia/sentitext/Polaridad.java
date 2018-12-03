package javasensei.ia.sentitext;

import javasensei.util.EnumHelper;

/**
 *
 * @author oramas
 */
public enum Polaridad {
    NEUTRAL(0),
    POSITIVO(1),
    NEGATIVO(2);
    
    private final int polaridadValue;
    
    Polaridad(int polaridadValue){
        this.polaridadValue = polaridadValue;
    }
    
    public double getValue(){
        return polaridadValue;
    }
    
    public static Polaridad getPolaridad(String value){
        Polaridad polaridad = Polaridad.NEUTRAL;
        
        for (Polaridad p : Polaridad.values()){
            if (p.name().toLowerCase().equals(value.toLowerCase())){
                polaridad = p;
                break;
            }
        }
        
        return polaridad;
    }

    public static Polaridad getPolaridad(int polaridadValue){
        switch(polaridadValue){
            case 0:
                return NEUTRAL;
            case 1:
                return POSITIVO;
            case 2:
                return NEGATIVO;
            default:
                return POSITIVO;
        }
    }

    @Override
    public String toString() {
        return EnumHelper.toString(name());
    }
}
