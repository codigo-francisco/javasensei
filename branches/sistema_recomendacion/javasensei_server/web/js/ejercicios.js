function obtenerInstruccion(id) {
    //TODO: se obtiene las instrucciones iniciales para un ejercicio

    return {
        emocion: "neutral",
        texto: ["Las variables son una de las características fundamentales de los lenguajes de programación, permiten acceder a la memoria para almacenar y recuperar los datos con los que nuestros programas van a trabajar. Son por tanto el mecanismo que los lenguajes de programación ponen a nuestra disposición para acceder a la memoria."
                    , "Se trata de un mecanismo de lo más sencillo, sólo tenemos que dar un nombre a nuestras variables, a partir de ese momento el compilador traducirá de forma automática ese nombre en un acceso a memoria. Por ejemplo:"
                    , "<code>//Almacenamos un dato en memoria referenciado por el nombre edad "
                    , "edad = 5;"
                    , "//Recuperamos el dato almacenado y lo modificamos"
                    , "edad = edad + 1;</code>"],
        opciones: []
    };
}
function obtenerEjercicios(callback) {
    //Se hace una solicitud rest al servidor java
    var urlDominioEstudiante = url + "dominioEstudiante/getDominioEstudiante/" + usuario.id;

    $.ajax({
        url: urlDominioEstudiante,
        type: "GET",
        contentType: "application/json",
        dataType: "json"
    }).done(function (datos) {
        //datos.intervencion = true; //TODO: Quitar esto, es de prueba, falla las reglas difusas
        console.log("%cDatos recibidos: %O", "color:blue;", datos);
        callback(datos);
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("Fallo " + textStatus);
    });
}
