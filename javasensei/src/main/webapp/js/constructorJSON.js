function Dato(json){
	this.json= JSON.parse(JSON.stringify(json));
	this.opciones=[];
}
function puntero(){
	document.body.style.cursor = "pointer";
}
function punteroOut(){
	document.body.style.cursor = "auto";
}

var jsonVacio = {"Tipo":"", "Texto":"", "Instruccion":"", "Presentacion":"", 
				"Neutral":"", "Encantado":"", "Sorprendido":"",
				"Compasivo":"", "Esceptico":"", "Positiva":"", "Neutral ":"", "Negativa":""};

var objeto= new Dato({});
objeto.opciones.push(new Dato(jsonVacio));

var n = [];

function adentrar(indice){
	document.body.style.cursor = "auto";
	guardar();
    n.push(indice);
    cambiarCampos();
}
function atrasar(){
    guardar();
    n.pop();
    cambiarCampos();
}
//Guardar es para guardar localmente en el objeto global de tipo DATO.
function guardar(){
    var jsonTexts = {};
    //Guarda el tipo si no es pasoinicial
    if(n.length>1){
        var sel = $("#selTipo").prop("selectedIndex");
        jsonTexts["Tipo"]=parseInt(sel);
    }
    //Guarda lo de los input type="text" y textarea
    var texts = $("#campos").find("input,textarea");
    $.each(texts, function(index, value){
        var text = $(texts[index]);
        if(isNaN(text.val()) || text.val()=="")
            if(text.attr("id") == "Presentacion" || text.attr("id") == "Instrucciones del Ejercicio"){
                jsonTexts[text.attr("id")]=text.val().split('\n');
            }
            else
                jsonTexts[text.attr("id")]=text.val();
        else{
            var val = parseInt(text.val());
            jsonTexts[text.attr("id")]=val;
        }
    });
    if(n.length==1)
        jsonTexts["Texto"]="Paso Inicial";
    //Por ultimo lo guarda en el objeto local de tipo DATO
    var ob=objeto;
    n.forEach( function(valor){
        ob=ob.opciones[valor];
    });
    ob.json=jsonTexts;
}
function cambiarCampos(){
	var ob=objeto;
	n.forEach( function(valor){
		ob=ob.opciones[valor];
	});
	var campos=$("#campos");
    campos.empty();
	var datos = ob.json;
    Object.keys(datos).forEach(
        function(key) {
            var value = datos[key];
            var label = $("<span>").text(key).attr("id","lbl"+key);
            var caja;
            if(key!="Tipo"){
                if(key=="Presentacion" || key=="Instrucciones del Ejercicio"){
                    value=value.toString().replace(/,/g,"\n");
                    caja=$("<textarea>").attr("id",key).val(value);
                }
                else{
                    caja=$("<input type='text'>").attr("id",key).val(value);    
                }
                campos.append(label);
                campos.append(caja);
                campos.append($("<br>"));
            }else{
                campos.append(label);
                campos.append($("<select id='selTipo' onchange=''>").append($("<option selected>").text("Paso Optimo")).append($("<option>").text("Paso Suboptimo")).append($("<option>").text("Paso Erroneo")).append($("<option>").text("Paso Final Optimo")).append($("<option>").text("Paso Final Suboptimo")));
                if(value!="")
                    $("#selTipo").prop("selectedIndex",parseInt(value));
            }
        }
    );            
    $("#lblNeutral").before($("<p>").append($("<span>").append($("<strong>").append("Texto Emocional"))));        
    $("#lblPositiva").before($("<p>").append($("<span>").append($("<strong>").append("Retroalimentación"))));
    $("#Id").prop("type","number");       
    if(n.length == 1){
        $("#Texto").remove();
        $("#lblTexto").remove();
        $("#lblTipo").remove();
        $("#selTipo").remove();
    }
    var listaOp = $("#listaOpciones");
    listaOp.empty();
    var textList;
    for(i=0; i<ob.opciones.length;i++){
        textList = ob.opciones[i].json["Texto"];
        if(textList=="")
            textList="Sin Texto";
    	listaOp.append($("<li>").append($("<span onmouseover='puntero()' onmouseout='punteroOut()' onclick='adentrar("+i+")'>").text(textList)));
    }
    if(n.length>0){
    	$("#btnAgregar").attr("disabled",false);
    	$("#btnAtras").attr("disabled",false);
    }
    else{
    	$("#btnAgregar").attr("disabled",true);
    	$("#btnAtras").attr("disabled",true);
    }
    if(n.length>1)
    	$("#btnBorrar").attr("disabled",false);
    else
    	$("#btnBorrar").attr("disabled",true);
}
function agregarOpcion(){
	var ob=objeto;
	n.forEach( function(valor){
		ob=ob.opciones[valor];
	});
	ob.opciones.push(new Dato(jsonVacio));
	adentrar(ob.opciones.length-1);
}
function borrarOpcion(){
	var ob=objeto;
	for(i=0; i<n.length-1;i++)
		ob=ob.opciones[n[i]];
	ob.opciones.splice(n[n.length-1],1);
	n.pop();
	cambiarCampos();
}
var paso;
function transformarJSON(){
    guardar();
    var jsonFinal = {};
    jsonFinal["id"]=parseInt(objeto.json["Id"]);
    jsonFinal["paso"]=-1;
    jsonFinal["tipo"]="instruccionesiniciales";
    jsonFinal["instruccionesejercicio"]=objeto.json["Instrucciones del Ejercicio"];
    jsonFinal["opciones"]=[];
    paso=0;
    jsonFinal["opciones"].push(transformar(objeto.opciones[0]));
    //grabar
    var nombre = prompt("Escribe el nombre del archivo:","");
    var blob = new Blob([JSON.stringify(jsonFinal,null,3)],{type:"text/json;"});
    saveAs(blob, nombre+".json");
}
function transformar(ob){
    var jsonAux={};
    jsonAux["paso"]=paso;
    paso++;
    var opJson = ob.json; 
    if(paso!=1){
        jsonAux["texto"]=opJson["Texto"];
    }
    switch(parseInt(opJson["Tipo"])){
       case 0: jsonAux["tipo"]="pasooptimo"; break;
       case 1: jsonAux["tipo"]="pasosuboptimo"; break;
       case 2: jsonAux["tipo"]="pasoerroneo"; break;
       case 3: jsonAux["tipo"]="pasofinaloptimo"; break;
       case 4: jsonAux["tipo"]="pasofinalsuboptimo"; break;
       default: jsonAux["tipo"]="pasoinicial"; break;
    }
    jsonAux["instruccion"]=opJson["Instruccion"];
    jsonAux["presentacion"]=opJson["Presentacion"];
    jsonAux["textoEmocional"]={};
    jsonAux["textoEmocional"]["neutral"]=opJson["Neutral"];
    jsonAux["textoEmocional"]["encantado"]=opJson["Encantado"];
    jsonAux["textoEmocional"]["sorprendido"]=opJson["Sorprendido"];
    jsonAux["textoEmocional"]["compasivo"]=opJson["Compasivo"];
    jsonAux["textoEmocional"]["esceptico"]=opJson["Esceptico"];
    jsonAux["textoEmocional"]["retroalimentacion"]={};
    jsonAux["textoEmocional"]["retroalimentacion"]["positiva"]=opJson["Positiva"];
    jsonAux["textoEmocional"]["retroalimentacion"]["neutral"]=opJson["Neutral "];
    jsonAux["textoEmocional"]["retroalimentacion"]["negativa"]=opJson["Negativa"];
    jsonAux["opciones"]=[];

    for(var i=0; i<ob.opciones.length; i++){
        jsonAux["opciones"].push(transformar(ob.opciones[i]));
    }
    return jsonAux;
}