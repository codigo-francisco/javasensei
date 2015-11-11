var graficosEmocionesSensei=function(){
    
    this.configuracionGrafica = {
        chart: {
            type: 'scatter',
            zoomType: 'xy'
        },
        title: {
            text: null
        },
        subtitle: {
            text: null
        },
        xAxis: {
            tickInterval:1,
            title: {
                enabled: true,
                text: 'Numero de Paso'
            }
        },
        yAxis: {
            tickInterval:1,
            title: {
                text: 'Tiempo (s)'
            }
        },
        plotOptions: {
            scatter: {
                tooltip: {
                    pointFormat: 'Paso {point.x}, Segundo: {point.y}'
                }
            }
        },
        series: []
    };
    
    this.cargarGrafico = function(){
        var control = $("#graficaComportamiento");
        var emociones = new buckets.Dictionary();;
        var datos = [];
        
        //Se debe organizar la informacion de la bitacora por emociones
        for(var index=0; index < avatar_context.bitacoras.length; index++){
            var bitacora = avatar_context.bitacoras[index];
            if (!emociones.containsKey(bitacora.emocion)){
                emociones.set(bitacora.emocion,[]);
            }
            
            var arr = emociones.get(bitacora.emocion);
            arr.push([bitacora.pasoId, bitacora.segundos]);
            emociones.set(bitacora.emocion, arr);
        }
        
        //Debe convertirse al formato correcto
        emociones.forEach(function(key,value){
            datos.push({
                name : key,
                data: value
            });
        });
        
        this.configuracionGrafica.series = datos;
        
        control.empty();
        
        control.highcharts(this.configuracionGrafica);
    };
};