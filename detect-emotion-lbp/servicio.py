#!/usr/bin/env python
# -*- coding: utf-8 -*

import base64

import Pyro4
import numpy as np
import cv2
from detect_emotion import detect_emotion
import os
import warnings

#warnings.simplefilter("ignore") #Ignorar el warning DeprecationWarning: Passing 1d arrays as data is deprecated in 0.17 and will raise ValueError in 0.19. Reshape your data either using X.reshape(-1, 1) if your data has a single feature or X.reshape(1, -1) if it contains a single sample. DeprecationWarning)

class emotion_service(object):
    @Pyro4.expose
    def emotion_face_prediction_java(self, fotografia):
        #ruta del archivo
        img = cv2.imread(fotografia)
        # gray = cv2.imdecode(img, cv2.IMREAD_GRAYSCALE)
        os.remove(fotografia)
        result = detector.predict(img)
        print(result)
        return result
    def emotion_face_prediction_pythonweb(self, img):
        img = np.asanyarray(bytearray(img,"utf8"))
        #img = cv2.imdecode(img, cv2.IMREAD_GRAYSCALE)
        result = detector.predict(img)
        return result

detector = detect_emotion("data/modelo.m")
daemon = Pyro4.Daemon("localhost", 25312)
uri = daemon.register(emotion_service, "emotion_service", True)
print("Ejecutando servicio")
print("Uri: ", uri)
daemon.requestLoop()