var tutorial = function (div) {
    var div = div;
    var divs = div.find(".imagenes_tutorial div");
    var totalDiv = divs.length;
    var divActual = 0;
    var currentDiv = $(divs[0]); //Div actual
    var lastDiv = $(divs[0]); //Primer Div

    this.adelanteTutorial = function () {
        if (divActual < totalDiv - 1) {
            cambiarPosicion({data:divActual+1});
        }
    };

    this.atrasTutorial = function () {
        if (divActual > 0) {
            cambiarPosicion({data:divActual-1});
        }
    };

    this.mostrarTutorial = function () {
        $(":mobile-pagecontainer")
                .pagecontainer("change", "#" + div[0].id);
    };
    
    var cambiarPosicion = function(event){
        var index = event.data;
        
        currentButton.removeClass("barraprogreso_tutorial_activo");
        lastDiv = currentDiv;
        lastDiv.hide();
        
        divActual = index;
        
        currentButton = $(barraProgreso.find("button")[divActual]);
        currentButton.addClass("barraprogreso_tutorial_activo");
        currentDiv = $(divs[divActual]);
        currentDiv.show();
    };
    
    //Construir barra de progreso
    var barraProgreso = div.find(".barraprogreso_tutorial");
    for(var index=0; index < totalDiv; index++){
        barraProgreso.append(
            $("<button>")
            .attr("data-role","none")
            .addClass("barraprogreso_boton")
            .click(index, cambiarPosicion)
        );
    }
    
    var currentButton = $(barraProgreso.find("button")[0]);

    //Pasos de configuración

    //Escondemos todos los divs
    divs.hide();
    //Colocamos el primer div
    cambiarPosicion({data:0});
    $(div.find(".atras_tutorial")).click(this.atrasTutorial);
    $(div.find(".adelante_tutorial")).click(this.adelanteTutorial);
};