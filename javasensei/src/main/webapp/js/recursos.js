function recursos_sensei() {

    function peticionAjaxRecursos(target, callback, control) {

        var resource = url + target;

        $.ajax({
            url: resource,
            type: "POST",
            data:{
                usuario: JSON.stringify(usuario)
            },
            dataType: "json"
        }).done(function (data) {
            callback(data, control);
        }).fail(function (error) {
            console.error(error);
        });
    }

    this.obtenerEjercicios = function obtenerEjercicios(control) {
        peticionAjaxRecursos("recomendacion/ejercicios", crearListaEjercicios, control);
    };

    this.obtenerRecursos = function obtenerRecursos(control) {
        peticionAjaxRecursos("recursos/todos", crearListaRecursos, control);
    };
    
    function crearListaRecursos(recursos, control) { //Debera ser un div el primer argumento
        
        //Creacion dinamica de la lista de recursos
        var control = $(control);
        if (recursos.length > 0) {
            control.empty();
            
            //Creamos el primer div colapsible
            $.each(recursos, function (index, item) {
                var divRecursos = $("<div>")
                        .append(
                                $("<h3>")
                                . text(item.nombre)
                                );
                
                var listaRecursos = $("<ul>");
                //Lista de recursos
                $.each(item.recursos, function (index, item) {
                    var link = $("<a href='#'>")
                                    .attr("data-url", item.url)
                                    .attr("data-id", item.id)
                                    .attr("data-ranking", item.ranking)
                                    .text(item.titulo)
                                    .click(function () {
                                        var control = $(this);
                                        visualizarRecurso({
                                            titulo: control.text(),
                                            url: control.attr("data-url"),
                                            id: control.attr("data-id"),
                                            ranking: control.attr("data-ranking")
                                        });
                                    });
                        listaRecursos.append(
                            $("<li>")
                            .append(link)
                        );
                });
                control.append(divRecursos
                        .append(listaRecursos
                                .listview()
                                ).collapsible()
                        );
            });
        }
    }

    function crearListaEjercicios(items, control) {
        var control = $(control);
        control.empty();

        $.each(items, function (index, item) {
            control.append($("<li>").
                    append($("<a>")
                            .attr("href", item.url)
                            .text(item.titulo)
                            .bind("click",
                                    {
                                        id: item.id,
                                        url: item.url
                                    },
                            cargarEjercicio
                                    )
                            )
                    );
        });

        control.listview("refresh");
    }
}

function configRating(id, ranking) {
        var calificaciones = $("#calificaciones");
        calificaciones.empty();
        calificaciones.append(
                $("<div>")
                .attr("data-id", id)
                .attr("data-average", ranking)
                .jRating({
                    showRateInfo: false,
                    step: true,
                    rateMin: 1,
                    rateMax: 5,
                    canRateAgain: true,
                    nbRates: 9999999,
                    sendRequest: false,
                    onClick: function (element, rate) {
                        //Se manda a guardar el ranking
                        $.ajax({
                            url: "servicios/recursos/setranking",
                            data: {
                                idRecurso: element.dataset["id"],
                                idAlumno: usuario.id,
                                ranking: rate
                            },
                            dataType: "json"
                        }).done(function (data) {
                            configRating(data.id, data.ranking);
                        }).fail(function (error) {
                            console.error(error);
                        });
                    }
                })
                );
    }

function visualizarRecurso(datos) {
    $.ajax({
        url: "recursos/" + datos.url, //Recursos es una carpeta por default
        type: "GET",
        dataType: "html"
    }).done(function (data) {
        $("#titulo_recurso").text(datos.titulo);
        $("#contenido_recurso").html(data);
        prettyPrint();
        configRating(datos.id, datos.ranking);
        //Visualizamos la ventana
        $(":mobile-pagecontainer").pagecontainer("change", "#visor_recursos",{ transition: "flip" });
    }).fail(function (error) {
        console.error(error);
    });
}