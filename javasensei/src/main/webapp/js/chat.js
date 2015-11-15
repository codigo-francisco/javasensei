/* global usuario */

var ultimoMensaje = new Date().getTime();
var chatSensei = function () {

    this.verificarMensaje = function () {        
        //Ajax para saber si hay nuevos mensajes
        $.ajax({
            type: "GET",
            url: url + "chat/leermensajes",
            data: {
                fechaActual: ultimoMensaje
            }
        }).done(function(datos){
            if (datos.length>0){
                //Agregamos nuevos mensajes
                $.each(datos,function(index,data){
                    $("#chatbox").prepend($("<p>").text(data.nombreUsuario + ": " + data.message));
                });

                ultimoMensaje = datos[datos.length-1].fecha;
            }
        });
    };

    this.setUp = function () {
        $("#usermsg").keyup(this.procesarEnvioMensaje);
        
        setInterval(this.verificarMensaje, 2500);
    };

    this.procesarEnvioMensaje = function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if (code === 13) {            
            var message = $("#usermsg").val();
            $("#usermsg").val("");
            $("#chatbox").append($("<p>").text(usuario.nombre + ": " + message));

            //Enviar mensaje al servidor
            $.ajax({
                type: "GET",
                url: url + "chat/agregarmensaje",
                data: {
                    message: message,
                    nombreUsuario: usuario.nombre,
                    idUsuario: usuario.id,
                    fecha: new Date().getTime()
                }
            }).done(function(data){
                if (data!="false")
                    ultimoMensaje = data;
            });
        }
    };

};