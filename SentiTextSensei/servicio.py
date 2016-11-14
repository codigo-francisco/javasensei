# Under Python, start a Pyro NameServer: python -m Pyro4.naming
#!/usr/bin/env python
# -*- coding: utf-8 -*

import base64

import Pyro4
import numpy as np

from detect_text_emotion import detect_text_emotion

@Pyro4.expose
class sentiment_text_service(object):
    def emotion_text_prediction_java(self, texto):
        returnValue = detector.predict_sentimiento(texto)
        print returnValue
        return returnValue[0]
    def emotion_text_prediction_pythonweb(self, texto):
        returnValue = detector.predict_sentimiento(texto)
        print returnValue
        return returnValue[0]

detector = detect_text_emotion("data/corpus_model.m")
daemon = Pyro4.Daemon("localhost", 25315)                # make a Pyro daemon
uri = daemon.register(sentiment_text_service,"text_service",True)
print("Ready...")
print("Uri: ", uri)
daemon.requestLoop()

