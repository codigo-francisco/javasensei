/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasensei.tutor.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Rock
 */
@javax.ws.rs.ApplicationPath("servicios")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(javasensei.tutor.rest.BitacoraRest.class);
        resources.add(javasensei.tutor.rest.ChatRest.class);
        resources.add(javasensei.tutor.rest.DominioRest.class);
        resources.add(javasensei.tutor.rest.EmocionRest.class);
        resources.add(javasensei.tutor.rest.EstrategiaTutor.class);
        resources.add(javasensei.tutor.rest.EstudianteRest.class);
        resources.add(javasensei.tutor.rest.RecomendacionRest.class);
        resources.add(javasensei.tutor.rest.RecursosRest.class);
    }
    
}
