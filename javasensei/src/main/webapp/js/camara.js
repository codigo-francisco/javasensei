var camera_context = {};

var camera = function () {

    this.limite = 1; //Para modificar la cantidad de fotos a almacenar
    this.milisegundos = 1000; //1000 milisegundos es una foto cada segundo
    this.id_interval = 0;
    this.url = "emocion/";

    this.tipoCamino = "";
    this.funcionEmocion = function () {
    };

    camera_context = this;
};

camera.prototype = {
    inicializarFotos: function () {
        console.log("Se inicializo a tomar fotos");
        //Se toma una foto de acuerdo a los milisegundos marcados
        if (usuario.activarEmociones)
            camera_context.id_interval = setInterval(camera_context.snap, camera_context.milisegundos);
    },
    reiniciarFotos: function (inicializar) {
        console.log("Se reinicio la sesion de fotografias");
        
        $.get(
            url + "bitacora/reiniciarsesionfotografia",
            {id:usuario.id},
            function(result){
                if (inicializar){
                    camera_context.inicializarFotos();
                }
            }
        );
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

            var datos = {
                usuario: usuario.id,
                ejercicioId: avatar_context.id,
                pasoId: tree_self.pasoactual,
                fecha: new Date().toISOString(),
                tipoPaso: contexto.tipoPaso,
                segundos: segundos,
                sesionactual: true,
                tutor:"",
                emocionTutor:"",
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
    getFotografias: function () {
        camera_context.detenerFotos();

        //return localStorage.getItem("photo_list");
    }
};