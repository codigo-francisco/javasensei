var canvas_tutorial;
function takeScreenShot() {
    html2canvas(document.body, {
        onrendered: function (canvas) {
            canvas_tutorial = $(canvas);
            canvas_tutorial.prop("id","canvas_tutorial");
            canvas_tutorial.css({
                position : "absolute",
                top: 0,
                left: 0,
                'z-index':1400
            });
            $(document.body).append(canvas_tutorial);
            //document.body.appendChild(canvas);
        }
    },{
        useCORS : true
    });
}