package javasensei.exceptions;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author Rock
 */
public class JavaException extends Exception{

    public JavaException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        
        StackTraceElement[] elements = getStackTrace();
        
        String line = System.lineSeparator();
        
        if (elements.length>0){
            StackTraceElement element = elements[0];
            message.append(String.format("Clase con el error: %s%s", element.getClassName(),line));
            message.append(String.format("Metodo con el error: %s%s", element.getMethodName(),line));
            message.append(String.format("Linea de codigo con el error: %s%s", element.getLineNumber(),line));
        }
        
        return message.toString();
    }
    
    public static void printMessage(Throwable cause, OutputStream output){
        PrintStream print;
        boolean isPrint = output instanceof PrintStream;
        if (!isPrint)
            print = new PrintStream(output);
        else
            print = (PrintStream)output;
        
        JavaException exception = new JavaException(cause);
        print.println(exception.getMessage());
    }
}
