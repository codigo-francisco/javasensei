package javasensei.tutor;

import javasensei.util.EnumHelper;

/**
 *
 * @author Rock
 */
public enum Calidad {
    MALA(1),
    BUENA(2);
    
    private final int value;
    
    Calidad(int i){
        this.value = i;
    }
    
    public int getValue(){
        return value;
    }
    
    public Calidad parse(String value){
        return Calidad.valueOf(value.toUpperCase());
    }
    
    @Override
    public String toString() {
        return EnumHelper.toString(name());
    }
}
