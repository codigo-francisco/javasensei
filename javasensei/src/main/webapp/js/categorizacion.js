var ultimas_fotos=[];
function iniciarCategorizacion(){    
    //Obtener fotografias
    $.post(url+"bitacora/obtenerbitacoracategorizacion",
        {idUsuario:usuario.id},
        function(result){
            var idElemento = "#paginacategorizacion";
            
            //Por alguna razon jQuery no lo esta haciendo automaticamente
            result = JSON.parse(result);
            ultimas_fotos = result;
            
            if (result.length>0){
                crearCarousel(result);
            }
            
            //Inicializar la categorización por parte del usuario
            $(":mobile-pagecontainer").pagecontainer("change", idElemento);    
        }
    );
}

function crearCarousel(datos){
    var trackFotos = $("#trackFotos");
    trackFotos.empty();
    
    for (var objeto in datos){
        objeto = datos[objeto];
        trackFotos.append(
            $("<li>")
            .append(
                $("<img>")
                .prop("src","data:image/jpeg;base64,"+objeto.fotografia)
            )
        );
    }
    
    trackFotos.itemslide();
}