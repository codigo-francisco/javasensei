function iniciarCategorizacion(){
    $("#fotografiascategorizacion").glide({
        type: "carousel",
        autoplay:false,
        centered:true,
        autoheight:true
    });
    
    //Obtener fotografias
    $.post(url+"bitacora/obtenerbitacoracategorizacion",function(result){
        if (result.length>0){
            for (var foto in result){
                
            }
        }
        //Inicializar la categorización por parte del usuario
        $(":mobile-pagecontainer").pagecontainer("change", "#paginacateagorizacion");    
    });
}