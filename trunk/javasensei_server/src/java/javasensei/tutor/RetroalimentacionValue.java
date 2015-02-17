package javasensei.tutor;

/**
 *
 * @author Rock
 */
public class RetroalimentacionValue {
    public static String POSITIVA="positiva";
    public static String NEGATIVA="negativa";
    public static String NEUTRAL="neutral";
    
    public static String getStringRetroalimentacion(double value){
        String resultado = POSITIVA;
        
        switch(new Long(Math.round(value)).intValue()){
            case 0:
            case 1:
                resultado = POSITIVA;
                break;
            case 2:
                resultado = NEUTRAL;
                break;
            case 3:
            case 4:
                resultado = NEGATIVA;
                break;
        }
        
        return resultado;
    }
}
