var camera_context = {};

var camera = function (videoId, canvasId, ancho, largo) {
    this.canvas_tag = document.getElementById(canvasId.slice(1));
    this.video_tag = document.getElementById(videoId.slice(1));
    this.ancho = ancho;
    this.largo = largo;
    this.context = {};
    this.limite = 3; //Para modificar la cantidad de fotos a almacenar
    this.milisegundos = 1000; //1000 milisegundos es una foto
    this.id_interval = 0;
    this.url = "emocion/";

    this.tipoCamino = "";
    this.funcionEmocion = function () {};

    this.photo_list = new buckets.LinkedList(); //Almacenamiento FIFO
    this.photo_list.toArrayWithIndex = function () {
        var array = Array();
        var index = 0;

        this.forEach(function (value) {
            array[index] = value;
            index++;
        });

        return array;
    };

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
        //Limpiamos la lista
        clearInterval(camera_context.id_interval);
    },
    setup: function () {

        var videoObj = {video: {mandatory: {minWidth: camera_context.ancho, minHeight: camera_context.largo}}};

        navigator.userMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;

        navigator.userMedia(videoObj, function (stream) { //Connection
            camera_context.video_tag.src = window.URL.createObjectURL(stream);
            camera_context.video_tag.play();

            camera_context.setupCanvas(); //Ajustamos el canvas a la camara
        }, function (error) { //Error en la conexion
            console.log(error);
            alert("Necesita habilitar la camara o usar un navegador con HTML 5");
        });
    },
    setupCanvas: function () {
        var canvas = camera_context.canvas_tag;

        camera_context.context = canvas.getContext("2d");

        canvas.width = camera_context.ancho;
        canvas.height = camera_context.largo;
    },
    snap: function () {

        camera_context.context.drawImage(camera_context.video_tag, 0, 0, camera_context.ancho, camera_context.largo);

        var image = camera_context.canvas_tag.toDataURL("image/jpeg").replace("data:image/jpeg;base64,", "");

        //console.log(image);

        var photo_list = camera_context.photo_list;

        photo_list.add(image);

        if (photo_list.size() > camera_context.limite) {
            photo_list.removeElementAtIndex(0); //Primer item
        }
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
        }).done(function (emocion) {
            //camera_context.funcionEmocion(camera_context.tipoCamino, emocion);
            console.log("%c Emocion recibida:%O","color:white; background:black;",emocion);
            camera_context.funcionEmocion(camera_context.tipoCamino, emocion);
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error("Fallo: " + textStatus);
            console.error("Error: " + errorThrown);
        });
    }
};