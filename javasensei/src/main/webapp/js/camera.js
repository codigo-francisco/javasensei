var camera_context = {};

var camera = function () {

    this.limite = 1; //Para modificar la cantidad de fotos a almacenar
    this.milisegundos = 1000; //1000 milisegundos es una foto cada segundo
    this.id_interval = 0;
    this.url = "emocion/";

    this.tipoCamino = "";
    this.funcionEmocion = function () {
    };

    localStorage.setItem("last_photo", JSON.stringify([]));

    localStorage.setItem("photo_list", JSON.stringify([]));

    camera_context = this;
};

camera.prototype = {
    inicializarFotos: function () {
        console.log("Se inicializo a tomar fotos");
        //Se toma una foto de acuerdo a los milisegundos marcados
        if (usuario.activarEmociones)
            camera_context.id_interval = setInterval(camera_context.snap, camera_context.milisegundos);
    },
    reiniciarFotos: function () {
        console.log("Se reinicio la lista");
        localStorage.setItem("last_photo", localStorage.getItem("photo_list"));
        localStorage.setItem("photo_list", JSON.stringify([]));
    },
    detenerFotos: function () {
        console.log("Se mando a detener");
        if (camera_context.id_interval)
            clearInterval(camera_context.id_interval);
    },
    snap: function () {
        Webcam.snap(function (dataUrl) {
            console.log("Snap");
            var image = dataUrl.replace("data:image/jpeg;base64,", "");

            //var photo_list = camera_context.photo_list;
            var photo_list = JSON.parse(localStorage.getItem("photo_list"));

            if (photo_list.length >= camera_context.limite) {
                //delete photo_list[0];
                photo_list.shift(); //Remueve primer elemento
            }

            photo_list[photo_list.length] = image;

            var datos = {
                usuario: usuario.id,
                ejercicioId: avatar_context.id,
                pasoId: tree_self.pasoactual,
                fecha: new Date().toISOString(),
                tipoPaso: contexto.tipoPaso,
                segundos: segundos,
                fotografia: image
            };

            $.ajax({
                url: url + "bitacora/bitacorafotografia",
                type: "POST",
                data: {
                    datos: JSON.stringify(datos)
                },
                dataType: "json"
            });

            localStorage.setItem("photo_list", JSON.stringify(photo_list));

            delete photo_list;
            delete datos;
            delete image;
        });
    },
    setup: function () {
        //Configuracion de la camara
        Webcam.set({
            dest_width: 610,
            dest_height: 400,
            image_format: 'jpeg',
            jpeg_quality: 75,
            force_flash: false,
            flip_horiz: true
        });

        Webcam.on("live", eliminarBackgroundCamera);
        
        Webcam.on("error", errorCamara);

        Webcam.attach('#camera');
    },
    getUltimasFotografias: function () {
        return localStorage.getItem("last_photo");
    },
    getFotografias: function () {
        camera_context.detenerFotos();

        return localStorage.getItem("photo_list");
    }
};