package javasensei.ia.sentitext;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.razorvine.pickle.PickleException;
import net.razorvine.pyro.PyroException;
import net.razorvine.pyro.PyroProxy;

/**
 *
 * @author oramas
 */
public class ExtractPolarityTextPython {
    public String processData(String datos) {
        String message = "positivo";
        try{           
            PyroProxy remoteObject = new PyroProxy("localhost", 25315, "text_service");
            
            Object result = (Object)remoteObject.call("emotion_text_prediction_java", datos);
            message = (String)result;  // cast to the type that 'pythonmethod' returns
            System.out.println("Result Message = "+ message);            
            remoteObject.close();
            return message;
            
        }catch(IOException | PickleException | PyroException ex){
            Logger.getLogger(ExtractPolarityTextPython.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }
    
    
}
