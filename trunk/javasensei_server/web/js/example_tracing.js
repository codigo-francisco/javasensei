//informacion global
var contexto = {};

var example_tracing_sensei = function (areatrabajo, areasoluciones, boton_adelante, boton_atras, controles) { //Recibe el ejercicio que se va a cargar
    contexto = this; //Guardamos el contexto, una referencia a este owner

    this.areatrabajo = $(areatrabajo);
    this.areasoluciones = $(areasoluciones);
    this.cierretracing = $("#controles_cierre_tracing");
    this.boton_adelante = $(boton_adelante);
    this.boton_atras = $(boton_atras);
    this.controles = $(controles);

    this.tree_example_tracing = emptyFunction;
    this.letras = ["a", "b", "a", "b"];

    this.suscriptores_carga = new buckets.Set();
    this.suscriptores_inicio = new buckets.Set(); //Conjunto con los suscriptores
    this.suscriptores_error = new buckets.Set();
    this.suscriptores_suboptimo = new buckets.Set();
    this.suscriptores_optimo = new buckets.Set();
    this.suscriptores_final_optimo = new buckets.Set();
    this.suscriptores_final_suboptimo = new buckets.Set();
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
    },
    suscribirse_inicio_ejercicio: function (callback_inicio) {
        contexto.suscriptores_inicio.add(callback_inicio);
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
                notificaciones = contexto.suscriptores_optimo;
                break;
            case "pasosuboptimo":
                notificaciones = contexto.suscriptores_suboptimo;
                break;
            case "pasofinalsuboptimo":
                notificaciones = contexto.suscriptores_final_suboptimo;
                break;
            case "pasofinaloptimo":
                notificaciones = contexto.suscriptores_final_optimo;
                break;
        }

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

    },
    mover_adelante: function () {
        //Llamamos al mover atras del tree_example, este nos devuelve el paso que necesitamos redenrizar
        var paso_datos = contexto.tree_example_tracing.adelante();

        contexto.construir_interfaz(paso_datos, true, false);
    },
    cerrarInterfaz : function(){
        contexto.areatrabajo.hide();
        contexto.controles.hide();
        contexto.cierretracing.hide();
    },
    construir_ejercicio: function (url) { //Construye todo el ejercicio junto con el example_tracing
        this.areatrabajo.show();
        this.controles.show();

        //Se enlazan los click con la funcion actual
        this.boton_atras.unbind("click").bind("click",this.mover_atras);
        this.boton_adelante.unbind("click").bind("click",this.mover_adelante);

        //Obtenemos el json del ejercicio
        $.getJSON(carpeta_ejercicios + url, function (data) {
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
                    /*case "pasoerroneo":
                     contexto.notificar_evento(contexto.ejercicio.tipo);
                     break;
                     case "pasooptimo":
                     break;
                     case "pasosuboptimo":
                     break;*/
            }
            contexto.notificar_evento(tipo, datos_notificacion);
        }

        //Se evalua si se debe habilitar el boton atras
        if (contexto.tree_example_tracing.tieneAtras()) {
            contexto.boton_atras.removeClass("ui-disabled"); //Habilitarlo
        } else {
            contexto.boton_atras.addClass("ui-disabled"); //Sedhabilitado
        }

        //Se evalua si se debe habilitar el boton atras
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

        codigo.html(this.ejercicio.presentacion.join("<br>"));

        hljs.highlightBlock(codigo[0]);
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