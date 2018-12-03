#!/usr/bin/env python
# -*- coding: utf-8 -*

import cPickle
import warnings
from random import sample

import cv2

from detect_emotion import detect_emotion

warnings.filterwarnings("ignore")

detector = detect_emotion("data/modelo.m")

#cargamos las imagenes
imagenes_emocion = cPickle.load(open("data/imagenes_emociones1.sav","rb"))

def predecir_muestra(k=5):
    #tamaño de las imagenes
    size = len(imagenes_emocion)
    #Elección aleatoría de 5 indices
    indexes = sample(range(size), k)

    def predecir(index):
        frame = imagenes_emocion[index]["imagen"]
        emocionImagen = imagenes_emocion[index]["emocion"]
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        result, emocion = detector.predict(gray)
        cv2.putText(frame, emocionImagen, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_AA)
        cv2.putText(frame, emocion, (0,100), cv2.FONT_HERSHEY_SIMPLEX, 1,(0,0,0),2, cv2.LINE_AA)
        cv2.imshow("Imagen", frame)
        cv2.waitKey()

    map(predecir, indexes)

#predecir_muestra(5)