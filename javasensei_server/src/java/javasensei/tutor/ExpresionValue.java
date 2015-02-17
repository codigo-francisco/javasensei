package javasensei.tutor;

/**
 *
 * @author Rock
 */
public class ExpresionValue {
    public static final String NEUTRAL = "neutral"
            ,ENCANTADO = "encantado"
            ,SORPRENDIDO = "sorprendido"
            ,COMPASIVO = "compasivo"
            ,ESCEPTICO = "esceptico";
    
    public static String getExpresionString(double value){
        String resultado =  NEUTRAL;
        switch((int)value){
            case 0:
            case 1:
                resultado = NEUTRAL;
                break;
            case 2:
                resultado = ENCANTADO;
                break;
            case 3:
                resultado = SORPRENDIDO;
                break;
            case 4:
                resultado = COMPASIVO;
                break;
            case 5:
            case 6:
                resultado = ESCEPTICO;
                break;
        }
        
        return resultado;
    }
    /*
    TERM neutral := trian 0 0.5 1;
        TERM encantado := trian 1 1.5 2;
	TERM sorprendido := trian 2 2.5 3;
	TERM compasivo := trian 3 3.5 4;
        TERM exeptico := trian 4 4.5 4.9;
    */
}
