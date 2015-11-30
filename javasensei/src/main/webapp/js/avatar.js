/* global menu_context, usuario, camera_sensei, example_tracing, tree_self, contexto, detectorEmocional */

var avatar_context;
var avatar_sensei = function avatar_sensei(avatar_control) {
    this.avatar_control = $(avatar_control);
    this.avatar_datos = {};
    this.id = 0;
    this.url = "servicios/estrategiatutor/";
    this.es_primera_carga = false;
    this.instrucciones_ejercicio = [];
    this.ultimos_datos = {};
    this.sePuedeIntervencion = true;
    this.idTimeout = 0;
    this.bitacoras = new Array();

    avatar_context = this;
};

function obtenerRecomendacionesTutor(idEjercicio, datos, callback) { //Objeto del usuario y el ejercicio actual

    $.ajax({
        url: "servicios/recursos/recomendaciones",
        data: {
            idEjercicio: idEjercicio,
            idAlumno: usuario.id
        },
        dataType: "json"
    }).done(function (data) {
        datos.opciones = data;
        callback(datos);
    }).error(function (data) {
        console.error("Error en la obtencion de recomendaciones: %0", data);
    });
}

var segundos = 0;
var segundero;
function iniciarSegundero(){
    if (segundero)
        detenerSegundero();
    segundos = 0;
    segundero = setInterval(function(){
        segundos++;
    },1000);
}

function detenerSegundero(){
    clearInterval(segundero);
}

avatar_sensei.prototype = {
    llamarInformacion: function(expresion){
        var datos_avatar = {
            emocion: expresion,
            texto: avatar_context.instrucciones_ejercicio,
            opciones: [] //Sin opciones es solamente las instrucciones y la emocion
        };
        
        avatar_context.crearAvatar(datos_avatar);
        avatar_context.mostrarAvatar();
    },
    mostrarInformacion: function () {
        avatar_context.llamarInformacion(avatar_context.ultimos_datos.emocion);
    },
    primera_carga: function (data) { //Notificacion de que se esta cargando el ejercicio (diferente al paso inicial)
        camera_sensei.detenerFotos();
        iniciarSegundero();
        avatar_context.bitacoras = new Array();
        avatar_context.es_primera_carga = true;
        camera_sensei.inicializarFotos();
        avatar_context.sePuedeIntervencion = true;
        avatar_context.instrucciones_ejercicio = data.instruccionesejercicio;
    },
    inicio_ejercicio: function (datos) {
        console.log("Paso Inicial notificado, ID: " + datos.id);
        avatar_context.id = datos.id; //Id del ejercicio actual
        if (avatar_context.es_primera_carga) {
            avatar_context.es_primera_carga = false;
            $.ajax({
                url: avatar_context.url + "emociontutor",
                type: "POST",
                data: {
                    datosestudiante: JSON.stringify(usuario)
                },
                dataType: "json"
            }).done(function (datos) {
                avatar_context.llamarInformacion(datos.expresion);
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.error("Fallo: " + textStatus);
            });
        }
    },
    procesarBitacora: function(datos,tipoCamino){
        //Agregamos un nuevo objeto a la bitacora
        avatar_context.bitacoras.push({
            idAlumno: usuario.id,
            emocion: datos.emocion,
            aciertos: tree_self.obtenerTotalAciertos(),
            errores: tree_self.obtenerTotalErrores(),
            promedioErrores: tree_self.obtenerCalidadRespuesta(),
            promedioAciertos: tree_self.obtenerCalidadAcierto(),
            totalErrores: tree_self.obtenerTotalPasosErrores(),
            totalAciertos: tree_self.obtenerTotalPasosAciertos(),
            ejercicioId: avatar_context.id,
            pasoId: tree_self.pasoactual,
            fecha: new Date().toISOString(),
            tipoPaso: contexto.tipoPaso,
            segundos: segundos,
            fotografias : JSON.parse(camera_sensei.getUltimasFotografias())
        });
        if (tipoCamino=="caminofinaloptimo" ){ 
            avatar_context.cierreEjercicio(1);
        }else if (tipoCamino == "caminofinalsuboptimo"){
            avatar_context.cierreEjercicio(0.7);
        }
    },
    ejecutarAjax: function (tipoCamino, fotografias) {
            //Se hace una solicitud rest al servidor java en caso de que el detecto este activado
            $.ajax({
                url: avatar_context.url + tipoCamino,
                type: "POST",
                beforeSend:function(jqXHR,settings){
                    $("#fullscreenloading").show();
                },
                data: {
                    datosestudiante: JSON.stringify(usuario),
                    fotos: fotografias,
                    detector:detectorEmocional
                },
                dataType: "json"
            }).always(function (datos) {
                $("#fullscreenloading").hide();
                console.log("%cDatos recibidos: %O", "color:blue;", datos);
                avatar_context.intervencion(datos);
                if (usuario.activarEmociones)
                    avatar_context.procesarBitacora(datos,tipoCamino);
                else{
                    datos.emocion = "sinemocion";
                    avatar_context.procesarBitacora(datos,tipoCamino);
                }
            });
    },
    llamarSistemaLogicoDifuso: function (tipoCamino) {
        usuario.calidadRespuesta = example_tracing.obtenerCalidadRespuesta();
        var fotografias = camera_sensei.getFotografias();
        camera_sensei.reiniciarFotos();
        avatar_context.ejecutarAjax(tipoCamino, fotografias);
        $("#progressbar > div").css({"background":"#3C3"});
    },
    paso_suboptimo: function () {
        console.log("Paso suboptimo notificado");
        avatar_context.llamarSistemaLogicoDifuso("caminosuboptimo");
        camera_sensei.inicializarFotos();
        $("#progressbar > div").css({"background":"#3C3"});
    },
    paso_optimo: function () {
        console.log("Paso optimo notificado");
        avatar_context.llamarSistemaLogicoDifuso("caminooptimo");
        camera_sensei.inicializarFotos();
        $("#progressbar > div").css({"background":"#3C3"});
    },
    paso_erroneo: function () {
        console.log("Paso erroneo notificado");
        avatar_context.llamarSistemaLogicoDifuso("caminoerroneo");
        camera_sensei.inicializarFotos();
        $("#progressbar > div").css({"background":"#FA0"});
    },
    paso_final_suboptimo: function () {
        console.log("Paso suboptimo notificado");
        avatar_context.llamarSistemaLogicoDifuso("caminofinaloptimo");
        $("#progressbar > div").css({"background":"#3C3"});
    },
    paso_final_optimo: function () {
        console.log("Paso final optimo notificado");
        avatar_context.llamarSistemaLogicoDifuso("caminofinaloptimo");
    },
    paso_siguiente: function () {
        console.log("paso adelante llamado");
        matrizEjercicios = $.map(contexto.tree_example_tracing.matriz,function(el){
            return el;
        });
        pasoActual = contexto.tree_example_tracing.pasoactual;;
        if (matrizEjercicios[pasoActual].tipo === "pasoerroneo")
            $("#progressbar > div").css({"background":"#FA0"});
    },
    paso_atras: function () {
        console.log("paso atras llamado");
        $("#progressbar > div").css({"background":"#3C3"});
    },
    cierreEjercicio: function (valor_paso) { //Funcion que se llama cuando se finaliza los ejercicios
        //Logica de timers
        camera_sensei.detenerFotos();
        clearTimeout(avatar_context.idTimeout);
        detenerSegundero();

        //Logica de controles
        contexto.controles.hide();
        contexto.progressbar.hide();
        contexto.cierretracing.show();
        
        //Mostramos el rating
        avatar_context.obtenerRatingEjercicio(avatar_context.id);

        //Ajax para cambiar el estado del ejercicio
        $.ajax({
            url: "servicios/estudiantes/finalizarEjercicio",
            type: "GET",
            data: {
                idEjercicio: avatar_context.id,
                idAlumno: usuario.id,
                valor: valor_paso
            },
            contentType: "application/json",
            dataType: "json"
        }).done(function (data) {
            console.log(data);
            menu_context.actualizarBoton(avatar_context.id, valor_paso);
            avatar_context.id=0; //Se reinicia el id
        }).error(function (error) {
            console.error(error);
        });
        
        //Guardado de la bitacora
        $.ajax({
            url: "servicios/bitacora/guardarbitacoras",
            type: "POST",
            data: {
                logBitacoras : JSON.stringify(avatar_context.bitacoras)
            }
        }).done(function (data) {
            console.log("Bitacora guardada:%O", data);
        }).error(function (error) {
            console.error(error);
        });
    },
    obtenerRatingEjercicio: function (idEjercicio) {
        $.ajax({
            url: "servicios/recursos/getrankingejercicio",
            data: {
                idEjercicio: idEjercicio,
                idAlumno: usuario.id
            },
            dataType: "json"
        }).done(function (data) {
            avatar_context.setRatingEjercicio(data.id, data.ranking);
        }).fail(function (error) {
            console.error(error);
        });
    },
    setRatingEjercicio: function (id, ranking) {
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
                        }).fail(function (error) {
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
        
        if (datos.emocion!=""){ 
            image_control.show();
            //Obtenemos la imagen de la emocion
            var image = "img/avatar/" + datos.emocion + ".jpg"; //Las imagenes estan en ese formato
            image_control.attr("src", image);
        }else{ //No hay imagen porque no esta el detector activado
            image_control.hide();
        }

        //Obtenemos el control del texto y opciones del avatar
        var texto_avatar = control.find("#texto_avatar");
        texto_avatar.empty();
        //Colocamos el texto
        texto_avatar.append(
                $("<span>").html(datos.texto.join("<br><br>")));


        var opciones_avatar = control.find("#opciones_avatar");
        opciones_avatar.empty();
        if (datos.opciones.length > 0) {
            $("#recomendaciones").show();
            //creamos la lista
            $.each(datos.opciones, function (index, opcion) {
                //Creamosla opcion en la lista
                var lista = $("<li>").append(
                        $("<a>")
                        .text(opcion.titulo)
                        .on("click", {
                            opciones: opcion
                        }, function (event) {
                            var opciones = event.data.opciones;
                            visualizarRecurso(opciones);
                        }
                        )
                );
                opciones_avatar.append(lista);
            });

            opciones_avatar.listview("refresh");
        } else {
            $("#recomendaciones").hide();
        }

        avatar_context.ultimos_datos = datos;
    },
    intervencion: function (datos) {
        if (avatar_context.sePuedeIntervencion && datos.intervencion && !datos.opciones) {
            ////Hay que obtener las recomendaciones... y modificar la propiedad opciones
            obtenerRecomendacionesTutor(avatar_context.id, datos, avatar_context.intervencion);
            return;
        }

        if (avatar_context.sePuedeIntervencion && datos.intervencion) { //Se realiza una intervencion
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

            avatar_context.sePuedeIntervencion = false;
            avatar_context.idTimeout = setTimeout(function () {
                avatar_context.sePuedeIntervencion = true;
            }, 55000);
        }
    }
};