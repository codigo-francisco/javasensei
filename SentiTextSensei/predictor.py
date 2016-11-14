# coding=utf-8

# Versión del detect_text_emotion pero sin la estructura de una clase.
# Se utiliza para entrenar el corpus y crear el archivo serializado corpus_model.m
# que contiene el corpus ya entrenado

import sys
import csv
import re
import nltk.classify
import unicodedata
from nltk import word_tokenize
from nltk.stem import SnowballStemmer
from nltk.corpus import stopwords
from string import punctuation
import cPickle
from sklearn.pipeline import Pipeline
from sklearn.externals import joblib
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from unicodedata import normalize
from sklearn.naive_bayes import BernoulliNB, MultinomialNB
from sklearn.cross_validation import train_test_split
from sklearn.metrics import classification_report
from sklearn.metrics import accuracy_score

# Si es la primera vez se tienen que descarcar los stopwords de:
# nltk.download("stopwords")

spanish_stopwords = stopwords.words('spanish')
stemmer = SnowballStemmer('spanish')
non_words = list(punctuation)

# train_corpus.csv contiene dos columnas
# primera columna -> polaridad texto (positivo o negativo)
# segunda columna -> texto
def cargar_corpus_entrenamiento(corpus):
    polaridad = []
    texto = []
    with open(corpus) as csv_file:
        reader = csv.reader(csv_file, delimiter='|')
        reader.next()
        for row in reader:
            # skip missing data
            #print(row[0])
            #print(row[1])
            if row[0] and row[1]:
                polaridad.append(row[0])
                texto.append(row[1])

        return polaridad,texto

# Stemming: transformamos cada palabra en su raiz.
def stem_tokens(tokens, stemmer):
    stemmed = []
    for item in tokens:
        stemmed.append(stemmer.stem(item))
    return stemmed

# Tokenizar, este paso convierte una cadena de texto en una lista de palabras (tokens).
# Mediante el uso de nltk.word_tokenize), remueve signos de puntuación y acentos.
def tokenize(texto):
    texto = ''.join([c for c in texto if c not in non_words])
    tokens = word_tokenize(texto)
    # stem
    try:
        stems = stem_tokens(tokens, stemmer)
    except Exception as e:
        print(e)
        print(texto)
        stems = ['']
    return stems

# Definición del vector de caracteristicas
count_vectorizer = CountVectorizer(
    analyzer='word',
    tokenizer=None,
    lowercase=True,
    stop_words=spanish_stopwords
)

# Crea el pipeline junto con el clasificador
text_clf = Pipeline([('vect', count_vectorizer),
                     ('tfidf', TfidfTransformer(use_idf=False)),
                     ('clf', BernoulliNB()),
                     ])

# Preprocess crear term frequency matrix
# CountVectorizer convierte la columna de texto en una matriz en la que cada palabra es una columna
# cuyo valor es el número de veces que dicha palabra aparece en cada texto
# Convierte a minusculas elimina signos de puntuacion y remueve stopwords
# Entrena el corpus y lo guarda como un objeto serializado
def preprocess_train():
    polaridad,texto = cargar_corpus_entrenamiento('data/train_corpus.csv')
    text_clf.fit(texto, polaridad)
    modelo = open("data/corpus_model.m","wb")
    cPickle.dump(text_clf, modelo)
    modelo.close()

# Dado un texto predice su sentimiento
def obtener_sentimiento(texto):
    modelo = open("data/corpus_model.m", "rb")
    text_clf2 = cPickle.load(modelo)
    modelo.close()

    newTexto = [(texto)]
    sentimiento = text_clf2.predict(newTexto)
    return(sentimiento)

def evaluar_modelo():
    polaridad, texto = cargar_corpus_entrenamiento('data/train_corpus.csv')
    # preparing data for split validation. 60% training, 40% test
    X_train, X_test, y_train, y_test = train_test_split(texto, polaridad, test_size=0.4, random_state=43)

    classifier = text_clf.fit(X_train,y_train)
    print("Score:",classifier.score(X_test,y_test))
    predicted = classifier.predict(X_test)
    #print(predicted)
    print classification_report(y_test,predicted)
    print "Nivel de predicción {:.2%}".format(accuracy_score(y_test,predicted))
    #print("Prueba de validación cruzada 60% training, 40% test")

# Procesa un archivo de prueba para validar el predictor de sentimiento
def test_corpus():
    texto =[]
    with open("data/test_corpus.csv","rb") as csv_file:
        reader = csv.reader(csv_file)
        for row in reader:
            texto.append(row[0])
    clf = joblib.load('data/corpus_model.m')
    #count_vectorizer.vocabulary = joblib.load("data/texto_corpus.pkl")
    newTexto = count_vectorizer.transform(texto)
    tfidf_data = TfidfTransformer(use_idf=False).fit_transform(newTexto)
    polaridad = clf.predict(tfidf_data)
    print(polaridad)

def main():
    print("Iniciando predictor...")
    # Si es la primera vez se tiene que entrenar el corpus, después ya no es necesario
    preprocess_train()
    print(obtener_sentimiento('Hoy es un dia complicado'))
    print(obtener_sentimiento('Hacer ejercicio es bueno para tu salud'))
    print(obtener_sentimiento('Programar en Java es divertido'))
    print(obtener_sentimiento('Programar en Java es aburrido'))
    #test_corpus()
    #evaluar_modelo()
    print("Fin predictor...")

if __name__ == '__main__':
    main()