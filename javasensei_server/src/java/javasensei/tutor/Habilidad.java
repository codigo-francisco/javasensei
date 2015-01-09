package javasensei.tutor;

import javasensei.util.EnumHelper;

/**
 *
 * @author Rock
 */
public enum Habilidad {
    MALA(1),
    BUENA(2);
    
    private final int value;
    
    Habilidad(int i){
        this.value = i;
    }
    
    public int getValue(){
        return value;
    }

    @Override
    public String toString() {
        return EnumHelper.toString(name());
    }
}
