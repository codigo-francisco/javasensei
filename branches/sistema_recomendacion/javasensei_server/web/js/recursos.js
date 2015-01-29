function recursos_sensei() {

    function peticionAjaxRecursos(target, callback, control, useUser) {

        var resource = url + target;
        if (useUser) {
            resource += "/" + JSON.stringify(usuario);
        }

        $.ajax({
            url: resource,
            type: "GET",
            contentType: "application/json",
            dataType: "json"
        }).done(function (data) {
            callback(data, control);
        }).fail(function (error) {
            console.error(error);
        });
    }

    this.obtenerEjercicios = function obtenerEjercicios(control) {
        peticionAjaxRecursos("recomendacion/ejercicios", crearLista, control, true);
    };

    this.obtenerRecursos = function obtenerRecursos(control) {
        peticionAjaxRecursos("recursos/todos", crearListaRecursos, control, true);
    };

    this.obtenerIntereses = function obtenerIntereses(control) {
        //TODO: Es dummy
        var enlaces = [
            {titulo: '¿Cuales son los lenguajes de programacion mas usado (2014)?',
                url: "www.google.com"},
            {titulo: 'Android supera en cantidad de desarrolladores a iOS',
                url: 'www.google.com'},
            {titulo: 'El narcicismo y Apple',
                url: 'www.google.com'}
        ];

        crearLista(enlaces, control);
    };

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
                        }).fail(function(error){
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

            configRating(datos.id, datos.ranking);

            //Visualizamos la ventana
            $(":mobile-pagecontainer").pagecontainer("change", "#visor_recursos");
        }).fail(function (error) {
            console.error(error);
        });
    }

    function crearListaRecursos(recursos, control) {
        //Creamos la lista
        var control = $(control);

        if (recursos.length > 0) {
            control.empty();

            $.each(recursos, function (index, item) {
                control.append($("<li>").
                        attr("data-role", "list-divider").
                        text(item.nombre));

                $.each(item.recursos, function (index, item) {
                    control.append($("<li>").
                            append($("<a>")
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
                                    })
                                    ));
                });
            });
        }

        control.listview("refresh");
    }

    function crearLista(items, control) {
        var control = $(control);
        control.empty();

        $.each(items, function (index, item) {
            control.append($("<li>").
                    append($("<a>")
                            .attr("href", item.url)
                            .text(item.titulo)
                            ));
        });

        control.listview("refresh");
    }
}