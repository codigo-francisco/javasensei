#!/usr/bin/env python
# Software License Agreement (BSD License)
#
# Copyright (c) 2012, Philipp Wagner <bytefish[at]gmx[dot]de>.
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions
# are met:
#
#  * Redistributions of source code must retain the above copyright
#    notice, this list of conditions and the following disclaimer.
#  * Redistributions in binary form must reproduce the above
#    copyright notice, this list of conditions and the following
#    disclaimer in the documentation and/or other materials provided
#    with the distribution.
#  * Neither the name of the author nor the names of its
#    contributors may be used to endorse or promote products derived
#    from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
# "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
# LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
# FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
# COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
# INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
# BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
# LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
# CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
# LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
# ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.

import sys, os
import glob
import cPickle
# import facerec modules
from facerec.feature import Fisherfaces
from facerec.distance import EuclideanDistance
from facerec.classifier import NearestNeighbor
from facerec.model import PredictableModel
from facerec.validation import KFoldCrossValidation
from facerec.visual import subplot
from facerec.util import minmax_normalize
# import numpy, matplotlib and logging
import numpy as np
from PIL import Image
import matplotlib.cm as cm
import logging
import cv2
from sklearn.cross_validation import train_test_split
from sklearn.metrics import classification_report
from sklearn.metrics import confusion_matrix

emociones = ("feliz", "neutral", "triste")

def read_images():
    """Reads the images in a given folder, resizes images on the fly if size is given.

    Args:
        path: Path to a folder with subfolders representing the subjects (persons).
        sz: A tuple with the size Resizes

    Returns:
        A list [X,y]

            X: The images, which is a Python list of numpy arrays.
            y: The corresponding labels (the unique number of the subject, person) in a Python list.
    """
    c = 0
    X,y = [], []
    cascade_face = cv2.CascadeClassifier()
    cascade_face.load("classifiers/lbpcascade_frontalface.xml")
    #emociones = ("enojado", "feliz", "neutral", "sorpresa", "triste")
    carpeta_emociones = "D:/Respaldo Jose Luis/proyecto RVERK/RafD_Ordenado/"
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
                        im = cv2.resize(im ,(100, 100))
                        X.append(np.asarray(im, dtype=np.uint8))
                        y.append(indice)
            except IOError, (errno, strerror):
                print "I/O error({0}): {1}".format(errno, strerror)
            except:
                print "Unexpected error:", sys.exc_info()[0]
                raise
    return [X,y]


# Now read in the image data. This must be a valid path!
[X,y] = read_images()
# Then set up a handler for logging:
handler = logging.StreamHandler(sys.stdout)
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
handler.setFormatter(formatter)
# Add handler to facerec modules, so we see what's going on inside:
logger = logging.getLogger("facerec")
logger.addHandler(handler)
logger.setLevel(logging.DEBUG)
# Define the Fisherfaces as Feature Extraction method:
feature = Fisherfaces()
# Define a 1-NN classifier with Euclidean Distance:
classifier = NearestNeighbor(dist_metric=EuclideanDistance(), k=1)
# Define the model as the combination
model = PredictableModel(feature=feature, classifier=classifier)
# Compute the Fisherfaces on the given data (in X) and labels (in y):
#X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.05, random_state=42)
model.compute(X, y)
# Then turn the first (at most) 16 eigenvectors into grayscale
# images (note: eigenvectors are stored by column!)
E = []
for i in xrange(min(model.feature.eigenvectors.shape[1], 16)):
    e = model.feature.eigenvectors[:,i].reshape(X[0].shape)
    E.append(minmax_normalize(e,0,255, dtype=np.uint8))
# Plot them and store the plot to "python_fisherfaces_fisherfaces.pdf"
subplot(title="Fisherfaces", images=E, rows=4, cols=4, sptitle="Fisherface", colormap=cm.jet, filename="fisherfaces.png")

# Perform a 10-fold cross validation
cv = KFoldCrossValidation(model, k=10)
cv.validate(X, y)
# And print the result:
cv.print_results()
cPickle.dump(model,open("data/modeloFaceRec.pck","wb"),cPickle.HIGHEST_PROTOCOL)