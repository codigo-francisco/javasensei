var menu_context;
function menu_sensei(idUsuario) {
    this.ejercicios = [];
    this.updateMenu = true;
    menu_context = this;

    this.actualizarMenu = function actualizarMenu() {
        obtenerEjercicios(function(ejercicios){
            menu_context.ejercicios = ejercicios;
            menu_context.updateMenu = true;
        });
    };

    this.getMenu = function getMenu(menuejercicios) { //Debera ser un div el primer argumento
        if (this.updateMenu) {
            var menu = $(menuejercicios);
            //Creacion dinamica del menu
            menu.empty();

            //Creamos el primer div colapsible

            $.each(this.ejercicios.lecciones, function(index, ejercicio) {
                var divEjercicio = $("<div></div>")
                        //.attr("data-role","collapsible")
                        //.attr("data-inset",false)
                        .append($("<h3></h3>")
                                .text(ejercicio.titulo)
                                );

                var listaLecciones = $(document.createElement("ul"));

                //Lista de lecciones
                $.each(ejercicio.lecciones, function(index, leccion) {
                    listaLecciones.append(
                            $("<li>")
                                .append($("<a href='#'>")
                                .text(leccion.titulo)
                                .click(function(){
                                    //Cerrar el panel
                                    $( "#panelmenu" ).panel( "close" );
                                })
                                .click(
                                    {
                                        id: leccion.id,
                                        url:leccion.url
                                    },
                                    cargarEjercicio
                                )
                            )
                    );
                });

                menu.append(divEjercicio
                        .append(listaLecciones
                                .listview()
                                ).collapsible()
                        );
            });
        }
        this.updateMenu = false;
    };
}