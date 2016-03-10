// Opera 8.0+
var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
// Firefox 1.0+
var isFirefox = typeof InstallTrigger !== 'undefined';
// At least Safari 3+: "[object HTMLElementConstructor]"
var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
// Internet Explorer 6-11
var isIE = /*@cc_on!@*/false || !!document.documentMode;
// Edge 20+
var isEdge = !isIE && !!window.StyleMedia;
// Chrome 1+
var isChrome = !!window.chrome && !!window.chrome.webstore;
// Blink engine detection
var isBlink = (isChrome || isOpera) && !!window.CSS;

function detectarNavegador() {
    if (isFirefox) {
        $(".fullscreenbrowser").hide();
        camera_sensei.setup();
    }
}

function enlazarControlesCondiciones() {
    $("#checkAcuerdo").on("change", function () {
        if ($("#checkAcuerdo").prop("checked")) {
            $("#btnAceptarCondiciones").removeClass("ui-disabled");
        } else {
            $("#btnAceptarCondiciones").addClass("ui-disabled");
        }
    });

    $("#btnAceptarCondiciones").click(function () {
        $.get(
                url + "condiciones",
                {id: usuario.id},
                function (response) {
                    usuario.aceptarCondiciones = true;
                    console.log("Se guardaron las condiciones");
                }
        );

        $( ":mobile-pagecontainer" ).one( "pagecontainerhide",
            function( event, ui ) {
                console.log(ui.prevPage[0].id);
                if (ui.prevPage[0].id == "paginacondiciones")
                    tutorial_principal.mostrarTutorial(true);
            }
        );
        
        $.mobile.back();
    });
}

function mostrarCondiciones() {
    $(":mobile-pagecontainer").pagecontainer("change", "#paginacondiciones");
}