
import com.google.common.base.Charsets;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import javasensei.db.managments.EstudiantesManager;
import javasensei.estudiante.ModeloEstudiante;
import javax.xml.bind.JAXBContext;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Administrador
 */
public class StresfulTest {

    @Test
    public void stressfulChangeStep() {
        //Creamos los usuarios
        int threadSize = 2; //1000 threads son 1000 usuarios nuevos

        ExecutorService executors = Executors.newFixedThreadPool(threadSize);

        List<Callable<String>> metodos = new ArrayList<>();

        for (int index = 1; index < threadSize + 1; index++) {
            metodos.add((Callable<String>) () -> {
                int idFacebook = new Random().nextInt();
                String token = "sadfsdfasJ(A(FSDDAFsafdA(SDFASDf";
                
                //Los creamos usando HTTP
                String uri = String.format("http://javasensei.ddns.net/javasensei/servicios/estudiantes/getorcreatestudent?idFacebook=%s&token=%s"
                        ,idFacebook,token);
                URL url = new URL(uri);
                HttpURLConnection connection
                        = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                
                //Conexion
                BufferedReader reader = new BufferedReader(new InputStreamReader(
			(connection.getInputStream())));

                String json = reader.lines().parallel().collect(Collectors.joining("\n"));
                
                //Peticion POST para simular el cambio de pasos
                uri = "http://javasensei.ddns.net/javasensei/servicios/estrategiatutor/caminofinalsuboptimo";
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                
                String fotos="";
                
                String params = String.format("datosestudiante=%s&fotos=%s",json,fotos);
                
                OutputStream output = connection.getOutputStream();
                output.write(params.getBytes(Charsets.UTF_8));
                output.flush();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String response = br.lines().parallel().collect(Collectors.joining("\n"));
                
                
                return response;
            });
        }

        try {
            List<Future<String>> resultadosMetodos = executors.invokeAll(metodos);
            //List<String> resultados = new ArrayList<>();
            
            final String stringEmpty = "";

            for (Future<String> future : resultadosMetodos) {
                try {
                    String result = future.get();
                    System.out.println(result);
                    Assert.assertNotSame(stringEmpty, result);
                    //resultados.add(result);
                } catch (ExecutionException ex) {
                    //resultados.add(String.format("{error:%s}", ex.getMessage()));
                    System.out.println(ex.getMessage());
                }
            }
        } catch (InterruptedException ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
