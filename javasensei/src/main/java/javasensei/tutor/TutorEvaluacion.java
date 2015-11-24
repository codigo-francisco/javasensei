package javasensei.tutor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javasensei.db.managments.EstudiantesManager;
import javasensei.estudiante.ModeloEstudiante;
import javasensei.ia.fuzzylogic.tutor.CaminoCarga;
import javasensei.ia.fuzzylogic.tutor.CaminoErroneoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoFinalOptimoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoFinalSubOptimoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoOptimoFuzzyLogic;
import javasensei.ia.fuzzylogic.tutor.CaminoSubOptimoFuzzyLogic;

/**
 *
 * @author Rock
 */
public class TutorEvaluacion {

    private ModeloEstudiante estudiante;
    private Gson gson = new Gson();
    private EstudiantesManager managerDb;

    public TutorEvaluacion(ModeloEstudiante estudiante) {
        this.estudiante = estudiante;
        managerDb = new EstudiantesManager(this.estudiante);
    }

    protected String prepararRespuesta(CaminoFuzzyLogic camino) {        
        ResultadoTutor resultado = camino.evaluarEmocion();

        JsonObject json = new JsonObject();
        json.addProperty("expresion", resultado.getExpresion());
        json.addProperty("intervencion", resultado.getIntervencion());
        json.addProperty("retroalimentacion", resultado.getRetroalimentacion());
        json.addProperty("emocion", estudiante.getEmocionActual().toString());

        return gson.toJson(json);
    }

    protected String prepararRespuesta(CaminoFuzzyLogic camino, boolean intervencion) {
        ResultadoTutor resultado = camino.evaluarEmocion();

        JsonObject json = new JsonObject();
        json.addProperty("expresion", resultado.getExpresion());
        json.addProperty("intervencion", true);
        json.addProperty("retroalimentacion", resultado.getRetroalimentacion());

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
        managerDb.updateDataStudent();
        return prepararRespuesta(new CaminoFinalSubOptimoFuzzyLogic(estudiante));
    }

    /**
     * Metodo que obtiene la estrategia ante un paso suboptimo y regresa en
     * formato JSON
     *
     * @return Cadena Json que se enviara al tutor del lado del cliente.
     */
    public String obtenerEvaluacionPasoFinalOptimo() {
        managerDb.updateDataStudent();
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
