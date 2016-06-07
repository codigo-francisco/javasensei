#!/usr/bin/env python
# -*- coding: utf-8 -*

import base64

import Pyro4
import cv2
import numpy as np

from detect_emotion import detect_emotion


class emotion_service(object):
    def emotion_face_prediction_java(self, fotografia):
        img = base64.decodestring(fotografia["data"])
        img = np.asanyarray(bytearray(img))
        gray = cv2.imdecode(img, cv2.IMREAD_GRAYSCALE)
        result = detector.predict(gray)
        return result
    def emotion_face_prediction_pythonweb(self, img):
        img = np.asanyarray(bytearray(img,"utf8"))
        img = cv2.imdecode(img, cv2.IMREAD_GRAYSCALE)
        result = detector.predict(img)
        return result

detector = detect_emotion("data/prueba.m", "data/X.x", "data/y.y")
daemon = Pyro4.Daemon("localhost", 25312)
uri = daemon.register(emotion_service, "emotion_service", True)
print("Uri: ", uri)
daemon.requestLoop()