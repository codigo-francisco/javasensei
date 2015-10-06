var configuracionGrafica = {
    data: {
        
    },
    chart: {
        polar: true,
        type: 'area',
        spacingRight:50,
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
        min: 0,     //max: 10*/
        title: {
            text: 'Ejercicios Resueltos'
        }
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
        
        configuracionGrafica.data = [{table: 'datatable'}];
        
    }

    $("#primertabg").empty();
    
    configuracionGrafica.chart.width = $("#divGraficaEstudiante").css("width").replace("px","");
    configuracionGrafica.chart.height = 400;
    
    var div = $("<div>");

    div.highcharts(configuracionGrafica);

    $("#primertabg").append(
             div
    );
};
