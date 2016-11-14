
package javasensei.ia.recognitionemotion;

import javasensei.util.EnumHelper;

/**
 *
 * @author Rock
 */
public enum Emocion {
    ROSTRO_NO_RECONOCIDO(-2),
    SINEMOCION(-1),
    FELIZ(1),
    SORPRESA(2),
    TRISTE(3),
    ENOJADO(4),
    NEUTRAL(5),
    ENGANCHADO(6),
    EMOCIONADO(7),
    FRUSTRADO(8),
    ABURRIDO(9);
    
    private final int emocionValue;
    
    Emocion(int emocionValue){
        this.emocionValue = emocionValue;
    }
    
    public double getValue(){
        return emocionValue;
    }
    
    public static Emocion getEmocion(String value){
        Emocion emocion = Emocion.ROSTRO_NO_RECONOCIDO;
        
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
                return ENOJADO;
            case 1:
                return FELIZ;
            case 3:
                return SORPRESA;
            case 4:
                return TRISTE;
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