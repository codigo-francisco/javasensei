
import java.nio.charset.Charset;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
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
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import org.junit.Assert;
import org.junit.Test;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.junit.After;

/**
 *
 * @author Administrador
 */
public class StresfulTest {

    private static final String EMPTY = "";
    private static final String MAIN_PATH = "http://javasensei.ddns.net/javasensei/servicios/";

    private static String executeRequest(String url) {
        return executeRequest(url, "GET", null);
    }
    
    static int fallas = 0;

    private static String executeRequest(String url, String method, String params) {
        String response = EMPTY;
        try {
            url = MAIN_PATH.concat(url);

            
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(0);
            connection.setRequestMethod(method);
            connection.setDoInput(true);

            if (method.equals("POST")) {
                connection.setDoOutput(true);
                if (params != null) {
                    OutputStream output = connection.getOutputStream();
                    output.write(params.getBytes(Charset.forName("UTF-8")));
                    output.flush();
                }
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            response = br.lines().parallel().collect(Collectors.joining("\n"));

            connection.disconnect();
            
            System.out.println("Peticion exitosa!");

        } catch (IOException ex) {
            fallas++;
            System.out.println(ex.getMessage());
        }

        return response;
    }
    
    public static void main(String[] args){
        new StresfulTest().fullTestSystemRemote();
    }
    
    //@Test
    public void fullTestSystemRemote() {
        int quantityUsers = 10; //Cantidad de usuarios

        try {
            ExecutorService executors = Executors.newFixedThreadPool(quantityUsers);
            executors.awaitTermination(20, TimeUnit.SECONDS);

            List<Callable<String>> metodos = new ArrayList<>();

            for (int position = 1; position <= quantityUsers; position++) {
                metodos.add((Callable<String>) () -> {
                    //Creacion de usuarios
                    String idFacebook = Integer.toString(new Random().nextInt());
                    String token = "qjh123hj21jh23hj1jh2jh13h2132132j13h21j3h"; //Token aleatorio, no importa
                    String foto = "[\"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\"]";
                    //Creacion de usuario
                    String jsonUsuario = executeRequest(
                            String.format("estudiantes/getorcreatestudent?idFacebook=%s&token=%s", idFacebook, token)
                    );

                    JsonParser parser = new JsonParser();
                    JsonObject usuarioJson = parser.parse(jsonUsuario).getAsJsonObject();

                    //Carga de ejercicio
                    String jsonPasoCarga = executeRequest("estrategiatutor/emociontutor", "POST", "datosestudiante=" + jsonUsuario);
                    Assert.assertNotSame(EMPTY, jsonPasoCarga);

                    //Simulacion paso erroneo
                    String jsonCaminoErroneo = executeRequest("estrategiatutor/caminoerroneo", "POST",
                            String.format("datosestudiante=%s&fotos=%s", jsonUsuario, foto
                            )
                    );
                    Assert.assertNotSame(EMPTY, jsonCaminoErroneo);

                    //Simulacion paso optimo
                    String jsonCaminoOptimo = executeRequest("estrategiatutor/caminooptimo", "POST",
                            String.format("datosestudiante=%s&fotos=%s", jsonUsuario, foto
                            )
                    );
                    Assert.assertNotSame(EMPTY, jsonCaminoOptimo);

                    //Simulacion paso suboptimo
                    String jsonCaminoSubOptimo = executeRequest("estrategiatutor/caminosuboptimo", "POST",
                            String.format("datosestudiante=%s&fotos=%s", jsonUsuario, foto
                            )
                    );
                    Assert.assertNotSame(EMPTY, jsonCaminoSubOptimo);

                    //Simulacion paso final optimo
                    String jsonCaminoFinalOptimo = executeRequest("estrategiatutor/caminofinaloptimo", "POST",
                            String.format("datosestudiante=%s&fotos=%s", jsonUsuario, foto
                            )
                    );
                    Assert.assertNotSame(EMPTY, jsonCaminoFinalOptimo);

                    //Simulacion carga de grafica de avance
                    String jsonGraficaAvance
                            = executeRequest("dominioEstudiante/getDataGraphics?idAlumno=" + usuarioJson.get("id").getAsLong());

                    Assert.assertNotSame(EMPTY, jsonGraficaAvance);

                    //Simulacion carga de recomendacion ejercicios
                    String jsonRecomendacionEjercicios
                            = executeRequest("recomendacion/ejercicios", "POST", "usuario="+jsonUsuario);
                    Assert.assertNotSame(EMPTY, jsonRecomendacionEjercicios);

                    //Simulacion carga de recurso
                    String jsonRecursos
                            = executeRequest("recursos/todos", "POST", "usuario="+jsonUsuario);
                    Assert.assertNotSame(EMPTY, jsonRecursos);

                    return jsonUsuario;

                });
            }

            executors.invokeAll(metodos);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @After
    public void reportFallas(){
        System.out.printf("%s Fallas %s", fallas, System.lineSeparator());
    }

    //@Test
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
                String uri = String.format("http://javasensei.ddns.net/javasensei/servicios/estudiantes/getorcreatestudent?idFacebook=%s&token=%s", idFacebook, token);
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

                String fotos = "";

                String params = String.format("datosestudiante=%s&fotos=%s", json, fotos);

                OutputStream output = connection.getOutputStream();
                output.write(params.getBytes(Charset.forName("UTF-8")));
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
