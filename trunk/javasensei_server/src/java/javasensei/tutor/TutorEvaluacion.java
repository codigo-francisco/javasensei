package javasensei.tutor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javasensei.estudiante.ModeloEstudiante;
import javasensei.ia.fuzzylogic.tutor.CaminoCarga;
import javasensei.ia.fuzzylogic.tutor.CaminoErroneoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoFinalOptimoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoFinalSubOptimoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoOptimoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoSubOptimoFuzzyLogic;
import javasensei.ia.recognitionemotion.Emocion;
import javasensei.db.managments.EstudiantesManager;

/**
 *
 * @author Rock
 */
public class TutorEvaluacion {

    private ModeloEstudiante estudiante;
    //private CaminoFuzzyLogic camino;
    private Gson gson = new Gson();
    private EstudiantesManager managerDb = new EstudiantesManager();

    public TutorEvaluacion(String jsonEstudiante) {
        try {
            estudiante = new ModeloEstudiante(jsonEstudiante);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public TutorEvaluacion(ModeloEstudiante estudiante) {
        this.estudiante = estudiante;
    }

    protected String prepararRespuesta(CaminoFuzzyLogic camino) {
        //Antes de realizar la evauacion, se guardan los datos del perfil actualizados
        managerDb.updateDataStudent(estudiante);
        
        ResultadoTutor resultado = camino.evaluarEmocion();

        JsonObject json = new JsonObject();
        json.addProperty("expresion", resultado.getExpresion());
        json.addProperty("intervencion", resultado.getIntervencion());
        json.addProperty("retroalimentacion", resultado.getRetroalimentacion());
        json.addProperty("opcion1", resultado.getOpcion1());
        json.addProperty("opcion2", resultado.getOpcion2());
        json.addProperty("opcion3", resultado.getOpcion3());

        return gson.toJson(json);
    }

    protected String prepararRespuesta(CaminoFuzzyLogic camino, boolean intervencion) {
        ResultadoTutor resultado = camino.evaluarEmocion();

        JsonObject json = new JsonObject();
        json.addProperty("expresion", resultado.getExpresion());
        json.addProperty("intervencion", true);
        json.addProperty("retroalimentacion", resultado.getRetroalimentacion());
        json.addProperty("opcion1", resultado.getOpcion1());
        json.addProperty("opcion2", resultado.getOpcion2());
        json.addProperty("opcion3", resultado.getOpcion3());

        return gson.toJson(json);

    }

    /**
     * Metodo para obtener una emocion del tutor
     *
     * @return Emocion obtenida en formato json
     */
    public String obtenerEmocionCarga() {
        return prepararRespuesta(new CaminoCarga(estudiante), true);
    }

    /**
     * Metodo que obtiene la estrategia ante un paso erroneo y regresa en
     * formato JSON
     *
     * @return Cadena Json que se enviara al tutor del lado del cliente.
     */
    public String obtenerEvaluacionPasoErroneo() {
        return prepararRespuesta(new CaminoErroneoFuzzyLogic(estudiante));
    }

    /**
     * Metodo que obtiene la estrategia ante un paso suboptimo y regresa en
     * formato JSON
     *
     * @return Cadena Json que se enviara al tutor del lado del cliente.
     */
    public String obtenerEvaluacionPasoSubOptimo() {
        return prepararRespuesta(new CaminoSubOptimoFuzzyLogic(estudiante));
    }

    /**
     * Metodo que obtiene la estrategia ante un paso suboptimo y regresa en
     * formato JSON
     *
     * @return Cadena Json que se enviara al tutor del lado del cliente.
     */
    public String obtenerEvaluacionPasoFinalSubOptimo() {
        return prepararRespuesta(new CaminoFinalSubOptimoFuzzyLogic(estudiante));
    }

    /**
     * Metodo que obtiene la estrategia ante un paso suboptimo y regresa en
     * formato JSON
     *
     * @return Cadena Json que se enviara al tutor del lado del cliente.
     */
    public String obtenerEvaluacionPasoFinalOptimo() {
        return prepararRespuesta(new CaminoFinalOptimoFuzzyLogic(estudiante));
    }

    /**
     * Metodo que obtiene la estrategia ante un paso optimo y regresa en formato
     * JSON
     *
     * @return Cadena Json que se enviara al tutor del lado del cliente.
     */
    public String obtenerEvaluacionPasoOptimo() {
        return prepararRespuesta(new CaminoOptimoFuzzyLogic(estudiante));
    }
}
