var tutorial = function (div, conf) {
    var parentDialog = div.parent().parent();
    var div = div;
    var divs = div.find(".imagenes_tutorial div");
    var totalDiv = divs.length;
    var divActual = 0;
    var currentDiv = $(divs[0]); //Div actual
    var lastDiv = $(divs[0]); //Primer Div
    var urlPrimeraVez = conf.urlPrimeraVez;
    var parametroUrl = conf.parametroUrl;

    this.adelanteTutorial = function () {
        if (divActual < totalDiv - 1) {
            cambiarPosicion({data: divActual + 1});
        }
    };

    this.atrasTutorial = function () {
        if (divActual > 0) {
            cambiarPosicion({data: divActual - 1});
        }
    };

    this.mostrarTutorial = function (primera) {

        if (primera) { //Deshabilitar el boton de salida
            $.post(url + urlPrimeraVez,
                    {
                        json: JSON.stringify(parametroUrl)
                    },
                    function (result) {
                        console.log("Se guardo que el usuario ya vió el tutorial");
                    }
            );
        }

        $(":mobile-pagecontainer")
                .pagecontainer("change", "#" + parentDialog.prop("id"));
    };

    var cambiarPosicion = function (event) {
        var index = event.data;

        currentButton.removeClass("barraprogreso_tutorial_activo");
        lastDiv = currentDiv;
        
        lastDiv.hide("slide", function () {
            divActual = index;

            currentButton = $(barraProgreso.find("button")[divActual]);
            currentButton.addClass("barraprogreso_tutorial_activo");
            
            currentDiv = $(divs[divActual]);
            currentDiv.show("slide",250);
            
        },250);
    };

    //Construir barra de progreso
    var barraProgreso = div.find(".barraprogreso_tutorial");
    for (var index = 0; index < totalDiv; index++) {
        barraProgreso.append(
                $("<button>")
                .attr("data-role", "none")
                .addClass("barraprogreso_boton")
                .click(index, cambiarPosicion)
                );
    }

    var currentButton = $(barraProgreso.find("button")[0]);

    //Pasos de configuración

    //Escondemos todos los divs
    divs.hide();
    //Colocamos el primer div
    cambiarPosicion({data: 0});
    $(div.find(".atras_tutorial")).click(this.atrasTutorial);
    $(div.find(".adelante_tutorial")).click(this.adelanteTutorial);
};