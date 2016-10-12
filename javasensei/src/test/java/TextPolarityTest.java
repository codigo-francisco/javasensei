
import javasensei.ia.sentitext.ExtractPolarityTextPython;


/**
 *
 * @author oramas
 */
public class TextPolarityTest {
    public static void main(String[] args) {
        new TextPolarityTest().testing();
    }
    
    //@Test
    public void testing() {
        ExtractPolarityTextPython p = new ExtractPolarityTextPython();
        String texto = "Programar en Java es divertido";
        System.out.println(texto);
        System.out.println("Polaridad:"+ p.processData(texto));
    }
}
