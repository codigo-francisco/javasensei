/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function(){
    var contentLink = $("#content-link");
    contentLink.hide();
    
    $("#buscar").on("click",function (){
        
        var idAlumno = $("#alumno").val();
        var emocionInicial = $("#inicial").val();
        var emocionFinal = $("#final").val();
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
                    var dateConvert = new Date(value.fecha.$date).toLocaleString();
                    value.fecha = dateConvert;
                    
                });
                
                csv = Papa.unparse(data);
                contentLink.show();

            }).fail(function(){
                alert("No se pudo hacer la peticion de las bitacoras");
            });
            
            var link = $("#link");
            
            link.on("click", function() {
                link.attr("href", 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv));
                link.attr("target","_blank");
                link.attr("download",'bitacoras.csv');
                contentLink.hide();
            });
        
    });
    
    
    
});
