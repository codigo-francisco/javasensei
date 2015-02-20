var camera_context = {};

var camera = function () {

    this.limite = 3; //Para modificar la cantidad de fotos a almacenar
    this.milisegundos = 1000; //1000 milisegundos es una foto
    this.id_interval = 0;
    this.url = "emocion/";

    this.tipoCamino = "";
    this.funcionEmocion = function () {
    };

    this.photo_list = new buckets.LinkedList(); //Almacenamiento FIFO

    camera_context = this;
};

camera.prototype = {
    inicializarFotos: function () {
        camera_context.reiniciarFotos();

        //Se toma una foto de acuerdo a los milisegundos marcados
        camera_context.id_interval = setInterval(camera_context.snap, camera_context.milisegundos);
    },
    reiniciarFotos: function () {
        camera_context.photo_list.clear();
    },
    detenerFotos: function () {

        clearInterval(camera_context.id_interval);
        camera_context.reiniciarFotos();
    },
    snap: function () {

        Webcam.snap(function (data) {
            var image = camera_context.canvas_tag.toDataURL("image/jpeg").replace("data:image/jpeg;base64,", "");

            camera_context.photo_list.add(image);

            if (camera_context.photo_list.size() > camera_context.limite) {
                camera_context.photo_list.removeElementAtIndex(0); //Primer item
            }
        });
    },
    setup: function () {
        //Configuracion de la camara
        Webcam.set({
            dest_width: 800,
            dest_height: 600,
            image_format: 'jpeg',
            jpeg_quality: 100,
            force_flash: false
        });

        Webcam.on("live", eliminarBackgroundCamera);

        Webcam.attach('#camera');
    },
    evaluarEmociones: function (callback, datos) {
        var url_emocion = url + camera_context.url + "obteneremocion";

        camera_context.tipoCamino = datos;
        camera_context.funcionEmocion = callback;
        camera_context.detenerFotos();

        $.ajax(url_emocion, {
            data: {
                "fotos": JSON.stringify(camera_context.photo_list.toArray())
            },
            type: 'POST',
            jsonpCallback: "jsonpCallback"
        }).done(function (datos) {
            //camera_context.funcionEmocion(camera_context.tipoCamino, emocion);
            console.log("%c Emocion recibida:%O", "color:white; background:black;", datos);
            usuario.emocionPrevia = usuario.emocionActual;
            usuario.emocionActual = datos.emocion;
            camera_context.funcionEmocion(camera_context.tipoCamino);
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error("Fallo: " + textStatus);
            console.error("Error: " + errorThrown);
        });

        camera_context.reiniciarFotos();
    }
};