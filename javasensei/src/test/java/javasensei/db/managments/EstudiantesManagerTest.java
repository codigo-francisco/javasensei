/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.db.managments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javasensei.estudiante.ModeloEstudiante;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonParser;
import com.mongodb.QueryBuilder;
import java.util.HashSet;
import javasensei.db.Connection;

/**
 *
 * @author Rock
 */
public class EstudiantesManagerTest {
    
    public EstudiantesManagerTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of finalizarEjercicio method, of class EstudiantesManager.
     */
    //@Test
    public void testConcurrencyExcercise() {
        List<Integer> listId = new ArrayList<>();
        int threadSize = 1000; //1000 threads son 1000 usuarios nuevos
        
        ExecutorService executors = Executors.newFixedThreadPool(threadSize);
        
        List<Callable<String>> metodos = new ArrayList<>();
        
        for (int index = 1; index<threadSize+1;index++){
            metodos.add((Callable<String>) () -> {
                ModeloEstudiante estudiante = new ModeloEstudiante();
                estudiante.setIdFacebook(new Integer(new Random().nextInt()).toString());
                estudiante.setToken("sadfsdfasJ(A(FSDDAFsafdA(SDFASDf");
                
                EstudiantesManager manager = new EstudiantesManager(estudiante);
                
                return manager.insertOrCreateStudent();
            });
        }
        
        try {
            List<Future<String>> resultadosMetodos = executors.invokeAll(metodos);
            List<String> resultados = new ArrayList<>();
            
            for(Future<String> future:resultadosMetodos){
                try {
                    String result = future.get();
                    System.out.println(result);
                    resultados.add(result);
                } catch (ExecutionException ex) {
                    resultados.add(String.format("{error:%s}",ex.getMessage()));
                }
            }
            
            //Convertimos los valores a JSON y tomamos su ID, de ahi detectamos si tiene repetidos
            JsonParser parser = new JsonParser();
            
            for (String result : resultados){
                listId.add(new Integer(parser.parse(result).getAsJsonObject().get("id").toString()));
            }
            
            listId.sort(null);
            
            System.out.println(listId);
            
            //Verificamos si hay duplicados
            Assert.assertTrue("Prueba Exitosa",new HashSet(listId).size()==listId.size());           
            
        } catch (InterruptedException ex) {
            Assert.fail(ex.getMessage());
        } 
        
        //Borramos los registros
        Connection.getCollection().get(CollectionsDB.ALUMNOS).remove(
                QueryBuilder.start("id").in(listId)
                .get()
        );
    }
    
}