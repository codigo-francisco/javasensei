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
            console.err(error);
        });
    }

    this.obtenerEjercicios = function obtenerEjercicios(control) {
        peticionAjaxRecursos("recomendacion/ejercicios", crearLista, control, true);
    };

    this.obtenerRecursos = function obtenerRecursos(control) {
        peticionAjaxRecursos("recursos", crearListaRecursos, control, false);
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

    function visualizarRecurso(datos) {

        $.ajax({
            url: datos.url,
            type: "GET",
            dataType: "html"
        }).done(function (data) {
            $("#titulo_recurso").text(datos.titulo);
            $("#contenido_recurso").html(data);
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
                                    .text(item.titulo)
                                    .click(function () {
                                        var control = $(this);
                                        visualizarRecurso({
                                            titulo: control.text(),
                                            url: control.attr("data-url")
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