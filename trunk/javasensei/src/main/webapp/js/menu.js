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
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("Fallo " + textStatus);
    });
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
    
    this.actualizarBoton=function (idEjercicio){
        $("#ejercicioMenu"+idEjercicio).css("color","green");
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

                    if (ejercicio.terminado == 1) {
                        link.css("color", "green");
                    }

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