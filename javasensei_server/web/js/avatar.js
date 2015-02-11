var avatar_context;
var avatar_sensei = function avatar_sensei(avatar_control) {
    this.avatar_control = $(avatar_control);
    this.avatar_datos = {};
    this.id = 0;
    this.url = "estrategiatutor/";
    this.es_primera_carga = false;
    this.instrucciones_ejercicio = [];
    avatar_context = this;
};
avatar_sensei.prototype = {
    ejecutarAjax: function (tipoCamino) {
        //Se hace una solicitud rest al servidor java        
        var urlAjax = url + avatar_context.url + tipoCamino;
        urlAjax += "/" + JSON.stringify(usuario);

        $.ajax({
            url: urlAjax,
            type: "GET",
            contentType: "application/json",
            dataType: "json"
        }).done(function (datos) {
            //datos.intervencion = true; //TODO: Quitar esto, es de prueba, falla las reglas difusas
            console.log("%cDatos recibidos: %O", "color:blue;", datos);
            avatar_context.intervencion(datos);
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.log("Fallo " + textStatus);
        });
    },
    solicitud_usuario: function () {

    },
    primera_carga: function (data) { //Notificacion de que se esta cargando el ejercicio (diferente al paso inicial)
        camera_sensei.inicializarFotos();
        avatar_context.es_primera_carga = true;
        avatar_context.instrucciones_ejercicio = data.instruccionesejercicio;
    },
    inicio_ejercicio: function (datos) {
        console.log("Paso Inicial notificado, ID: " + datos.id);
        avatar_context.id = datos.id; //Id del ejercicio actual
        if (avatar_context.es_primera_carga) {
            avatar_context.es_primera_carga = false;
            //Aqui se esperaria la intervencion del tutor para explicar el ejercicio.
            //La construccion de los ejercicios en el caso de las instrucciones distinta a la de los demas, aqui solo
            //Se obtiene la informacion para mostrar al usuario

            var urlEmocion = url + avatar_context.url + "emociontutor/" + JSON.stringify(usuario);

            $.ajax({
                url: urlEmocion,
                type: "GET",
                contentType: "application/json",
                dataType: "json"
            }).done(function (datos) {
                var datos_avatar = {
                    emocion: datos.expresion,
                    texto: avatar_context.instrucciones_ejercicio,
                    opciones: [] //Sin opciones es solamente las instrucciones y la emocion
                };

                avatar_context.crearAvatar(datos_avatar);
                avatar_context.mostrarAvatar();

                camera_sensei.inicializarFotos();
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.error("Fallo: " + textStatus);
            });

        }

    },
    llamarSistemaLogicoDifuso: function (tipoPaso) {
        usuario.calidadRespuesta = example_tracing.obtenerCalidadRespuesta();
        avatar_context.ejecutarAjax(tipoPaso);
    },
    paso_suboptimo: function () {
        console.log("Paso suboptimo notificado");
        camera_sensei.evaluarEmociones(avatar_context.llamarSistemaLogicoDifuso, "caminosuboptimo");
    },
    paso_optimo: function () {
        console.log("Paso optimo notificado");
        camera_sensei.evaluarEmociones(avatar_context.llamarSistemaLogicoDifuso, "caminooptimo");
    },
    paso_erroneo: function () {
        console.log("Paso erroneo notificado");
        camera_sensei.evaluarEmociones(avatar_context.llamarSistemaLogicoDifuso, "caminoerroneo");
    },
    paso_final_suboptimo: function () {
        console.log("Paso suboptimo notificado");
        camera_sensei.evaluarEmociones(avatar_context.llamarSistemaLogicoDifuso, "caminofinaloptimo");
        avatar_context.cierreEjercicio();
    },
    paso_final_optimo: function () {
        console.log("Paso final optimo notificado");
        camera_sensei.evaluarEmociones(avatar_context.llamarSistemaLogicoDifuso, "caminofinalsuboptimo");
        avatar_context.cierreEjercicio();
    },
    paso_siguiente: function () {

    },
    paso_atras: function () {

    },
    cierreEjercicio: function () { //Funcion que se llama cuando se finaliza los ejercicios
        $("#controles_tracing").hide();
        camera_sensei.detenerFotos();
        //Mostramos el rating
        $("#controles_cierre_tracing").show();
        
        //Ajax para cambiar el estado del ejercicio
        $.ajax({
            url: "servicios/estudiantes/finalizarEjercicio",
            type: "GET",
            data:{
                idEjercicio : avatar_context.id,
                idAlumno : usuario.id
            },
            contentType: "application/json",
            dataType: "json"
        }).done(function(data){
            console.log(data);
        }).error(function(error){
           console.error(error); 
        });
    },
    obtenerRatingEjercicio : function(idEjercicio){
        $.ajax({
            url:"servicios/recursos/getrankingejercicio",
            data:{
                idEjercicio : idEjercicio,
                idAlumno : usuario.id
            },
            dataType:"json"
        }).done(function(data){
            avatar_context.setRatingEjercicio(data.id, data.ranking);
        }).fail(function(error){
            console.error(error);
        });
    },
    setRatingEjercicio : function(id, ranking){
        var calificaciones = $("#calificaciones_ejercicios");
        calificaciones.empty();
        calificaciones.append(
                $("<div>")
                .attr("data-id", id)
                .attr("data-average", ranking)
                .jRating({
                    showRateInfo: false,
                    step: true,
                    rateMin: 1,
                    rateMax: 5,
                    canRateAgain: true,
                    nbRates: 9999999,
                    sendRequest: false,
                    onClick: function (element, rate) {
                        //Se manda a guardar el ranking
                        $.ajax({
                            url: "servicios/recursos/setrankingejercicio",
                            data: {
                                idEjercicio: element.dataset["id"],
                                idAlumno: usuario.id,
                                ranking: rate
                            },
                            dataType: "json"
                        }).done(function (data) {
                            avatar_context.setRatingEjercicio(data.id, data.ranking);
                        }).fail(function(error){
                            console.error(error);
                        });
                    }
                })
                );
    },
    mostrarAvatar: function () {
        $(":mobile-pagecontainer").pagecontainer("change", "#tutor_sensei");
    },
    crearAvatar: function (datos) {
        //El avatar se crea de acuerdo al contenido en avatar_datos
        var control = avatar_context.avatar_control;

        //Obtenemos el control de la imagen
        var image_control = control.find("#image_avatar");
        //Obtenemos la imagen de la emocion
        var image = "img/avatar/" + datos.emocion + ".jpg"; //Las imagenes estan en ese formato
        image_control.attr("src", image);

        //Obtenemos el control del texto y opciones del avatar
        var texto_avatar = control.find("#texto_avatar");
        texto_avatar.empty();
        //Colocamos el texto
        texto_avatar.append(
                $("<span>").html(datos.texto.join("<br><br>")));


        var opciones_avatar = control.find("#opciones_avatar");
        opciones_avatar.empty();
        if (datos.opciones.length > 0) {
            //creamos la lista
            $.each(datos.opciones, function (index, opcion) {
                //Creamosla opcion en la lista
                var lista = $("<li>").append(
                        $("<a>")
                        .text(opcion.texto)
                        .on("click", {
                            opciones: opcion
                        },
                        function (event) {
                            var opciones = event.data.opciones;
                            visualizarRecurso(opciones);
                            //event.data.funcion(event.data.url);
                        }
                        )
                        );
                opciones_avatar.append(lista);
            });

            opciones_avatar.listview("refresh");
        }
    },
    intervencion: function (datos) {
        if (!datos.opciones) {
            ////Hay que obtener las recomendaciones... y modificar la propiedad opciones
            obtenerRecomendacionesTutor(avatar_context.id, datos, avatar_context.intervencion);
            return;
        }

        if (datos.intervencion) { //Se realiza una intervencion
            var datos_nuevos = {};

            datos_nuevos.opciones = datos.opciones;
            //De acuerdo a los datos se realizara un formato para el metodo crearAvatar
            //La imagen se va a obtener de acuerdo a la expresion emocional
            datos_nuevos.emocion = datos.expresion;
            //Obtenemos los textos de retroalimentacion y motivacional
            var respuesta = example_tracing.obtenerTextoEmocional(datos.expresion, datos.retroalimentacion);
            //Ambas respuesta se dejan en un array ya que el tutor unira los textos formar el texto del tutor
            datos_nuevos.texto = [
                respuesta.motivacional,
                respuesta.retroalimentacion
            ];

            //Se envia todo al tutor para que forme el avatar
            avatar_context.crearAvatar(datos_nuevos);
            avatar_context.mostrarAvatar();
        }
    }
};