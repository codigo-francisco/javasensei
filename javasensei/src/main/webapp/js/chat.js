/* global usuario */

var ultimoMensaje;
colaMensajes = new buckets.Queue();
var chatSensei = function () {
    this.idInterval = -1;
    Offline.options = {checks: {xhr: {url: '/favicon.ico'}}};
    this.verificarMensaje = function () {
        if (colaMensajes.size() >= 100)
            colaMensajes.Queue.dequeue();
        //Ajax para saber si hay nuevos mensajes, verifica si hay conectividad
        Offline.check();
        if (Offline.state !== "up")
            return;
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
                    var last = colaMensajes.list.last();
                    var current = data._id.$oid;
                    if (last === current)
                        return;
                    $("#chatbox").append(
                            $("<p class='mensaje'>").html(data.nombreUsuario + ": " + data.message)
                            .css("color",data.color));
                    var chatbox = document.getElementById("chatbox");
                    chatbox.scrollTop = chatbox.scrollHeight;
                });

                ultimoMensaje = datos[datos.length-1].fecha;
                colaMensajes.add(ultimoMensaje);
            }    
        });
    };
    this.changeExercise = function(estado){
        var chatBoton = $("#chatboton");
        if (estado && ultimoMensaje){ //Se habilita y ademas se cambia el id
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
        //Offline.on('up', this.recibirMensaje);
        //Obtenemos la hora del servidor
        obtenerHora();
    };

    this.procesarEnvioMensaje = function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        var message = $("#usermsg").val()
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;");
        if(e.shiftKey && e.keyCode === 13) {
            //Espacio vacío
        } else if (code === 13) {
            enviandoMensaje = true;
            var regex = /[a-z]|[0-9]|[áéíóúñ]|[$-/:-?{-~!"^_`\[\]]/i;
            if (!regex.test(message)){
                $("#usermsg").val("");
                return;
            }
            message = message.replace(/\n/g, "<br/>");
            var color = $("#botoncolor").css("background-color");
            $("#usermsg").val("");
            $("#chatbox").append(
                    $("<p class='mensaje'>").html(usuario.nombre + ": " + message)
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
                    color: color
                }
            }).done(function(data){
                colaMensajes.add(data._id);
                
                var fecha = data.fecha;
                if (fecha>ultimoMensaje)
                    ultimoMensaje = fecha;
            });
        }
    };

};

function obtenerHora(){
    $.get(url+"servidor/obtenerHora")
    .done(function(data){
        ultimoMensaje = data;
    }).fail(function(){
        setTimeout(1000, obtenerHora);
    });
}
