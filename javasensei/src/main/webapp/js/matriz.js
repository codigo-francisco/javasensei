/*Desplegar la matriz de ejercicios*/
function llenarTabla(datos){
    var matriz=$("#tablaEjercicios");
    matriz.empty();
    var suma=0;
    var sumaTot=0;
    var tr = ($("<tr>"));
    tr.append($("<td>").text("Lecciones"));
    tr.append($("<td colspan=\"10\">").text("Ejercicios"));
    tr.append($("<td id=\"prome\">").text("Porcentaje"));
    matriz.append(tr);
   // matriz.append($("<tr>").append($("<th id=\"prome\" colspan=\"12\">").text("PROM")));
    for (var index = 0; index < datos.length; index++) {
	var dato = datos[index];
	var tr = $("<tr>").append($("<th class=\"lec\">").text(dato.nombre));

	var arreglo = dato.ejercicios;
	for(var indexDato = 0; indexDato < arreglo.length; indexDato++ ){
            var tmp=arreglo[indexDato].terminado;
            suma+=tmp;
            switch(tmp){
                //Ejercicio sin responder
                case 0:
                    tr.append($("<td><span class=\"white\">"+(indexDato+1)+"</span>"));
                    break;
                //Ejercicio terminado de manera óptima
                case 1:
                    tr.append($("<td><span class=\"optimo\">"+(indexDato+1)+"</span>"));
            
                    break;
                //Ejercicio terminado de manera subóptima
                case 0.7: 
                    tr.append($("<td><span class=\"sub\">"+(indexDato+1)+"</span>"));
                    break;
            }
            
        }
        
        var longitud = arreglo.legth;
        var prom=(longitud>0)?(suma/arreglo.length)*100:0; //Posible error de lecciones con 0 ejercicios
        sumaTot+=prom;
        tr.append($("<td style=\"background: ThreeDHighlight;\">").text(Math.round((prom))+"%"));
        matriz.append(tr);
        suma=0;

    };
    var promTot=(sumaTot/datos.length);
    tr = $("<tr>");
    tr.append($("<th class=\"lec\" colspan=\"11\" style=\"background: ThreeDHighlight;\">").text("TOTAL"));
    tr.append($("<th id=\"tdtot\" style=\"background: ThreeDHighlight;\">").text((+Math.round(promTot))+"%"));
    matriz.append(tr);

}
