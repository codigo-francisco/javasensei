#!/usr/bin/env python

import cPickle
import glob
import sys

# import facerec modules
from facerec.feature import SpatialHistogram
from facerec.classifier import SVM
from facerec.model import PredictableModel
from facerec.validation import KFoldCrossValidation
# import numpy, matplotlib and logging
import numpy as np
import logging
import cv2
import warnings

warnings.filterwarnings("ignore")

emociones = ("enojado", "feliz")#, "neutral", "sorpresa", "triste")

def read_images():
    X,y = [], []
    cascade_face = cv2.CascadeClassifier()
    cascade_face.load("classifiers/lbpcascade_frontalface.xml")
    clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8, 8))
    #emociones = ("enojado", "feliz", "neutral", "sorpresa", "triste")
    #carpeta_emociones = "D:/Respaldo Jose Luis/proyecto RVERK/RafD_Ordenado/"
    carpeta_emociones = "D:/Francisco/Pictures/Camera Roll/"
    indice = -1
    for emocion in emociones:
        imagenes = glob.glob(carpeta_emociones + emocion + "\\*.jpg")
        indice += 1
        for imagen in imagenes:
            try:
                im = cv2.imread(imagen, cv2.IMREAD_GRAYSCALE)
                faces = cascade_face.detectMultiScale(im)
                if len(faces) > 0:
                    for (corX, corY, w, h) in faces[:1]:
                        im = im[corY:corY + h, corX:corX + w]
                        #im = cv2.resize(im ,(100, 100))
                        X.append(np.asarray(im, dtype=np.uint8))
                        y.append(indice)
            except IOError, (errno, strerror):
                print "I/O error({0}): {1}".format(errno, strerror)
            except:
                print "Unexpected error:", sys.exc_info()[0]
                raise
    return [X,y]

# Then set up a handler for logging:
handler = logging.StreamHandler(sys.stdout)
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
handler.setFormatter(formatter)
# Add handler to facerec modules, so we see what's going on inside:
logger = logging.getLogger("facerec")
logger.addHandler(handler)
logger.setLevel(logging.DEBUG)

[X, y] = read_images()
#y = cPickle.load(open("data/y.y","rb"))
#X = cPickle.load(open("data/X.x","rb"))

feature = SpatialHistogram()
# Define a 1-NN classifier with Euclidean Distance:
classifier = SVM()
# Define the model as the combination
model = PredictableModel(feature=feature, classifier=classifier)

model.compute(X, y)

cPickle.dump(model,open("data/modelo2.m","wb"), cPickle.HIGHEST_PROTOCOL)

# Perform a 10-fold cross validation
cv = KFoldCrossValidation(model, k=10)
cv.validate(X, y)
cv.print_results()