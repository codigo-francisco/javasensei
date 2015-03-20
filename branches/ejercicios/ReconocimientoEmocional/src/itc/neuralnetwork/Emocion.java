package itc.neuralnetwork;

/**
 * @author Francisco Gonzalez Hernandez, Ramon Zatarain Cabada, Lucia Barron Estrada, Raul Oramas Bustillos
 * Email Contacto: franciscogonzalez_hernandez@hotmail.com
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
    
    public int getValue(){
        return emocionValue;
    }
    
    public static Emocion valueOf(int emocionValue){
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
        String name = this.name();
        
        return name.substring(0,1).concat(name.substring(1).toLowerCase());
    }
}
