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
    
    public static Habilidad getHabilidad(String value){
        Habilidad habilidad = Habilidad.MALA;
        
        for(Habilidad h : Habilidad.values()){
            if (h.name().toLowerCase().equals(value.toLowerCase())){
                habilidad= h;
                break;
            }
        }
        
        return habilidad;
    }

    @Override
    public String toString() {
        return EnumHelper.toString(name());
    }
}
