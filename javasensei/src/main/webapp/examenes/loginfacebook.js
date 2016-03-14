window.fbAsyncInit = function () {
    FB.init({
        appId: '603592966417068',
        xfbml: true,
        version: 'v2.1',
        status: true
    });
};

function checkLoginState(){
    FB.getLoginStatus(processLogin);
}

// Retorna un número aleatorio entre min (incluido) y max (excluido)
function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min)) + min;
}

function processLogin(response) {
    switch (response.status) {
        case 'connected' :
            
            var numAleatorio = getRandomInt(1,4);
            localStorage.setItem("usuario",response.authResponse.userID);
            localStorage.setItem("examen", numAleatorio);
            
            window.location="pre_test.html";
            break;
        case "not_authorized":
            alert("Por favor autorize a la aplicacion para usar facebook");
        default:
            //En caso de que no haga login
            $("#fullscreenlogin").show();
    }
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