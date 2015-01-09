function obtenerRecomendacionesTutor(ejercicio) { //Objeto del usuario y el ejercicio actual

    //TODO: Dummy con las opciones
    return [
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
        ];
}