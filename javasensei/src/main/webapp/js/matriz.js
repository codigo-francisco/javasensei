/*Desplegar la matriz de ejercicios*/
function llenarTabla(datos) {

    var matriz = $("#datosTabla");
    matriz.empty();

    var maxColspan = 0;
    var suma = 0;
    var sumaTot = 0;

    for (var index = 0; index < datos.length; index++) {
        var dato = datos[index];
        var tr = $("<tr>");
        tr.append($("<th>").text(dato.nombre));

        var arreglo = dato.ejercicios;

        var td = $("<td>");

        if (arreglo.length > maxColspan)
            maxColspan = arreglo.length;

        for (var indexDato = 0; indexDato < arreglo.length; indexDato++) {
            var valorTerminado = arreglo[indexDato].terminado;
            suma += valorTerminado;
            var span = $("<span>");
            switch (valorTerminado) {
                case 0: //Ejercicio sin responder
                    span.addClass("white");
                    break;
                case 1: //Ejercicio terminado de manera óptima
                    span.addClass("optimo");
                    break;
                case 0.7: //Ejercicio terminado de manera subóptima
                    span.addClass("sub");
                    break;
            }
            span.text(indexDato + 1);
            td.append(span);
        }

        tr.append(td);

        var longitud = arreglo.length;
        var prom = (longitud > 0) ? (suma / longitud) * 100 : 0; //Posible error de lecciones con 0 ejercicios
        sumaTot += prom;
        tr.append($("<td>").text(Math.round(prom)+ "%"));
        matriz.append(tr);
        suma = 0;
    }
    ;
    //var promTot = (sumaTot / datos.length);
    //$("#tdtot").text(Math.round(promTot) + "%");

    //colspan variado
    //$(".colspanvar").prop("colspan", maxColspan);
}
