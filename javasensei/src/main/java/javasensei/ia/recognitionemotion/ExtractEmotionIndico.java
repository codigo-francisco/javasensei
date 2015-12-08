package javasensei.ia.recognitionemotion;

import io.indico.Indico;
import io.indico.api.image.FacialEmotion;
import io.indico.api.results.IndicoResult;
import io.indico.api.utils.IndicoException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rock
 */
public class ExtractEmotionIndico implements INeuralNetwork<BufferedImage> {
    
    private Indico indico=new Indico("0cf4020b79fe55449a642020331dc870");
    
    @Override
    public Emocion processData(BufferedImage datos) {
        Emocion emocion = Emocion.NEUTRAL;
        
        try {
            IndicoResult result = indico.fer.predict(datos, "jpg");
            Map<FacialEmotion, Double> resultados = result.getFer();
            
            FacialEmotion facialEmotion = FacialEmotion.Neutral;
            Double valorMasAlto=Double.MIN_VALUE;
            
            for(FacialEmotion emotion:resultados.keySet()){
                Double valor = resultados.get(emotion);
                
                if (valor>valorMasAlto){
                    facialEmotion = emotion;
                    valorMasAlto = valor;
                }
                
                System.out.printf("Emocion %s, Valor: %s\n", emotion, valor);
            }
            
            emocion = parse(facialEmotion);
        } catch (UnsupportedOperationException | IOException | IndicoException ex) {
            Logger.getLogger(ExtractEmotionIndico.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emocion;
    }
    
    private static Emocion parse(FacialEmotion facialEmotion){
        Emocion result = Emocion.NEUTRAL;
        
        switch(facialEmotion){
            case Angry:
                result = Emocion.ENOJADO;
                break;
            case Fear:
            case Surprise:
                result = Emocion.SORPRESA;
                break;
            case Happy:
                result = Emocion.FELIZ;
                break;
            case Neutral:
                result = Emocion.NEUTRAL;
                break;
            case Sad:
                result = Emocion.TRISTE;
                break;
        }
        
        return result;
    }
}
