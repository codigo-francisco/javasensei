#!/usr/bin/env python
# -*- coding: utf-8 -*

import cPickle
import glob
import cv2
import math
import dlib
from sklearn.model_selection import cross_val_score
from sklearn.metrics import confusion_matrix
from sklearn.svm import LinearSVC
from sklearn.svm import SVC
from util import *
import random


class detect_emotion(object):
    model = None
    td = None
    tl = None
    pd = None
    pl = None
    emociones = ("Boredom", "Engagement", "Excitement", "Frustration")

    detector = dlib.get_frontal_face_detector()
    predictor = dlib.shape_predictor("data\shape_predictor_68_face_landmarks.dat")

    def __init__(self, modelPath=None, XPath=None, yPath=None, loadFiles=True):
        if loadFiles == True:
            self.model = cPickle.load(open(modelPath, "rb"))
            if XPath is not None:
                self.X = cPickle.load(open(XPath, "rb"))
            if yPath is not None:
                self.y = cPickle.load(open(yPath, "rb"))

    def predict(self, image):
        returnValue = (False, "Rostro no encontrado")
        result, img = self.__get_image__(image)
        if result:
            y = self.model.predict(np.array(img))
            returnValue = (True, self.emociones[y[0]])

        return returnValue

    def __get_image__(self, image): #get_landmark
        landmarks_vectorised = []
        result = True
        image = cv2.resize(image, (500, 500)) #Imagenes estandarizadas a 500 pixeles

        detections = self.detector(image, 1)
        for k, d in enumerate(detections):  # For all detected face instances individually
            shape = self.predictor(image, d)  # Draw Facial Landmarks with the predictor class
            xlist = []
            ylist = []
            for i in range(1, 68):  # Store X and Y coordinates in two lists
                xlist.append(float(shape.part(i).x))
                ylist.append(float(shape.part(i).y))

            xmean = np.mean(xlist)  # Get the mean of both axes to determine centre of gravity
            ymean = np.mean(ylist)
            xcentral = [(x - xmean) for x in
                        xlist]  # get distance between each point and the central point in both axes
            ycentral = [(y - ymean) for y in ylist]

            if xlist[26] == xlist[29]:  # If x-coordinates of the set are the same, the angle is 0, catch to prevent 'divide by 0' error in function
                anglenose = 0
            else:
                anglenose = int(math.atan((ylist[26] - ylist[29]) / (xlist[26] - xlist[29])) * 180 / math.pi)

            if anglenose < 0:
                anglenose += 90
            else:
                anglenose -= 90

            for x, y, w, z in zip(xcentral, ycentral, xlist, ylist):
                landmarks_vectorised.append(x)
                landmarks_vectorised.append(y)
                meannp = np.asarray((ymean, xmean))
                coornp = np.asarray((z, w))
                dist = np.linalg.norm(coornp - meannp)
                anglerelative = (math.atan((z - ymean) / (w - xmean)) * 180 / math.pi) - anglenose
                landmarks_vectorised.append(dist)
                landmarks_vectorised.append(anglerelative)

        if len(detections) < 1:
            result = False
        return (result, landmarks_vectorised)

    @staticmethod
    def create_model_training(savePath=None, XPath=None, yPath=None, path=None, rostrosPath=None):
        detect = detect_emotion(loadFiles=False)
        training_data = []
        training_labels = []
        prediction_data = []
        prediction_labels = []
        clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8, 8))

        for emotion in detect.emotions:
            training, prediction = detect_emotion.get_files(emotion)
            # Append data to training and prediction list, and generate labels 0-7
            for item in training:
                image = cv2.imread(item)  # open image
                gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)  # convert to grayscale
                clahe_image = clahe.apply(gray)
                landmarks_vectorised = detect.__get_image__(clahe_image)
                if landmarks_vectorised == "error":
                    pass
                else:
                    training_data.append(landmarks_vectorised)  # append image array to training data list
                    training_labels.append(detect.emotions.index(emotion))

            for item in prediction:
                image = cv2.imread(item)
                gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
                clahe_image = clahe.apply(gray)
                landmarks_vectorised = detect.__get_image__(clahe_image)
                if landmarks_vectorised == "error":
                    pass
                else:
                    prediction_data.append(landmarks_vectorised)
                    prediction_labels.append(detect.emotions.index(emotion))
        svm = SVC(kernel='linear', probability=True,tol=1e-3)
        svm.fit(training_data, training_labels)
        detect.model = svm
        detect.td = training_data
        detect.tl = training_labels
        detect.pd = prediction_data
        detect.pl = prediction_labels
        #Guardado
        cPickle.dump(training_data, open("data/td.x", "wb"))
        cPickle.dump(training_labels, open("data/tl.y", "wb"))
        cPickle.dump(prediction_data, open("data/pd.x", "wb"))
        cPickle.dump(prediction_labels, open("data/pl.y", "wb"))
        cPickle.dump(svm, open("data/modelo.m","wb"))

    @staticmethod
    def get_files(emotion):  # Define function to get file list, randomly shuffle it and split 80/20
        files = glob.glob("D:\Subcorpus\%s\*.png" % emotion)
        random.shuffle(files)
        # top 100
        files = files[:120]
        training = files[:int(len(files) * 0.9)]  # get first 80% of file list
        prediction = files[-int(len(files) * 0.1):]  # get last 20% of file list
        return training, prediction

    def crossValidation(self, cv=10):
        scores = cross_val_score(self.model, self.X, self.y, cv=cv)
        print("Precisión: %0.2f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))
        # Matriz de confusión
        yTrue = map(lambda index: self.emociones[index], self.y)
        yPred = map(lambda index: self.emociones[index], self.model.predict(self.X))
        cm = confusion_matrix(yTrue, yPred, self.emociones)
        print(cm)

# detector = detect_emotion.create_model_training()
# detector.crossValidation()