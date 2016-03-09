var tutorial = function (div) {
    var div = div;
    var divs = div.find(".imagenes_tutorial div");
    var totalDiv = divs.length;
    var divActual = 0;

    this.adelanteTutorial = function () {
        if (divActual < totalDiv - 1) {
            $(divs[divActual]).hide();
            divActual++;
            $(divs[divActual]).show();
        }
    };

    this.atrasTutorial = function () {
        if (divActual > 0) {
            $(divs[divActual]).hide();
            divActual--;
            $(divs[divActual]).show();
        }
    };

    this.mostrarTutorial = function () {
        $(":mobile-pagecontainer")
                .pagecontainer("change", "#" + div[0].id);
    };

    //Pasos de configuración

    //Escondemos todos los divs
    divs.hide();
    //Habilitamos el primer div
    $(divs[0]).show();
    $(div.find(".atras_tutorial")).click(this.atrasTutorial);
    $(div.find(".adelante_tutorial")).click(this.adelanteTutorial);
};