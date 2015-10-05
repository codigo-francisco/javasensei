var menu_context;
var cerrarMenu = function () {
    //Cerrar el panel
    $("#panelmenu").panel("close");
};

function obtenerEjercicios(callback) {
    //Se hace una solicitud rest al servidor java
    var urlDominioEstudiante = url + "dominioEstudiante/getDominioEstudiante/" + usuario.id;

    $.ajax({
        url: urlDominioEstudiante,
        type: "GET",
        contentType: "application/json",
        dataType: "json"
    }).done(function (datos) {
        //datos.intervencion = true; //TODO: Quitar esto, es de prueba, falla las reglas difusas
        console.log("%cDatos recibidos: %O", "color:blue;", datos);
        callback(datos);
        llenarTabla(datos);
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("Fallo " + textStatus);
    });
}
function llenarTabla(datos){
    var matriz=$("#tablaEjercicios");
    matriz.empty();
    var suma=0;
    matriz.append($("<tr>").append($("<th id=\"prome\" colspan=\"12\">").text("PROM")));
    for (var index = 0; index < datos.length; index++) {
	var dato = datos[index];
        
	var tr = $("<tr>").append($("<th class=\"lec\">").text(dato.nombre));

	var arreglo = dato.ejercicios;
	for(var indexDato = 0; indexDato < arreglo.length; indexDato++ ){
            var tmp=arreglo[indexDato].terminado;
            suma+=tmp;
            switch(tmp){
                //Ejercicio sin responder
                case 0:tr.append(
                    $("<td>")/*.
                    text("")*/
                    );break;
                //Ejercicio terminado de manera óptima
                case 1:tr.append(
                    $("<td style=\"background: green;\">")/*.
                    text("")*/
                    );break;
                //Ejercicio terminado de manera subóptima
                case 0.7: tr.append(
                    $("<td style=\"background: palegreen;\">")/*.
                    text("")*/
                    );break;
            }
            
        }
        var prom=suma/arreglo.length;
        tr.append($("<td style=\"background: LightGray;\">").text((prom*100)+"%"));
        matriz.append(tr);
        suma=0;

    };

}
function menu_sensei() {
    this.lecciones = [];
    this.updateMenu = true;
    menu_context = this;

    this.actualizarMenu = function actualizarMenu() {
        obtenerEjercicios(function (lecciones) {
            menu_context.lecciones = lecciones;
            menu_context.updateMenu = true;
        });
    };
    
    this.getColorFont=function(link, valorTerminado){
        if (valorTerminado == 1) {
            link.css("color", "#21610B");
        }else if(valorTerminado == .7){
            link.css("color","#00FF40");
        }
    }
    
    this.actualizarBoton=function (idEjercicio, valorPaso){
        menu_context.getColorFont($("#ejercicioMenu"+idEjercicio),valorPaso);
    };

    this.getMenu = function getMenu(menuejercicios) { //Debera ser un div el primer argumento
        if (this.updateMenu) {
            var menu = $(menuejercicios);
            //Creacion dinamica del menu
            menu.empty();

            //Creamos el primer div colapsible

            $.each(this.lecciones, function (index, leccion) {
                var divEjercicio = $("<div>")
                        .append(
                                $("<h5>")
                                .text(leccion.nombre)
                                );

                var listaLecciones = $("<ul>");

                //Lista de ejercicios
                $.each(leccion.ejercicios, function (index, ejercicio) {
                    var link = $("<a href='#'>")
                            .attr("id","ejercicioMenu"+ejercicio.id)
                            .text(ejercicio.titulo)
                            .click(cerrarMenu)
                            .click(
                                    {
                                        id: ejercicio.id,
                                        url: ejercicio.url
                                    },
                            cargarEjercicio
                                    );
                    
                    menu_context.getColorFont(link,ejercicio.terminado);
                    
                    listaLecciones.append(
                            $("<li>")
                            .append(link)
                    );
                });

                menu.append(divEjercicio
                        .append(listaLecciones
                                .listview()
                                ).collapsible()
                        );

                menu.find("a").addClass("noWrap");
            });
        }
        this.updateMenu = false;
    };
}