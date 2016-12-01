function tomarFotografiaRegulador() {
    Webcam.snap(function (dataUrl) {

        var imageOriginal = dataUrl;
        var image = dataUrl.replace("data:image/jpeg;base64,", "");

        $.ajax({
            type: "POST",
            url: "servicios/emociones",
            data: {fotografia: image},
            success: function (response) {
                var emocion = response;
                $("#emocion_text").text(emocion);
                $("#image_rostro").prop("src", imageOriginal);
                delete image;
                delete imageOriginal;

                /*
                 * Habilitar boton de continuar/cerrar,
                 * desactivar fotografias en caso de estar activado
                 */

                console.log(emocion);

                if (emocion == "enganchado" || emocion == "emocionado") {
                    clearInterval(temporizador_regulador);
                    $("#boton_tomarfoto").button("disable");
                    $("#check_repetir").checkboxradio("option", "disabled", true);
                    $("#boton_continuar").button("enable");
                }
            }
        });

    });
}

window.noKeyReturn = function (event) {
    key = event.which;
    console.log("Evento lanzado, teclado capturado, Key: " + event.which);
    if (key == 8 || key == 13) {
        event.preventDefault();
        return false;
    }
};

function iniciar_regulador() {
    //Suponemos que la camara está activada llegado a este punto
    $("video")[1].src = $("video")[0].src;
    
    //Quitamos posible foto previa y emocion previa
    $("#image_rostro").attr("src", "");
    $("#emocion_text").text("");

    temporizador_regulador = -1;

    $(window).bind("keypress", noKeyReturn);

    $("#paginaregulador").show();
    
    tomarFotografiaRegulador();
}

function configurar_regulador() {    
    window.temporizador_regulador = -1;

    $("#slider_intervalos").change(function () {
        if (temporizador_regulador != -1) {
            clearInterval(temporizador_regulador);
            var tiempo = $("#slider_intervalos").val() * 1000;
            temporizador_regulador = setInterval(tomarFotografiaRegulador, tiempo);
        }
    });

    $("#check_repetir").change(function () {
        //Activar o desactivar
        estado = $("#check_repetir").prop("checked");
        if (estado) {
            //Tiempo en milisegundos
            var tiempo = $("#slider_intervalos").val() * 1000;
            //Activar temporizador_regulador
            temporizador_regulador = setInterval(tomarFotografiaRegulador, tiempo);
            //Desactivar boton de toma de fotografias
            $("#boton_tomarfoto").button("disable");
        } else {
            //Desactivar temporizador_regulador
            clearInterval(temporizador_regulador);
            temporizador_regulador = -1;
            //Activar boton de toma de fotografias
            $("#boton_tomarfoto").button("enable");
        }
    });

    $("#boton_tomarfoto").click(tomarFotografiaRegulador);

    //Cerrado del dialogo
    $("#boton_continuar").click(function () {
        $(window).unbind("keypress");
        $("#paginaregulador").hide();
        //Desplegar el tutor
        $(":mobile-pagecontainer").pagecontainer("change", "#tutor_sensei");
    });
}