
package javasensei.ia.recognitionemotion;

import javasensei.util.EnumHelper;

/**
 *
 * @author Rock
 */
public enum Emocion {
    FELIZ(1),
    SORPRESA(2),
    TRISTE(3),
    ENOJADO(4),
    NEUTRAL(5);
    
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
            case 1:
                return FELIZ;
            case 2:
                return SORPRESA;
            case 3:
                return TRISTE;
            case 4:
                return ENOJADO;
            case 5:
                return NEUTRAL;
            default:
                return NEUTRAL;
        }
    }

    @Override
    public String toString() {
        return EnumHelper.toString(name());
    }
}