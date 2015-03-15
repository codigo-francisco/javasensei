var camera_context = {};

var camera = function () {

    this.limite = 2; //Para modificar la cantidad de fotos a almacenar
    this.milisegundos = 1500; //1000 milisegundos es una foto
    this.id_interval = 0;
    this.url = "emocion/";

    this.tipoCamino = "";
    this.funcionEmocion = function () {
    };

    //this.photo_list = new Array(); //Almacenamiento FIFO
    localStorage.setItem("photo_list",JSON.stringify([]));

    camera_context = this;
};

camera.prototype = {
    inicializarFotos: function () {
        console.log("Se inicializo a tomar fotos");
        //Se toma una foto de acuerdo a los milisegundos marcados
        camera_context.id_interval = setInterval(camera_context.snap, camera_context.milisegundos);
    },
    reiniciarFotos: function () {
        console.log("Se reinicio la lista");
        //camera_context.photo_list = new Array();
        localStorage.setItem("photo_list",JSON.stringify([]));
    },
    detenerFotos: function () {
        console.log("Se mando a detener");
        clearInterval(camera_context.id_interval);
    },
    snap: function () {
        Webcam.snap(function (dataUrl) {
            console.log("Snap");
            var image = dataUrl.replace("data:image/jpeg;base64,", "");
            
            //var photo_list = camera_context.photo_list;
            var photo_list = JSON.parse(localStorage.getItem("photo_list"));

            if (photo_list.length >= camera_context.limite){
                //delete photo_list[0];
                photo_list.shift(); //Remueve primer elemento
            }
                
            photo_list[photo_list.length] = image;
            
            localStorage.setItem("photo_list", JSON.stringify(photo_list));
            
            delete photo_list;
        });
    },
    setup: function () {
        //Configuracion de la camara
        Webcam.set({
            dest_width: 610,
            dest_height: 400,
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
                fotos: localStorage.getItem("photo_list")
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