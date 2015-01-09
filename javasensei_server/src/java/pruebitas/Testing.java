package pruebitas;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class Testing {
    public static void main(String[] args) {
        List<Reactivo> reactivos = new ArrayList<>();
        reactivos.add(new Reactivo("pregunta 1", 1));
        reactivos.add(new Reactivo("pregunta 2", 1));
        reactivos.add(new Reactivo("pregunta 3", 1));
        
        Gson gson = new Gson();
        String strGson = gson.toJson(reactivos);
        System.out.println(strGson);
    }
}
