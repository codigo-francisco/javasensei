#!/usr/bin/env python
# -*- coding: utf-8 -*

import Pyro4

# Nota: solo para pruebas internas

#text_emotion = Pyro4.Proxy("PYRONAME:sentiment_text_service")    # use name server object lookup uri shortcut
text_emotion = Pyro4.Proxy("PYRO:obj_02a4eecbc69546d584cec52140f7046a@localhost:49426")
print(text_emotion.emotion_text_prediction_pythonweb("Programar en Java es aburrido"))