//informacion global
var contexto = {};
var progreso = 0;
var nivelMax;
var matrizEjercicios = [];
var example_tracing_sensei = function () { //Recibe el ejercicio que se va a cargar
    contexto = this; //Guardamos el contexto, una referencia a este owner

    this.areatrabajo = $("#areatrabajo");
    this.areasoluciones = $("#areasolucion");
    
    this.cierretracing = $(".controles_cierre_tracing");
    this.controles = $(".controles_tracing");
    this.progressbar = $("#progressbar");
    
    this.boton_adelante = $("#adelante_tracing");
    this.boton_atras = $("#atras_tracing");

    this.tree_example_tracing = emptyFunction;
    this.letras = ["a", "b", "a", "b"];
    
    this.tipoPaso = "";

    this.suscriptores_carga = new buckets.Set();
    this.suscriptores_inicio = new buckets.Set(); //Conjunto con los suscriptores
    this.suscriptores_error = new buckets.Set();
    this.suscriptores_suboptimo = new buckets.Set();
    this.suscriptores_optimo = new buckets.Set();
    this.suscriptores_final_optimo = new buckets.Set();
    this.suscriptores_final_suboptimo = new buckets.Set();
    this.suscriptores_paso_atras = new buckets.Set();
    this.suscriptores_paso_siguiente = new buckets.Set();
};

var createInput = function createInput() {
    return $("<input type='text' readonly='readonly'>")[0].outerHTML;
};

var createFocusInput = function createFocusInput() {
    var input = $(createInput());
    input.addClass("focusInput");

    return input[0].outerHTML;
};

var emptyFunction = function () {
};

example_tracing_sensei.prototype = {
    suscribir_todos_ejercicios: function (obj_callbacks) {
        //Aqui se suscribiran a todos los eventos...
        contexto.suscriptores_carga.add(obj_callbacks.primera_carga);
        contexto.suscriptores_inicio.add(obj_callbacks.inicio_ejercicio);
        contexto.suscriptores_error.add(obj_callbacks.paso_erroneo);
        contexto.suscriptores_suboptimo.add(obj_callbacks.paso_suboptimo);
        contexto.suscriptores_optimo.add(obj_callbacks.paso_optimo);
        contexto.suscriptores_final_optimo.add(obj_callbacks.paso_final_optimo);
        contexto.suscriptores_final_suboptimo.add(obj_callbacks.paso_final_suboptimo);
        contexto.suscriptores_paso_atras.add(obj_callbacks.paso_atras);
        contexto.suscriptores_paso_siguiente.add(obj_callbacks.paso_siguiente);
    },
    notificar_evento: function (tipo, datos) {
        var notificaciones;

        switch (tipo) {
            case "carga":
                notificaciones = contexto.suscriptores_carga;
                break;
            case "pasoinicial":
                notificaciones = contexto.suscriptores_inicio;
                break;
            case "pasoerroneo":
                contexto.tree_example_tracing.colocarError(datos.paso);
                notificaciones = contexto.suscriptores_error;
                break;
            case "pasooptimo":
                contexto.tree_example_tracing.colocarAcierto(datos.paso);
                notificaciones = contexto.suscriptores_optimo;
                break;
            case "pasosuboptimo":
                contexto.tree_example_tracing.colocarAcierto(datos.paso);
                notificaciones = contexto.suscriptores_suboptimo;
                break;
            case "pasofinalsuboptimo":
                contexto.tree_example_tracing.colocarAcierto(datos.paso);
                notificaciones = contexto.suscriptores_final_suboptimo;
                break;
            case "pasofinaloptimo":
                contexto.tree_example_tracing.colocarAcierto(datos.paso);
                notificaciones = contexto.suscriptores_final_optimo;
                break;
            case "pasoatras":
                notificaciones = contexto.suscriptores_paso_atras;
                break;
            case "pasosiguiente":
                notificaciones = contexto.suscriptores_paso_siguiente;
                break;
        }
        
        contexto.tipoPaso = tipo;
        
        if (notificaciones) {
            //Se notifica a todos los suscriptores, pasando los datos
            notificaciones.forEach(function (suscriptor) {
                suscriptor(datos);
            });
        }
    },
    mover_atras: function () {
        //Llamamos al mover atras del tree_example, este nos devuelve el paso que necesitamos redenrizar
        var paso_datos = contexto.tree_example_tracing.atras();
        contexto.construir_interfaz(paso_datos, true, false);
        
        contexto.notificar_evento("pasoatras");
    },
    mover_adelante: function () {
        //Llamamos al mover atras del tree_example, este nos devuelve el paso que necesitamos redenrizar
        var paso_datos = contexto.tree_example_tracing.adelante();
        contexto.construir_interfaz(paso_datos, true, false);
        
        contexto.notificar_evento("pasosiguiente");
    },
    cerrarInterfaz : function(){
        contexto.areatrabajo.hide();
        contexto.controles.hide();
        contexto.cierretracing.hide();
        contexto.progressbar.hide();
        chat.changeExercise(false);
    },
    construir_ejercicio: function (data) { //Construye todo el ejercicio junto con el example_tracing
        this.areatrabajo.show();
        this.controles.show();
        this.progressbar.show();
        this.cierretracing.hide();

        //Se enlazan los click con la funcion actual
        this.boton_atras.unbind("click").bind("click",this.mover_atras);
        this.boton_adelante.unbind("click").bind("click",this.mover_adelante);

        //Titulo del ejercicio
        $("#tituloEjercicio").text(data.titulo);
        
        chat.changeExercise(true);

        //Obtenemos el json del ejercicio
        $.getJSON(carpeta_ejercicios + data.url, function (data) {
            contexto.notificar_evento("carga",{
                instruccionesejercicio: data.instruccionesejercicio
             });
            contexto.tree_example_tracing = new tree_example_tracing(data);
            contexto.tree_example_tracing.construirMatriz(); //Se construye la matriz de ejercicios.
            contexto.construir_interfaz(0); //El paso inicial siempre sera el 0
        });
    },
    cambiar_interfaz: function (event) {
        contexto.construir_interfaz(event.data.paso);
    },
    construir_interfaz: function (paso, esJson, notificar) { //Construye la Interfaz Grafica
        if (notificar == undefined) //En caso de que no este definido, se declara verdadero por default
            notificar = true;

        //Se carga el ejercicio, excepto si el dato ya es json
        if (!esJson)
            contexto.ejercicio = contexto.tree_example_tracing.avanzarNodo(paso);
        else
            contexto.ejercicio = paso;

        if (notificar) {
            //Se evalua que tipo de paso es para notificar el evento correspondiente
            var datos_notificacion = {};
            var tipo = contexto.ejercicio.tipo;
            datos_notificacion.tipo = tipo;
            datos_notificacion.paso = contexto.ejercicio.paso;
            //Switch para la creacion de datos
            switch (tipo) {
                case "pasoinicial":
                    datos_notificacion.id = contexto.tree_example_tracing.obtenerId();
                    break;
            }
            contexto.notificar_evento(tipo, datos_notificacion);
        }
        
        nivelMax = 0;
        matrizEjercicios = $.map(contexto.tree_example_tracing.matriz,function(el){
            return el;
        });
        for (var i=0;i<matrizEjercicios.length;i++){
            if (matrizEjercicios[i].nivel > nivelMax)
                nivelMax = matrizEjercicios[i].nivel;
        }
        
        //Se evalua si se debe habilitar el boton atras
        if (contexto.tree_example_tracing.tieneAtras()) {
            contexto.boton_atras.removeClass("ui-disabled"); //Habilitarlo
        } else {
            contexto.boton_atras.addClass("ui-disabled"); //Sedhabilitado
        }

        //Se evalua si se debe habilitar el boton adelante
        if (contexto.tree_example_tracing.tieneAdelante()) {
            contexto.boton_adelante.removeClass("ui-disabled"); //Habilitarlo
        } else {
            contexto.boton_adelante.addClass("ui-disabled"); //Sedhabilitado
        }

        contexto.construir_area_trabajo();
        contexto.construir_area_solucion();
    },
    construir_area_trabajo: function () {
        //Obtenemos el nodo del ejercicio
        var instruccion = this.areatrabajo.find("#instruccion");

        instruccion.html(this.ejercicio.instruccion);

        var codigo = this.areatrabajo.find("#codigo");
        codigo.removeClass();
        codigo.addClass("java");
        
        codigo.empty();

        hljs.highlightBlock(codigo[0]);

        codigo.html(this.ejercicio.presentacion.join("<br>"));
        
        codigo.find("input:text").each(function(index,input){
            input = $(input);
            var size = input.val().length;
            
            if (size<10)
                size=2;
            else if (size > 20)
                size-=5;
            
            input.attr("size",size);
        });

        pasoActual = contexto.tree_example_tracing.pasoactual;
        nivelActual = matrizEjercicios[pasoActual].nivel-1;
        progreso = Math.round((nivelActual/(nivelMax-1))*100);
        $("#progressbar")
                .progressbar("value",progreso)
                .children("#progressbar > div")
                .html(progreso + '%')
                .css({
                    "display":"block",
                    //"position":"absolute",
                    "font-size":"1em",
                    "line-height":"1em",
                    "text-align": "center"
                });
    },
    construir_area_solucion: function () {
        //Construimos los botones
        var nameClass = "ui-block-";
        this.areasoluciones.empty();


        $.each(this.ejercicio.opciones, function (index, opcion) {
            var paso = opcion.paso;
            contexto.areasoluciones.
                    append($("<div>").
                            addClass(nameClass + contexto.letras[index]).
                            append($("<button>").
                                    attr("id", "b" + index).
                                    addClass("ui-btn ui-corner-all").
                                    text(opcion.texto).
                                    click({paso: paso}, contexto.cambiar_interfaz)
                                    )
                            )

        });

    },
    obtenerCalidadRespuesta:function(){
        return contexto.tree_example_tracing.obtenerCalidadRespuesta();
    },
    obtenerTextoEmocional: function (emocion, retroalimentacion) {
        //Obtenemos del nodo actual, los textos de retroalimentacion y motivacion
        return contexto.tree_example_tracing.obtenerTextoEmocional(emocion, retroalimentacion);
    }
};