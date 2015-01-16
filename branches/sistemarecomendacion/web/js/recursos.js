function recursos_sensei(usuario) {

    this.obtenerVideo = function obtenerVideo(control) {
        //TODO: Dummy
        //alert("prueba");
        var control = $(control);
        control.attr("src", "//www.youtube.com/embed/Z0F7sJaOQtw?rel=0");
    };

    this.obtenerLibros = function obtenerLibros(control) {
        //TODO: Es dummy
        var libros = [//Aqui deberia obtenerse los libros
            {titulo: 'Fundamentos en java',
                url: 'www.google.com'},
            {titulo: 'Aprenda a programar en java!',
                url: 'www.google.com'}
        ];
        
        crearLista(libros, control);
    };

    this.obtenerEnlaces = function obtenerEnlaces(control) {
        //TODO: Es dummy
        var enlaces= [
            {titulo: '¿Cuales son los lenguajes de programacion mas usado (2014)?',
                url: "www.google.com"},
            {titulo: 'Android supera en cantidad de desarrolladores a iOS',
                url: 'www.google.com'},
            {titulo: 'El narcicismo y Apple', 
                url: 'www.google.com'}
        ];
        
        crearLista(enlaces, control);
    };
    
    function crearLista(items, control){
        var control = $(control);
        control.empty();
        
        $.each(items, function(index, item) {
            control.append($("<li>").
                    append($("<a>")
                        .attr("href", item.url)
                        .text(item.titulo)
                            ));
        });
        
        control.listview("refresh");
    }
}