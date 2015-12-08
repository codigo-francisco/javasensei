function Dato(json, nodo){
    this.json= JSON.parse(JSON.stringify(json));
    this.nodo=nodo;
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

var objeto= new Dato({},-1);
objeto.opciones.push(new Dato(jsonVacio,0));

var n = [];
//VARIABLES PARA EL ARBOL
var nodes;
var edges;
var container;
var data;
var options =   {
                    edges: {
                        smooth: {
                            type:'cubicBezier',
                            forceDirection: "vertical",
                            roundness: 0.4
                        }
                    },
                    layout: {
                        hierarchical:{
                            direction: "UD"
                        }
                    }
                };
var network;
$(document).ready(function(){
    nodes = new vis.DataSet([
        {id: -1, label: 'I.I.', color:"#F2F2F2", level:0, cola:[]},
        {id: 0, label: 'P.I.', level:1, cola:[0]}]);
    edges = new vis.DataSet([
        {from: -1, to: 0}]);
    data = {nodes: nodes, edges: edges};
    container = document.getElementById('mynetwork');
    network = new vis.Network(container, data, options);
    network.selectNodes([-1]);

    network.on("selectNode",function(){
        guardar();
        n=[].concat(nodes.get(network.getSelectedNodes()[0]).cola);
        cambiarCampos();
    });
});

function adentrar(indice){
    document.body.style.cursor = "auto";
    guardar();
    n.push(indice);
    cambiarCampos();
}
var banderaNuevaOpGuard=true;
function atrasar(){
    if(banderaNuevaOpGuard){
        if(n.length==1)
            guardar();
        n.pop();
        cambiarCampos();    
    }
    else{
        borrarOpcion();
        banderaNuevaOpGuard=true;
    }   
}
//Guardar es para guardar localmente en el objeto global de tipo DATO.
function funcBtnAceptar(){
    guardar();
    atrasar();
}
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
    var etiq;
    var color;
    var fuente;
    switch(parseInt(jsonTexts["Tipo"])){
        case 0: etiq="P.O.";
            color="#21610B";
            break;
        case 1: etiq="P.SO."; 
            color="#21610B";
            break;
        case 2: etiq="P.E."; 
            color="#FF0000";
            break;
        case 3: etiq="P.F.O."; 
            color="#21610B";
            fuente = {color:"#000000", strokeWidth:2, strokeColor:"#000000"};
            break;
        case 4: etiq="P.F.SO."; 
            color="#21610B";
            fuente = {color:"#000000", strokeWidth:2, strokeColor:"#000000"};
            break;
    }
    if(!banderaNuevaOpGuard){
        ob.nodo=idNode;
        agregarNodo(etiq, color, fuente);
        network.selectNodes([ob.nodo]);
        banderaNuevaOpGuard=true;
    }else{
        if(n.length>1)
        nodes.update({id:ob.nodo, label:etiq, color:color, font:fuente, cola: [].concat(n)});
    }
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
    if(n.length>1){
        $("#Negativa").after("<button id='btnAceptar' onclick='funcBtnAceptar()'> Aceptar </button>");
        $("#Negativa").after("<br><br>");
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

    if(ob.nodo != undefined)
        network.selectNodes([ob.nodo]);
}
function agregarOpcion(){
    var ob=objeto;
    n.forEach( function(valor){
        ob=ob.opciones[valor];
    });
    ob.opciones.push(new Dato(jsonVacio));
    adentrar(ob.opciones.length-1); 
    banderaNuevaOpGuard=false;
}
function borrarOpcion(){
    //Borrar el nodo del arbol
    var ob=objeto;
    n.forEach( function(valor){
        ob=ob.opciones[valor];
    });
    if(ob.nodo != undefined){
        borrarNodo();
        borrarBand=true;
    }
    //Borrar del la lista de opciones
    ob=objeto;
    for(i=0; i<n.length-1;i++){
        ob=ob.opciones[n[i]];
    }
    ob.opciones.splice(n[n.length-1],1);
    n.pop();
    for(i=0; i<ob.opciones.length;i++){
        var opc = ob.opciones[i];
        var aux = [].concat(n);
        aux.push(i);
        nodes.update({id:opc.nodo, cola:aux});
    }
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
var idNode=1;
function agregarNodo(etiq, color, fuente){
    var ob=objeto;
    for(i=0; i<n.length-1;i++){
        ob=ob.opciones[n[i]];
    }
    var idNodeBefore= ob.nodo;
    var levelBefore = nodes.get(idNodeBefore)["level"];
    nodes.add({id:idNode,label:etiq, color:color, font:fuente, level:levelBefore+1, cola: [].concat(n)});
    edges.add({from: idNodeBefore, to:idNode++});
}
var borrarBand=true;
function borrarNodo(){
    var nodoSel = network.getSelectedNodes()[0];
    if(borrarBand){
        borrarBand=false;
        var ob=objeto;
        for(i=0; i<n.length;i++){
            ob=ob.opciones[n[i]];
        }
        nodoSel = ob.nodo;
    }
    var nodosHijos = edges.get({filter:function(item){return item.from == nodoSel}});

    nodosHijos.forEach(function(nodo){
        network.selectNodes([nodo.to]);
        borrarNodo();        
    });

    network.selectNodes([nodoSel]);
    network.deleteSelected();
}