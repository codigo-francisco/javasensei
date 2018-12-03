var logout = function logout() {
    FB.logout(function (response) {
        console.warn("Se hizo logout en facebook %O", response);
        mostrarBackground();
    });
};

var processLoginJhonDoe = function () {
    usuario.idFacebook = "12345789123456789";
    usuario.token = "asdfd67as76dfas8";
    usuario.nombre = "Jhon Doe";
    usuario.foto = "";
    usuario.activarEmociones = true;
    eliminarBackground();
};

var examenActivado = false;

var processLogin = function processLogin(response) {
    //Construccion del usuario
    switch (response.status) {
        case 'connected' :
            var uid = response.authResponse.userID;
            var accessToken = response.authResponse.accessToken;

            $("#facebookId").text("ID: " + uid);
            usuario.idFacebook = uid;
            usuario.token = accessToken;

            if (examenActivado) {
                //Validamos si ya realizo el examen pretest
                //Validar si el usuario realizó el cuestionario
                $.get("servicios/examenes/realizoExamenPreTest",
                        {
                            idFacebook: usuario.idFacebook
                        }
                , function (response) {
                    if (response.realizado) {
                        loginUsuario();
                    } else {
                        alert("Aún no has realizado tu examen pretest, serás redirigido a él");
                        sessionStorage.setItem("usuario", usuario.idFacebook);
                        window.location = "examenes/pre_test.html";
                    }
                }
                );
            } else {
                loginUsuario();
            }

            break;
        case "not_authorized":
            alert("Por favor autorize a la aplicacion para usar facebook");
        default:
            //En caso de que no haga login
            $("#fullscreenlogin").show();
    }
};

function loginUsuario() {
    FB.api("/me", {fields: "name,picture"},
            function (response) {
                console.log("Datos del facebook: %O", response);

                usuario.nombreFacebook = response.name;
                usuario.fotografia = response.picture.data.url;

                $.get(url + "estudiantes/studentExists",
                        {idFacebook: usuario.idFacebook},
                        function (response) {
                            if (response == "true") {
                                checarUsuario(usuario);
                            } else {
                                $("#pantalla_nombre").show(); //Solicitamos el nombre
                            }

                            eliminarBackground();
                        });

                $("#imagen_usuario").attr("src", usuario.fotografia);
                $("#nombre_usuario").text(usuario.nombreFacebook);
            });
}

function checarUsuario(datos, nuevo) {
    var urlChecar = url + "estudiantes/getorcreatestudent";
    var datosAEnviar = {
        idFacebook: datos.idFacebook,
        nombreFacebook: datos.nombreFacebook,
        fotografia: datos.fotografia,
        token: datos.token,
        nuevo: false
    };

    if (nuevo) {
        datosAEnviar.nombre = datos.nombre;
        datosAEnviar.edad = datos.edad;
        datosAEnviar.sexo = datos.sexo;
        datosAEnviar.nuevo = true;

        alert("Gracias por proporcionar tus datos");
    }

    $.ajax(urlChecar, {
        data: {datos: JSON.stringify(datosAEnviar)},
        dataType: "json"
    }).done(function (data) {
        console.log("%cLogin de facebook realizado: %O", "color: red", data);
        usuario = data;
        configurarTutoriales();
        menu.actualizarMenu();
        //Verificamos si ya firmo las condiciones
        if (!usuario.aceptarCondiciones) {
            mostrarCondiciones();
        } else if (usuario.mostrarTutorialPrincipal == false) { //Verificamos si ya observo el tutorial
            tutorial_principal.mostrarTutorial(true);
        }

        $("#pantalla_nombre").hide();
    });
}

window.fbAsyncInit = function () {
    FB.init({
        appId: '603592966417068',
        xfbml: true,
        version: 'v2.1',
        status: true
    });

    checkLoginState();
};

function checkLoginState() {
    //Obtenemos el token para el trabajo
    FB.getLoginStatus(processLogin);
}

(function (d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {
        return;
    }
    js = d.createElement(s);
    js.id = id;
    js.src = "//connect.facebook.net/es_MX/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));
;