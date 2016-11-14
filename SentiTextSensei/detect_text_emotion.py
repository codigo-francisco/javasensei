# coding=utf-8
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

class detect_text_emotion(object):

    spanish_stopwords = None
    stemmer = None
    non_words = None
    count_vectorizer = None
    text_clf = None
    corpus_model = None

    def __init__(self, model):
        self.inicializar_parametros()
        #cargar corpus
        self.corpus_model = open(model, "rb")
        self.text_clf = cPickle.load(self.corpus_model)
        self.corpus_model.close()

    def inicializar_parametros(self):
        # Si es la primera vez --> descargar stopwords
        # nltk.download("stopwords")
        self.spanish_stopwords = stopwords.words('spanish')
        self.stemmer = SnowballStemmer('spanish')
        self.non_words = list(punctuation)
        self.count_vectorizer = CountVectorizer(
            analyzer='word',
            tokenizer=None,
            lowercase=True,
            stop_words=self.spanish_stopwords
        )
        self.text_clf = Pipeline([('vect', self.count_vectorizer),
                                  ('tfidf', TfidfTransformer(use_idf=False)),
                                  ('clf', BernoulliNB()),
                                  ])

    # Stemming: transformamos cada palabra en su raiz.
    def stem_tokens(self,tokens, stemmer):
        stemmed = []
        for item in tokens:
            stemmed.append(stemmer.stem(item))
        return stemmed

    # Tokenizar, este paso convierte una cadena de texto en una lista de palabras (tokens).
    # Mediante el uso de nltk.word_tokenize), remueve signos de puntuación y acentos.
    def tokenize(self,texto):
        textoX = ''.join([c for c in texto if c not in self.non_words])
        tokens = word_tokenize(textoX)
        # stem
        try:
            stems = self.stem_tokens(tokens, self.stemmer)
        except Exception as e:
            print(e)
            print(texto)
            stems = ['']
        return stems

    # Dado un texto predice su sentimiento
    def predict_sentimiento(self,texto):
        newTexto = [(texto)]
        sentimiento = self.text_clf.predict(newTexto)
        return sentimiento

