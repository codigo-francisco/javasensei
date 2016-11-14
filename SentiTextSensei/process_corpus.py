# -*- coding: UTF-8 -*-
import csv
import re
import string
from unicodedata import normalize
import unicodedata

# diccionario.csv contiene dos columnas
# la primera columna es el slang/modismo utilizado en Internet
# la segunda columna es la traducción normal
def cargar_diccionario(archivo):
    modismos = []
    with open(archivo) as csv_file:
        reader = csv.reader(csv_file,delimiter=",",quotechar='"')
        reader.next()
        print("Diccionario slang-internet:")
        for row in reader:
            if row[0] and row[1]:
                modismos.append((row[0],row[1])) #slang-significado
                #print(row[1] + ":" + row[0])
        return modismos

# elimina letras repetidas, ejemplo: siiii, sii --> si
def remover_letras(texto):
    pattern = re.compile(r"(.)\1{1,}", re.DOTALL)
    return pattern.sub(r"\1\1", texto)

# Elimina los acentos en una cadena de texto
def remover_acentos(txt, codif='utf-8'):
    return normalize('NFKD', txt.decode(codif)).encode('ASCII','ignore')

# Elimina los números en una cadena de texto
def remover_numeros(texto):
    return re.sub(r'[0-9]*', '', texto)

#Elimina algunos caracteres
def remover_puntos1(texto):
    return re.sub(r'[.,]','',texto)

#Elimina algunos caracteres
def remover_puntos2(texto):
    return re.sub(r'["-:¡!¿?$;]','',texto)

#Elimina https?://* to URL
def remover_url(texto):
    return re.sub('((www\.[^\s]+)|(https?://[^\s]+))','',texto)

#Elimina @username
def remover_username(texto):
    return re.sub('@[^\s]+','',texto)

#Eliminar #word
def remover_hashtag(texto):
    return re.sub(r'#([^\s]+)', '', texto)

#Eliminar espacios
def remover_espacios(texto):
    texto = texto.strip()
    # remove last " at string end
    texto = texto.rstrip('\'"')
    texto = texto.lstrip('\'"')
    return texto

# No es la mejor solución para los problemas con utf-8
# pero funciona.
def normalizar_texto(texto):
    try:
        texto = unicode(texto, 'utf-8')
    except NameError: # unicode is a default on python 3
        pass
    texto = unicodedata.normalize('NFD', texto)
    texto = texto.encode('ascii', 'ignore')
    texto = texto.decode("utf-8")
    return str(texto)

# Realiza un preprocesamiento del corpus antes de iniciar
# la tokenizacion, traduce emoticons y otros slangs a texto
def procesar(arch_entrada,arch_salida):
    original = open(arch_entrada, 'r')
    final = open(arch_salida, "w")

    print("leyendo y procesando corpus.csv...")
    datos = original.read()
    original.close()
    salida = remover_puntos1(datos)
    salida = reduce(lambda a, kv: a.replace(*kv), cargar_diccionario('data/diccionario.csv'), salida.lower())
    salida = remover_letras(datos)
    salida = remover_acentos(salida)
    salida = remover_url(salida)
    salida = remover_username(salida)
    salida = remover_hashtag(salida)
    salida = remover_numeros(salida)
    salida = remover_puntos2(salida)
    salida = remover_espacios(salida)
    final.write(salida.lower())
    final.close()
    print("preprocesamiento finalizado...ok")

def main():
    procesar('data/corpus.csv','data/train_corpus.csv')

if __name__ == '__main__':
    main()
