var configuracionGrafica = {
    chart: {
        polar: true,
        type: 'area',
        spacingRight:100,
        spacingLeft:50
    },
    title:{
        text:null
    },
    tooltip: {
        formatter: function () {
            return this.y + " ejercicios de " + this.x + " realizados";
        }
    },
    xAxis: {
    },
    yAxis: {
        gridLineInterpolation: "circle",
        tickInterval: 1,
        min: 0
                //max: 10
    },
    legend: {
        enabled: false
    },
    series: [
    ]
};

var cargarGrafico = function cargarGrafico() {
    $.ajax({
        url: "servicios/dominioEstudiante/getDataGraphics",
        type: "GET",
        data:{
            idAlumno : usuario.id
        },
        contentType: "application/json",
        dataType: "json"
    }).done(function (datos) {
        //datos.intervencion = true; //TODO: Quitar esto, es de prueba, falla las reglas difusas
        console.log("Datos de graficos recibido: " + datos);
        crearGrafico(datos);
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("Fallo " + textStatus);
    });
};

var crearGrafico = function crearGrafico(data) {
    if (data){
        configuracionGrafica.xAxis = {
            categories: data.lecciones
        };

        configuracionGrafica.yAxis.max = data.maximo;

        configuracionGrafica.series = [{data:data.listaEjercicios}];
    }

    $("#graficaestudiante").highcharts(configuracionGrafica);
};