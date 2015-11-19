var jsonList;
function cargarSelectJSON(indice){
    var jsonBox = document.getElementById("jsonBox");
    var btnNuevo = document.getElementById("btnNuevo");
    if(indice==0){
        btnNuevo.disabled=true;
        jsonBox.length=1;
        obtenerJSON(0);
        jsonBox.disabled = true;
        return;
    }
    $.ajax({
        url: "servicios/recursos/getJSONList",
        type: "GET",
        data: {
            coleccion: indice
        },
        dataType: "json"
    }).done(function (data) {
        jsonList = data;
        jsonBox.length=1;
        obtenerJSON(0);
        for(i=0; i<jsonList.length;i++){
            jsonBox.options[i+1] = new Option(indice!=3?jsonList[i].nombre:jsonList[i].titulo);
        }
        jsonBox.disabled = false;
        btnNuevo.disabled=false;
    }).fail(function (data) {
        jsonBox.length=1;
        obtenerJSON(0);
        jsonBox.options[0] = new Option("ERROR",0);
    });
}
function obtenerJSON(indice){
    var btnGuardar = document.getElementById("btnGuardar");
    var btnBorrar = document.getElementById("btnBorrar");
    var btnGuardar2 = document.getElementById("btnGuardar2");
    var btnBorrar2 = document.getElementById("btnBorrar2");
    var jsonArea = document.getElementById("jsonArea");
    var artCajas = $("#artCajas");
    if(indice==0){
        artCajas.empty();
        jsonArea.value="";
        btnGuardar.disabled=true;
        btnBorrar.disabled=true;
        btnGuardar2.disabled=true;
        btnBorrar2.disabled=true;
        return;
    }
    var datos = jsonList[indice-1];
    jsonArea.value = JSON.stringify(datos);
    artCajas.empty();
    Object.keys(datos).forEach(
        function(key) {
            var value = datos[key];
            var label = $("<span>").text(key+": ");
            var caja=$("<input type='text'>").attr("id",key).val(value);
            artCajas.append(label);
            artCajas.append(caja);
            artCajas.append($("<br>"));
        }
    );
    btnGuardar.disabled=false;
    btnBorrar.disabled=false;
    btnGuardar2.disabled=false;
    btnBorrar2.disabled=false;
}
function nuevo(){
    var jsonBox = document.getElementById("jsonBox");
    var collectionBox = document.getElementById("collectionBox");
    var btnNuevo = document.getElementById("btnNuevo");
    var btnBorrar = document.getElementById("btnBorrar");
    var btnGuardar = document.getElementById("btnGuardar");
    var btnBorrar2 = document.getElementById("btnBorrar2");
    var btnGuardar2 = document.getElementById("btnGuardar2");
    var jsonArea = document.getElementById("jsonArea");
    var artCajas = $("#artCajas");
    if(btnNuevo.value=="Cancelar"){
        collectionBox.selectedIndex=0;
        collectionBox.disabled=false;
        btnNuevo.value="Nuevo";
        btnGuardar.disabled=true;
        btnGuardar2.disabled=true;
        btnNuevo.disabled=true;
        jsonArea.value="";
        artCajas.empty();
        return;
    }
    jsonBox.length=1;
    jsonBox.disabled=true;
    collectionBox.disabled=true;
    btnNuevo.value="Cancelar";
    btnGuardar.disabled=false;
    btnBorrar.disabled=true;
    btnGuardar2.disabled=false;
    btnBorrar2.disabled=true;
    var datos;
    switch(collectionBox.selectedIndex){
        case 1: 
            jsonArea.value = "{\"id\":IDNUM,\"nombre\":\"NOMBRE DE LECCION\"}" ;
            datos=JSON.parse("{\"id\":1,\"nombre\":\" \"}");
        break;
        case 2: 
            jsonArea.value ="{\"idLeccion\":IDLECCION,\"id\":IDNUM,\"nombre\":\"NOMBRE DEL TEMA\"}" ;
            datos=JSON.parse("{\"idLeccion\":1,\"id\":1,\"nombre\":\" \"}");
        break;
        case 3: 
            jsonArea.value ="{\"idLeccion\":IDLECCION,\"id\":IDNUM,\"url\":\"leccionN/ejercicioN.json\",\"titulo\":\"TITULO DEL EJERCICIO\",\"idTema\":IDTEMA}" ;
             datos=JSON.parse("{\"idLeccion\":1,\"id\":1,\"url\":\"leccion01/ejercicio1.json\",\"titulo\":\" \",\"idTema\":1}");
        break;
    }
    artCajas.empty();
    Object.keys(datos).forEach(
        function(key) {
            var value = datos[key];
            var label = $("<span>").text(key+": ");
            var caja=$("<input type='text'>").attr("id",key).val("");
            artCajas.append(label);
            artCajas.append(caja);
            artCajas.append($("<br>"));
        }
    );
}
function guardar(op){
    var jsonBox = document.getElementById("jsonBox");
    var collectionBox = document.getElementById("collectionBox");
    var jsonArea = document.getElementById("jsonArea");
    var btnNuevo = document.getElementById("btnNuevo");
    var jsonViejo= (jsonBox.selectedIndex==0)? "": JSON.stringify(jsonList[jsonBox.selectedIndex-1]);
    var json;
    if(op==1){
        json=jsonArea.value;
    }else{
        var texts = $("#artCajas").find("input");
        var jsonFinal = {};
        $.each(texts, function(index, value){
                var text = $(texts[index]);
                if(isNaN(text.val()))
                    jsonFinal[text.attr("id")]=text.val();
                else{
                    var val = parseInt(text.val());
                    jsonFinal[text.attr("id")]=val;
                }
        });
        json=(JSON.stringify(jsonFinal));
    }
    $.ajax({
        url: "servicios/recursos/guardar",
        type: "GET",
        data: {
            coleccion: collectionBox.selectedIndex,
            json: json,
            oldJson: jsonViejo
        },
        dataType: "json"
    }).done(function (data) {
        alert(data.More);
        if(data.result){
            btnNuevo.value="Nuevo";
            collectionBox.disabled=false;
            collectionBox.selectedIndex=0;
            cargarSelectJSON(0);
        }
    }).fail(function (data) {
        alert("Error en la petición AJAX para Guardar.");        
    });
    
}
function borrar(){
    var jsonBox = document.getElementById("jsonBox");
    var collectionBox = document.getElementById("collectionBox");
    var jsonViejo= JSON.stringify(jsonList[jsonBox.selectedIndex-1]);
    $.ajax({
        url: "servicios/recursos/guardar",
        type: "GET",
        data: {
            coleccion: collectionBox.selectedIndex,
            json: "delete",
            oldJson: jsonViejo
        },
        dataType: "json"
    }).done(function (data) {
        alert(data.More);
        if(data.result){
            collectionBox.disabled=false;
            collectionBox.selectedIndex=0;
            cargarSelectJSON(0);
        }
    }).fail(function (data) {
        alert("Error en la petición AJAX para Borrar.");        
    });   
}