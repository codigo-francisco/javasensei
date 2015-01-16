package javasensei.tutor;

/**
 *
 * @author Rock
 */
public class OpcionValue {
    public static final String RECURSO = "recurso";
    public static final String EJERCICIO = "ejercicio";
    public static final String RESPUESTA = "respuesta";
    
    public static String getOpcionString(double value){
        String result = EJERCICIO;
        
        switch((int)value){
            case  1:
                result = RECURSO;
                break;
            case 2:
                result = EJERCICIO;
                break;
            case 3:
                result = RESPUESTA;
                break;
        }
        
        return result;
    }
}
