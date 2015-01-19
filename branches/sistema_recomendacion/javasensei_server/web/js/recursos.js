function recursos_sensei() {

    this.obtenerEjercicios = function obtenerEjercicios(control) {
        //TODO: Dummy, se deben obtener las recomendaciones del ejercicio
        var ejercicios = [
            {
                titulo: "Ejercicio 1",
                url: "www.google.com"
            },
            {
                titulo: "Ejercicio 1",
                url: "www.google.com"
            }
        ];
        crearLista(ejercicios, control);
    };

    this.obtenerRecursos = function obtenerRecursos(control) {
        //TODO: Es dummy
        var recursos = //Aqui deberia obtenerse todos los recursos
                [
                    {
                        nombre: "Lenguaje",
                        recursos: [
                            {
                                titulo: "Videos basico sobre lenguajes I",
                                url: "www.google.com"
                            },
                            {
                                titulo: "Videos basico sobre lenguajes II",
                                url: "www.google.com"
                            },
                            {
                                titulo: "Videos basico sobre lenguajes III",
                                url: "www.google.com"
                            },
                            {
                                titulo: "Videos basico sobre lenguajes IV",
                                url: "www.google.com"
                            },
                            {
                                titulo: "Videos basico sobre lenguajes V",
                                url: "www.google.com"
                            }
                        ]
                    },
                    {
                        nombre: "Tipo de Dato",
                        recursos: [
                            {
                                titulo: "Tipos de dato Integer",
                                url: "www.google.com"
                            },
                            {
                                titulo: "El complejo pero necesario mundo de las cadenas",
                                url: "www.google.com"
                            },
                            {
                                titulo: "El complejo pero necesario mundo de las cadenas",
                                url: "www.google.com"
                            },
                            {
                                titulo: "El complejo pero necesario mundo de las cadenas",
                                url: "www.google.com"
                            },
                            {
                                titulo: "El complejo pero necesario mundo de las cadenas",
                                url: "www.google.com"
                            }
                        ]
                    }
                ];

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
                                    .attr("href", item.url)
                                    .text(item.titulo)
                                    ));
                });
            });
        }
        
        control.listview("refresh");
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