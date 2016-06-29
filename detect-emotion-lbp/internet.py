#!/usr/bin/env python
# -*- coding: utf-8 -*

from __future__ import print_function

import sys
import urllib2
import warnings

import cv2
import numpy as np

from detect_emotion import detect_emotion

warnings.filterwarnings("ignore")

detector = detect_emotion("data/modelo.m")
def main(args):
    for arg in args:
        url = arg
        response = urllib2.urlopen(url)
        img = response.read()
        img = np.asanyarray(bytearray(img))
        img = cv2.imdecode(img, cv2.IMREAD_GRAYSCALE)
        result, prediction = detector.predict(img)
        print("Prediccion: ",prediction)
        cv2.imshow("Frame", img)
        cv2.waitKey()

if __name__=="__main__":
    if len(sys.argv)<2:
        quit("Faltan argumentos")
    else:
        main(sys.argv[1:])