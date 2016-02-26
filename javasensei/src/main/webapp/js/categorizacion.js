function iniciarCategorizacion(){    
    //Obtener fotografias
    $.post(url+"bitacora/obtenerbitacoracategorizacion",
        {idUsuario:usuario.id},
        function(result){
            var idElemento = "#paginacategorizacion";
            var trackFotos = $("#trackFotos");
            
            trackFotos.empty();
            //Por alguna razon jQuery no lo esta haciendo automaticamente
            result = JSON.parse(result);
            
            if (result.length>0){
                //Construir html
                for (var objeto in result){
                    objeto = result[objeto];
                    trackFotos.append(
                        $("<li>")
                        .addClass("glide__slide")
                        .append(
                            $("<img>").prop("src","data:image/jpeg;base64,"+objeto.fotografia)
                        )
                    );
                }
            }
            
            $("#fotografiascategorizacion").glide({
                type: "carousel",
                autoplay:false,
                centered:true,
                autoheight:true
            });
            
            //Inicializar la categorización por parte del usuario
            $(":mobile-pagecontainer").pagecontainer("change", idElemento);    
        }
    );
}