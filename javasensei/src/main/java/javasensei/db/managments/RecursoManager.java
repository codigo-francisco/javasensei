package javasensei.db.managments;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import javasensei.db.Connection;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.mongodb.BasicDBList;

/**
 *
 * @author Rock
 */
public class RecursoManager {

    DBCollection recursosCollection = Connection.getCollection().get(CollectionsDB.RECURSOS);
    DBCollection leccionesCollection = Connection.getCollection().get(CollectionsDB.LECCIONES);
    DBCollection rankingCollection = Connection.getCollection().get(CollectionsDB.RANKING_RECURSOS);
    DBCollection temasCollection = Connection.getCollection().get(CollectionsDB.TEMAS);
    DBCollection ejerciciosCollection = Connection.getCollection().get(CollectionsDB.EJERCICIOS);
    
    public double getRanking(int idRecurso, Long idAlumno){
        return new Double(recursosCollection.findOne(
                new BasicDBObject("idAlumno", idAlumno)
                .append("idRecurso", idRecurso)
            ).get("ranking").toString()
        );
    }
    
    public Boolean setRanking(int idRecurso, Long idAlumno, int ranking) {
        boolean result = false;

        try {
            rankingCollection.update(
                    QueryBuilder.start("idRecurso").is(idRecurso)
                    .put("idAlumno").is(idAlumno)
                    .get(), QueryBuilder.start("$set").is(
                            QueryBuilder.start("ranking").is(ranking).get()
                    ).get()
            );
            
            result = true;
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return result;
    }

    public String obtenerRecursosPorCategoria(long idUsuario) {

        List<DBObject> lista = new ArrayList<>();

        for (DBObject object : leccionesCollection.find().toArray()) {

            BasicDBObjectBuilder build = BasicDBObjectBuilder.start();

            List<DBObject> recursos = recursosCollection.find(
                    QueryBuilder.start("idLeccion").is(object.get("id")).get()
            ).toArray();
            
            if(!recursos.isEmpty())
                build.add("nombre", object.get("nombre"));
            
                
            List<DBObject> recursos2 = new ArrayList<>();
            for (DBObject recurso : recursos) {
                DBObject ranking = rankingCollection.findOne(
                        QueryBuilder.start("idRecurso").is(recurso.get("id"))
                        .put("idAlumno").is(idUsuario)
                        .get());

                //Obtener ranking y colocarlo en el objeto
                recurso.put("ranking", ranking.get("ranking"));

                recursos2.add(recurso);

            }
            
            if(!recursos2.isEmpty()){
                build.add("recursos", recursos2);
                lista.add(build.get());   
            }
                     
        }
        
        System.out.println(lista.toString());

        return lista.toString();
    }
    public String getJSONList(int col){
        List<DBObject> jsons = null;
        switch(col){
            case 1: jsons = leccionesCollection.find(new BasicDBObject(), new BasicDBObject().append("_id", 0)).toArray(); break;
            case 2: jsons = temasCollection.find(new BasicDBObject(), new BasicDBObject().append("_id", 0)).toArray(); break;
            case 3: jsons = ejerciciosCollection.find(new BasicDBObject(), new BasicDBObject().append("_id", 0)).toArray(); break;
        }
        BasicDBList listDB = new BasicDBList();
        for(DBObject o: jsons)
            listDB.add(o);
        return listDB.toString();
    }
    public String guardar(int coleccion, String json, String oldJson){
        BasicDBObject jsonResult = new BasicDBObject();
        DBObject old = oldJson.isEmpty()?null:(DBObject)JSON.parse(oldJson);
        WriteResult result = null;
        //Eliminar
        if(json.trim().equalsIgnoreCase("delete") && old!=null){
            switch(coleccion){
                case 1: result = leccionesCollection.remove(old); break;
                case 2: result = temasCollection.remove(old); break;
                case 3: result = ejerciciosCollection.remove(old); break;
            }
            if(result.getN() > 0){
                jsonResult.append("result", true);
                jsonResult.append("More", "Operación de eliminacion realizada correctamente.");
            }
            else{
                jsonResult.append("result", false);
                jsonResult.append("More", "Error en la operación de eliminacion.");
            }
            return jsonResult.toString();
        }
        DBObject update; 
        try{
            update = (DBObject)JSON.parse(json);
        }catch(Exception exc){
            jsonResult.append("result",false);
            jsonResult.append("More", "No es posible realizar la operación, la cadena no es de tipo JSON.");
            return jsonResult.toString();
        }
        //Valida que no exista el ID
        if(old==null || (int)update.get("id") != (int)old.get("id"))
            if(validarId(coleccion, (int)update.get("id"))){
                jsonResult.append("result",false);
                jsonResult.append("More", "No es posible realizar la operación, el ID ya existe.");
                return jsonResult.toString();
            }
        //GuardarNuevo
        if(old==null){
            switch(coleccion){
                case 1: result=leccionesCollection.insert(update); break;
                case 2: result=temasCollection.insert(update); break;
                case 3: result=ejerciciosCollection.insert(update); break;
            }
        } 
        //Actualizar
        else{
            switch(coleccion){
                case 1: result=leccionesCollection.update(old, update); break;
                case 2: result=temasCollection.update(old, update); break;
                case 3: result=ejerciciosCollection.update(old, update); break;
            }
        }
        if(result.isUpdateOfExisting() || result.getN()==0){
            jsonResult.append("result",true);
            jsonResult.append("More", "Operación Guardar realizada correctamente.");
        }
        else{
            jsonResult.append("result",false);
            jsonResult.append("More", "Error en la operación Guardar.");
        }
        return jsonResult.toString();
    }
    public Boolean validarId(int coleccion, int id){
        DBObject query = QueryBuilder.start("id").is(id).get();
        DBObject json = null;
        switch(coleccion){
            case 1: json=leccionesCollection.findOne(query); break;
            case 2: json=temasCollection.findOne(query); break;
            case 3: json=ejerciciosCollection.findOne(query); break;
        }
        return json!=null;
        //regresa true si ya existe ese ID
    }
}
