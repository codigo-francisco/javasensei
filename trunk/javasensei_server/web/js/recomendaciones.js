function obtenerRecomendacionesTutor(idEjercicio, datos, callback) { //Objeto del usuario y el ejercicio actual

    $.ajax({
        url:"servicios/recursos/recomendaciones",
        data:{
            idEjercicio : idEjercicio,
            idAlumno : usuario.id
        },
        dataType : "json"
    }).done(function(data){
        datos.opciones = data;
        callback(datos);
    }).error(function(data){
        console.error("Error en la obtencion de recomendaciones: %0", data);
    });

    //TODO: Dummy con las opciones
    /*return [
            {
                tipo: "cambio",
                texto: ["Prueba este ejercicio, es sobre variables"],
                url: "ejercicio1/practica1.json"
            },
            {
                tipo: "recurso_enlace",
                texto: ["Esta pagina puede ayudarte"],
                url: "http://www.google.com/"
            },
            {
                tipo: "recurso_video",
                texto: ["Este video sobre variables puede ayudarte"],
                url: "http://www.google.com/"
            }
        ];*/
}