
package javasensei.ia.recognitionemotion;

import javasensei.util.EnumHelper;

/**
 *
 * @author Rock
 */
public enum Emocion {
    FELIZ(0),
    SORPRESA(1),
    TRISTE(2),
    ENOJADO(3),
    NEUTRAL(4);
    
    private final int emocionValue;
    
    Emocion(int emocionValue){
        this.emocionValue = emocionValue;
    }
    
    public double getValue(){
        return emocionValue;
    }
    
    public static Emocion getEmocion(String value){
        Emocion emocion = Emocion.NEUTRAL;
        
        for (Emocion e : Emocion.values()){
            if (e.name().toLowerCase().equals(value.toLowerCase())){
                emocion = e;
                break;
            }
        }
        
        return emocion;
    }

    public static Emocion getEmocion(int emocionValue){
        switch(emocionValue){
            case 0:
                return FELIZ;
            case 1:
                return SORPRESA;
            case 2:
                return TRISTE;
            case 3:
                return ENOJADO;
            case 4:
                return NEUTRAL;
            default:
                return FELIZ;
        }
    }

    @Override
    public String toString() {
        return EnumHelper.toString(name());
    }
}