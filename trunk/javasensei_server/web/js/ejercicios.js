function obtenerInstruccion(id){
    //TODO: se obtiene las instrucciones iniciales para un ejercicio
    
    return {
        emocion:"neutral",
        texto: ["Las variables son una de las características fundamentales de los lenguajes de programación, permiten acceder a la memoria para almacenar y recuperar los datos con los que nuestros programas van a trabajar. Son por tanto el mecanismo que los lenguajes de programación ponen a nuestra disposición para acceder a la memoria."
                ,"Se trata de un mecanismo de lo más sencillo, sólo tenemos que dar un nombre a nuestras variables, a partir de ese momento el compilador traducirá de forma automática ese nombre en un acceso a memoria. Por ejemplo:"
                ,"<code>//Almacenamos un dato en memoria referenciado por el nombre edad "
                ,"edad = 5;"
                ,"//Recuperamos el dato almacenado y lo modificamos"
                ,"edad = edad + 1;</code>"],
        opciones: []
    };
}
function obtenerEjercicios(usuario) {
    //TODO: dummy por ahora devuelve un ejecicio de usuario por default (para definir el objeto)
    return [
            {
                id:3,
                titulo:'Introducción a Java',
                lecciones:[
                    {
                        id:1,
                        url: 'ejercicio1/practica1.json',
                        titulo: 'Ejercicio 1'
                    },
                    {
                        id:2,
                        url: 'leccion01/ejercicio2.json',
                        titulo: 'Ejercicio 2'
                    },
                    {
                        id:3,
                        url: 'leccion01/ejercicio3.json',
                        titulo: 'Ejercicio 3'
                    }
                ]
            },
            {
                id:4,
                titulo:'Variables y cÃ¡lculos',
                lecciones:[
                    {
                        id:1,
                        url: 'leccion02/ejercicio1.json',
                        titulo: 'Ejercicio 1'
                    },
                    {
                        id:2,
                        url: 'leccion02/ejercicio2.json',
                        titulo: 'Ejercicio 2'
                    },
                    {
                        id:3,
                        url: 'leccion02/ejercicio3.json',
                        titulo: 'Ejercicio 3'
                    }
                ]
            },
            {
                id:5,
                titulo:'Instrucciones de selecciÃ³n',
                lecciones:[
                    {
                        id:1,
                        url: 'leccion03/ejercicio1.json',
                        titulo: 'Ejercicio 1'
                    },
                    {
                        id:2,
                        url: 'leccion03/ejercicio2.json',
                        titulo: 'Ejercicio 2'
                    }
                ]
            },
            {
                id:6,
                titulo:'Instrucciones de repeticiÃ³n',
                lecciones:[
                    {
                        id:1,
                        url: 'leccion04/ejercicio1.json',
                        titulo: 'Ejercicio 1'
                    },
                    {
                        id:2,
                        url: 'leccion04/ejercicio2.json',
                        titulo: 'Ejercicio 2'
                    }
                ]
            },
            {
                id:7,
                titulo:'MÃ©todos',
                lecciones:[
                    {
                        id:1,
                        url: 'leccion05/ejercicio1.json',
                        titulo: 'Ejercicio 1'
                    },
                    {
                        id:2,
                        url: 'leccion05/ejercicio2.json',
                        titulo: 'Ejercicio 2'
                    }
                ]
            },
            {
                id:8,
                titulo:'Arreglos',
                lecciones:[
                    {
                        id:1,
                        url: 'leccion06/ejercicio1.json',
                        titulo: 'Ejercicio 1'
                    },
                    {
                        id:2,
                        url: 'leccion06/ejercicio2.json',
                        titulo: 'Ejercicio 2'
                    }
                ]
            }
        ];
}
