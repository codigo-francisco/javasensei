
$(function(){
    var contentLink = $("#content-link");
    contentLink.hide();
    
    $("#buscar").on("click",function (){
        
        var idAlumno = $("#alumno").val();
        var emocionInicial = $("#emocionInicial").val();
        var emocionFinal = $("#emocionFinal").val();
        var sesionId = $("#sesion").val();
        var fechaIncial = $("#fechaInicial").val();
        var fechaFinal = $("#fechaFinal").val();
        var ejercicioId = $("#ejercicio").val();
        
        $.ajax({
            type:"POST",
            url: "servicios/bitacora/obtenerbitacora",
            
            data:{
                idAlumno: idAlumno, 
                ejercicioId: ejercicioId,
                fechaInicial: fechaIncial,
                fechaFinal: fechaFinal,
                sesionId: sesionId,
                emocionInicial: emocionInicial,
                emocionFinal: emocionFinal
            }

            }).done(function(data){
                
                $.each(data, function(index,value) {
                    var dateConvert = new Date(value.fecha.$date);
                    dateConvert = new Date(dateConvert.getTime()+(dateConvert.getTimezoneOffset()*60000));
                    value.fecha = dateConvert.toLocaleString();
                    
                });
                
                csv = Papa.unparse(data);
                contentLink.show(1000);

            }).fail(function(){
                alert("No se pudo hacer la peticion de las bitacoras");
            });
            
            var link = $("#link");
            
            link.on("click", function() {
                link.attr("href", 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv));
                link.attr("target","_blank");
                link.attr("download",'bitacoras.csv');
                contentLink.hide(1000);
            });
        
    });
    
    
    
});
