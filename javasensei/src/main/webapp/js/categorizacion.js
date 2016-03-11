function enlazarBotonesCarousel() {
    $("#adelante_categorizar").click(adelanteCarousel);
    $("#atras_categorizar").click(atrasCarousel);
    $("#categorizar img").click(seleccionarEmocion);
}

function iniciarCategorizacion() {
    $("#categorizar img").css("background-color", "");

    //Obtener fotografias
    $.post(url + "bitacora/obtenerbitacoracategorizacion",
            {idUsuario: usuario.id},
            function (result) {
                //Por alguna razon jQuery no lo esta haciendo automaticamente
                result = JSON.parse(result);

                if (result.length > 0) {
                    crearCarousel(result);
                    //Inicializar la categorización por parte del usuario
                    $(":mobile-pagecontainer").pagecontainer("change", "#paginacategorizacion");
                }
            }
    );
}

var cantidadFotos = 0;
var indiceCarousel = 1;

function crearCarousel(datos) {

    var divFotografias = $("#div_categorizar");
    divFotografias.empty();
    var count = 1;
    cantidadFotos = 0;
    indiceCarousel = 0;

    //Creacion de images en html
    for (var objeto in datos) {
        objeto = datos[objeto];
        //Agregamos los img
        divFotografias.append(
            $("<img>")
                .prop("id", "img" + count)
                .prop("src", "data:image/jpeg;base64," + objeto.fotografia)
                .prop("_id", objeto._id)
                .prop("idEmocion", "")
                .css("display", "none")
        );

        cantidadFotos++;
        count++;
    }

    //Colocamos la primer fotografia
    if (cantidadFotos > 0) {
        indiceCarousel = 1;
        $("#img1").css("display", "block");
    }
}

function todasCategorizadas() {
    //Verificar que se haya categorizado todas las imagenes
    var fotos = $("#div_categorizar img");

    for (var index = 0; index < fotos.length; index++) {
        obj = $(fotos[index]);
        console.log(obj.prop("idEmocion") !== "");
        if (obj.prop("idEmocion") == "") {
            return false;
        }
    }
    
    return true;
}

function seleccionarEmocion(event) {
    var id = event.currentTarget.id;
    var fotografia = $("#img" + indiceCarousel);
    //Guardamos emocion seleccionada en la foto
    fotografia.prop("idEmocion", id);

    var idFotografia = fotografia.prop("_id");
    //Enviamos el id para que sea categorizada la fotografia
    $.get(url + "bitacora/categorizarfotografiausuario",
            {
                "idFotografia": idFotografia.$oid,
                "idUsuario": usuario.id,
                "emocion": id
            },
            function (result) {
                console.log("Categorizacion completa");
            }
    );

    resaltarEmocion(fotografia);
    //Si ya estan todas categorizadas, se cierra el cuadro de dialogo
    if (todasCategorizadas()) {
        $.mobile.back();
    } else {
        adelanteCarousel();
    }
}

function resaltarEmocion(fotografia) {
    //Verificamos que haya una emoción
    var id = fotografia.prop("idEmocion");

    //Eliminamos los fondos
    $("#categorizar img").css("background-color", "");

    if (id) {
        //Se establece en la imagen correcta
        $("#" + id).css("background-color", "blue");
    }
}

function adelanteCarousel() {
    //Si el siguiente indice no causa desbordamiento
    if (indiceCarousel + 1 <= cantidadFotos) {
        $("#img" + indiceCarousel).css("display", "none"); //Foto actual
        $("#img" + (indiceCarousel + 1)).css("display", "block"); //Siguiente foto
        indiceCarousel++; //Aumenta el indice

        resaltarEmocion($("#img" + indiceCarousel));
    }
}

function atrasCarousel() {
    //Si el siguiente indice no causa desbordamiento
    if (indiceCarousel - 1 > 0) {
        $("#img" + indiceCarousel).css("display", "none"); //Foto actual
        $("#img" + (indiceCarousel - 1)).css("display", "block"); //Foto anterior
        indiceCarousel--; //Disminuye el indice

        resaltarEmocion($("#img" + indiceCarousel));
    }
}