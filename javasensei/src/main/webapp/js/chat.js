/* global usuario */

var ultimoMensaje = new Date().getTime();
var chatSensei = function () {
    this.idInterval = -1;
    this.verificarMensaje = function () {        
        //Ajax para saber si hay nuevos mensajes
        $.ajax({
            type: "GET",
            url: url + "chat/leermensajes",
            data: {
                fechaActual: ultimoMensaje,
                idEjercicio: avatar_context.id
            }
        }).done(function(datos){
            if (datos.length>0){
                //Agregamos nuevos mensajes
                $.each(datos,function(index,data){
                    $("#chatbox").append(
                            $("<p class='mensaje'>").text(data.nombreUsuario + ": " + data.message)
                            .css("color",data.color));
                    chatbox = document.getElementById("chatbox");
                    chatbox.scrollTop = chatbox.scrollHeight;
                });

                ultimoMensaje = datos[datos.length-1].fecha;
            }
        });
    };
    
    this.changeExercise = function(estado){
        var chatBoton = $("#chatboton");
        if (estado){ //Se habilita y ademas se cambia el id
            chatBoton.removeClass("ui-state-disabled");
            this.idInterval = setInterval(this.verificarMensaje, 500);
        }else{
            chatBoton.addClass("ui-state-disabled");
            clearInterval(this.idInterval);
        }
        $("#chatbox").empty();
    };

    this.setUp = function () {
        $("#usermsg").keyup(this.procesarEnvioMensaje);
    };

    this.procesarEnvioMensaje = function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if (code === 13) {
            var message = $("#usermsg").val();
            var color = $("#botoncolor").css("background-color");
            $("#usermsg").val("");
            $("#chatbox").append(
                    $("<p class='mensaje'>").text(usuario.nombre + ": " + message)
                    .css("color",color));
            chatbox = document.getElementById("chatbox");
            chatbox.scrollTop = chatbox.scrollHeight;
            //Enviar mensaje al servidor
            $.ajax({
                type: "GET",
                url: url + "chat/agregarmensaje",
                data: {
                    message: message,
                    nombreUsuario: usuario.nombre,
                    idUsuario: usuario.id,
                    idEjercicio: avatar_context.id,
                    fecha: new Date().getTime(),
                    color: color
                }
            }).done(function(data){
                if (data!="false")
                    ultimoMensaje = data;
            });
        }
    };

};