#!/usr/bin/env python
# -*- coding: utf-8 -*

import cPickle
import glob
import os.path
import sys
import cv2

import dlib
from skimage.feature import local_binary_pattern
from sklearn.model_selection import cross_val_score
from sklearn.metrics import confusion_matrix
from sklearn import neighbors
from sklearn.svm import LinearSVC
from util import *


class detect_emotion(object):
    classifier_face = cv2.CascadeClassifier(r"classifiers/lbpcascade_frontalface.xml")
    classifier_eyes1 = cv2.CascadeClassifier("classifiers/eyes_lbp.xml")
    classifier_eyes2 = cv2.CascadeClassifier(r"classifiers/eye.xml")
    classifier_mouth = cv2.CascadeClassifier("classifiers/mouth.xml")
    classifier_nose = cv2.CascadeClassifier("classifiers/nose.xml")
    model = None
    X = None
    y = None
    emociones = ("Boredom","Engagement","Excitement","Frustration")
    #emociones = ("triste","enganchado", "emocionado", "frustrado")
    detector = dlib.get_frontal_face_detector()
    predictor = dlib.shape_predictor("data\shape_predictor_68_face_landmarks.dat")

    def __init__(self, modelPath=None, XPath = None, yPath = None, loadFiles=True):
        if loadFiles == True:
            self.model = cPickle.load(open(modelPath,"rb"))
            if XPath is not None:
                self.X = cPickle.load(open(XPath,"rb"))
            if yPath is not None:
                self.y = cPickle.load(open(yPath,"rb"))


    def predict(self, gray):
        returnValue = (False, "Rostro no encontrado")
        result, img = self.__get_image__(gray)
        if result:
            y = self.model.predict(img)
            returnValue = (True, self.emociones[y[0]])

        return returnValue
    
    def __get_image__(self, gray):
        roi_face = None
        roi_mouth = Roi()
        roi_nose = Roi()
        roi_eye_left = Roi()
        roi_eye_right = Roi()
        roi_eyebrown_left = Roi()
        roi_eyebrown_right = Roi()
        parts_founded = 0

        gray = cv2.resize(gray,(500,500))

        if False:
            faces = self.classifier_face.detectMultiScale(gray)
            for (x, y, w, h) in faces[:1]:
                roi_face = gray[y:y + h, x:x + w]

                # Parte posible de la nariz, probando con 1/3 rostro
                h_roi_face = roi_face.shape[0]
                w_roi_face = roi_face.shape[1]
                first_part_face = int(h_roi_face * .3)

                candidate_nose = roi_face[first_part_face:h_roi_face - first_part_face]

                noses = self.classifier_nose.detectMultiScale(candidate_nose)
                for (xNose, yNose, wNose, hNose) in noses[:1]:
                    roi_nose.x = xNose
                    roi_nose.y = first_part_face + yNose
                    roi_nose.w = wNose
                    roi_nose.h = hNose
                    roi_nose.image = candidate_nose[yNose:yNose + hNose, xNose:xNose + wNose]

                    parts_founded += 1

                    # A partir de donde termino la nariz, sacamos la parte restante para encontrar la boca
                    candidate_mouth = roi_face[first_part_face + yNose + hNose: h_roi_face - int(h_roi_face * .1)]

                    mouths = self.classifier_mouth.detectMultiScale(candidate_mouth)
                    for (xMouth, yMouth, wMouth, hMouth) in mouths[:1]:
                        roi_mouth.x = xMouth
                        roi_mouth.y = first_part_face + yNose + hNose + yMouth
                        roi_mouth.w = wMouth
                        roi_mouth.h = hMouth
                        roi_mouth.image = candidate_mouth[yMouth:yMouth + hMouth, xMouth:xMouth + wMouth]

                        parts_founded += 1

                    # A partir de donde comienza la nariz, sacamos la parte restante para tratar de encontrar los ojos
                    fix_forehead = int(h_roi_face * .15)
                    halfFace = int(w_roi_face / 2)
                    forehead = roi_face[fix_forehead:first_part_face + yNose]

                    # Dividimos la imagen en 2 para tratar de encontrar ojoz izquierdo y ojo derecho
                    candidate_eyeLeft = forehead[:, :halfFace]
                    candidate_eyeRight = forehead[:, halfFace:]

                    def searchEyeLeft(classifier, fix=1):
                        result = False
                        eyeLeft = classifier.detectMultiScale(candidate_eyeLeft)

                        if len(eyeLeft) > 0:
                            (xEyeLeft, yEyeLeft, wEyeLeft, hEyeLeft) = eyeLeft[0]
                            roi_eye_left.x = xEyeLeft
                            roi_eye_left.y = fix_forehead + yEyeLeft
                            roi_eye_left.w = wEyeLeft
                            roi_eye_left.h = hEyeLeft
                            roi_eye_left.image = candidate_eyeLeft[yEyeLeft:yEyeLeft + hEyeLeft,
                                                 xEyeLeft:xEyeLeft + wEyeLeft]

                            # A partir del ojo se cubre un area esperando que la ceja se encuentre ahí, el filtrado hará el trabajo de descubrirla despues
                            roi_eyebrown_left.w = xEyeLeft + int(wEyeLeft * 1.6)
                            roi_eyebrown_left.h = fix_forehead + int(yEyeLeft * fix)
                            roi_eyebrown_left.x = int(xEyeLeft * .5)
                            roi_eyebrown_left.y = fix_forehead + int(yEyeLeft * .3)

                            roi_eyebrown_left.image = gray[
                                                      y + fix_forehead + int(yEyeLeft * .3):y + fix_forehead + int(
                                                          yEyeLeft * fix),
                                                      x + int(xEyeLeft * .5):x + xEyeLeft + int(wEyeLeft * 1.6)]
                            result = True
                        return result

                    # Busqueda de ojo izquierdo con detector 1
                    resultEyeLeft = searchEyeLeft(self.classifier_eyes1)
                    if not resultEyeLeft:
                        # Busqueda de ojo derecho con detector 2
                        resultEyeLeft = searchEyeLeft(self.classifier_eyes2, 1.5)
                    parts_founded += resultEyeLeft

                    def searchEyeRight(classifier, fix=1):
                        result = False
                        eyeRight = classifier.detectMultiScale(candidate_eyeRight)

                        if len(eyeRight) > 0:
                            (xEyeRight, yEyeRight, wEyeRight, hEyeRight) = eyeRight[0]
                            roi_eye_right.x = halfFace + xEyeRight
                            roi_eye_right.y = fix_forehead + yEyeRight
                            roi_eye_right.w = wEyeRight
                            roi_eye_right.h = hEyeRight
                            roi_eye_right.image = candidate_eyeRight[yEyeRight:yEyeRight + hEyeRight,
                                                  xEyeRight:xEyeRight + wEyeRight]

                            roi_eyebrown_right.w = xEyeRight + int(wEyeRight * 1.6)
                            roi_eyebrown_right.h = fix_forehead + int(yEyeRight * fix)
                            roi_eyebrown_right.x = halfFace + int(xEyeRight * .5)
                            roi_eyebrown_right.y = fix_forehead + int(yEyeRight * .3)

                            roi_eyebrown_right.image = gray[y + fix_forehead + int(yEyeRight * .3):y + fix_forehead + int(
                                yEyeRight * 1.5), x + halfFace + int(xEyeRight * .5):x + halfFace + xEyeRight + int(
                                wEyeRight * fix)]

                            result = True
                        return result

                    # Busqueda de ojo derecho
                    resultEyeRight = searchEyeRight(self.classifier_eyes1)
                    if not resultEyeRight:
                        resultEyeRight = searchEyeRight(self.classifier_eyes2, 1.5)
                    parts_founded += resultEyeRight

        not_result = (False,[])

        #metodo normal con opencv no funciono
        if parts_founded < 4:
            #metodo con dlib
            dets = self.detector(gray, 1)

            if len(dets)>0:
                det = dets[0]
                #roi_face = gray[det.top():det.bottom(), det.left():det.right()]
                roi_face = gray.copy()
                shape = self.predictor(gray, det)
                points = shape.parts()

                #El proceso de ubicación de puntos no está estandarizado, se modifica para basarse en dlib
                # obtenidos los puntos, aplicamos los facial patches
                width_patch = roi_face.shape[1] / 18
                half_patch = width_patch / 2
                puntos = {}

                # Estas areas son los puntos que ya se encontraron
                puntos["p1"] = Roi.createROI(points[49].y , points[49].x)
                puntos["p4"] = Roi.createROI(points[55].y , points[55].x)
                puntos["p18"] = Roi.createROI(points[22].y , points[22].x)
                puntos["p19"] = Roi.createROI(points[23].y , points[23].x)
                # p16 va en la parte central de los ojos
                puntos["p16"] = Roi.createROI(
                    int(points[40].y + points[43].y / 2),
                    int(points[40].x + points[43].x / 2))
                # p17 va justo arriba de p16
                puntos["p17"] = Roi.createROI(puntos["p16"].y - width_patch, puntos["p16"].x)
                # Estos puntos estan justo debajo de p1 y p4
                puntos["p9"] = Roi.createROI(puntos["p1"].y + width_patch, puntos["p1"].x)
                puntos["p11"] = Roi.createROI(puntos["p4"].y + width_patch, puntos["p4"].x)
                # Este punto va justo en medio de p9 y p11
                puntos["p10"] = Roi.createROI((puntos["p9"].y + puntos["p11"].y) / 2,
                                              (puntos["p9"].x + puntos["p11"].x) / 2)
                # Estos puntos van justo debajo del ojo (parte central)
                puntos["p14"] = Roi.createROI(int(points[37].x + points[40].x / 2),
                                              int(points[37].y + points[40].y / 2))
                puntos["p15"] = Roi.createROI(int(points[43].x + points[46].x / 2),
                                              int(points[43].y + points[46].y / 2))
                # Lado izquierdo de la nariz
                puntos["p2"] = Roi.createROI(points[32].y , points[32].x-width_patch)
                puntos["p7"] = Roi.createROI(puntos["p2"].y, puntos["p2"].x - width_patch)  # justo a la izquierda de p2
                puntos["p8"] = Roi.createROI(puntos["p7"].y + width_patch, puntos["p7"].x)  # debajo de p7
                # Lado derecho de la nariz
                puntos["p5"] = Roi.createROI(points[36].y , points[36].x+width_patch)
                puntos["p13"] = Roi.createROI(puntos["p5"].y, puntos["p5"].x + width_patch)  # justo a la derecha de p5
                puntos["p12"] = Roi.createROI(puntos["p13"].y + width_patch, puntos["p13"].x)  # debajo de p13
                # punto central entre nariz y ojo
                puntos["p3"] = Roi.createROI(int(points[40].x + points[32].x / 2),
                                              int(points[40].y + points[32].y / 2))  # Izquierdo
                puntos["p6"] = Roi.createROI(int(points[36].x + points[43].x / 2),
                                              int(points[36].y + points[43].y / 2))  # Derecho

            else:
                #No funcionan ambos metodos, falta implementación de servició web
                return not_result
        else:
            # Preprocesamiento y localización de puntos
            #boca
            roi_mouth.localizationPoints()
            # ojos
            roi_eye_left.localizationPointsCenter()
            roi_eye_right.localizationPointsCenter()
            # nariz
            roi_nose.localizationPointsCenter()
            # Filtrado en ceja derecha y posicion izquierda y derecha
            roi_eyebrown_right.localizationPoints()
            # Filtrado en ceja izquierda y posicion izquierda y derecha
            roi_eyebrown_left.localizationPoints()

            # obtenidos los puntos, aplicamos los facial patches
            width_patch = roi_face.shape[1] / 9
            half_patch = width_patch / 2
            puntos = {}

            # Estas areas son los puntos que ya se encontraron
            puntos["p1"] = Roi.createROI(roi_mouth.y + roi_mouth.pointLeft[0], roi_mouth.x + roi_mouth.pointLeft[1])
            puntos["p4"] = Roi.createROI(roi_mouth.y + roi_mouth.pointRight[0], roi_mouth.x + roi_mouth.pointRight[1])
            puntos["p18"] = Roi.createROI((roi_eyebrown_left.y + roi_eyebrown_left.pointRight[0]),
                                          roi_eyebrown_left.x + roi_eyebrown_left.pointRight[1])
            puntos["p19"] = Roi.createROI((roi_eyebrown_right.y + roi_eyebrown_right.pointLeft[0]),
                                          roi_eyebrown_right.x + roi_eyebrown_right.pointLeft[1])
            # p16 va en la parte central de los ojos
            puntos["p16"] = Roi.createROI(
                int(((roi_eye_left.y + roi_eye_left.pointRight[1]) + roi_eye_right.y + roi_eye_right.pointLeft[1]) / 2),
                int(((roi_eye_left.x + roi_eye_left.pointRight[0]) + roi_eye_right.x + roi_eye_right.pointLeft[0]) / 2))
            # p17 va justo arriba de p16
            puntos["p17"] = Roi.createROI(puntos["p16"].y - width_patch, puntos["p16"].x)
            # Estos puntos estan justo debajo de p1 y p4
            puntos["p9"] = Roi.createROI(puntos["p1"].y + width_patch, puntos["p1"].x)
            puntos["p11"] = Roi.createROI(puntos["p4"].y + width_patch, puntos["p4"].x)
            # Este punto va justo en medio de p9 y p11
            puntos["p10"] = Roi.createROI((puntos["p9"].y + puntos["p11"].y) / 2, (puntos["p9"].x + puntos["p11"].x) / 2)
            # Estos puntos van justo debajo del ojo (parte central)
            puntos["p14"] = Roi.createROI(roi_eye_right.y + roi_eye_right.h + int(roi_eye_right.h / 2),
                                          roi_eye_right.x + int(roi_eye_right.w / 2))
            puntos["p15"] = Roi.createROI(roi_eye_left.y + roi_eye_left.h + int(roi_eye_left.h / 2),
                                          roi_eye_left.x + int(roi_eye_left.w / 2))
            # Lado izquierdo de la nariz
            puntos["p2"] = Roi.createROI(roi_nose.y + roi_nose.pointLeft[1],
                                         roi_nose.x + roi_nose.pointLeft[0] - half_patch)
            puntos["p7"] = Roi.createROI(puntos["p2"].y, puntos["p2"].x - width_patch)  # justo a la izquierda de p2
            puntos["p8"] = Roi.createROI(puntos["p7"].y + width_patch, puntos["p7"].x)  # debajo de p7
            # Lado derecho de la nariz
            puntos["p5"] = Roi.createROI(roi_nose.y + roi_nose.pointRight[1],
                                         roi_nose.x + roi_nose.pointRight[0] + half_patch)
            puntos["p13"] = Roi.createROI(puntos["p5"].y, puntos["p5"].x + width_patch)  # justo a la derecha de p5
            puntos["p12"] = Roi.createROI(puntos["p13"].y + width_patch, puntos["p13"].x)  # debajo de p13
            # punto central entre nariz y ojo
            puntos["p3"] = Roi.createROI(
                int(((roi_eye_left.y + roi_eye_left.pointRight[1]) + (roi_nose.y + roi_nose.pointLeft[1])) / 2),
                int(((roi_eye_left.x + roi_eye_left.pointRight[0]) + (roi_nose.x + roi_nose.pointLeft[0])) / 2))  # Izquierdo
            puntos["p6"] = Roi.createROI(
                int(((roi_eye_right.y + roi_eye_right.pointLeft[1]) + (roi_nose.y + roi_nose.pointRight[1])) / 2),
                int(((roi_eye_right.x + roi_eye_right.pointLeft[0]) + (roi_nose.x + roi_nose.pointRight[0])) / 2))  # Derecho

        #Extraemos cada punto y lo agregamos al arreglo
        face_array = np.asarray([], dtype=roi_face.dtype)
        try:
            for clavePunto in puntos:
                punto = puntos[clavePunto]
                squareBegin = punto.getSquareBegin(half_patch)
                squareEnd = punto.getSquareEnd(half_patch)
                facial_patch = roi_face[squareBegin[1]:squareEnd[1], squareBegin[0]:squareEnd[0]]
                if not facial_patch.shape == (26, 26):
                    return not_result
                #Se consigue el LBP
                lbp = local_binary_pattern(facial_patch, 8, 8, "uniform")
                #se consiguen los histogramas
                hist, _ = np.histogram(lbp, bins=256, range=(0, 256),normed=True)
                face_array = np.concatenate((face_array, hist)) #Se concatenan los histogramas
        except:
            print "Unexpected error:", sys.exc_info()[0]
            raise
        return (True, face_array)

    def __load_images__(self, path=None, rostrosPath=None):
        if path is None:
            path = "D:\\Subcorpus\\" #Las lineas son ambas importantes
        X, y = [], []
        rostros = []
        indice = -1
        for emocion in self.emociones:
            imagenes = glob.glob(path + emocion + "\\*.jpg")
            imagenes.extend(glob.glob(path+emocion+"\\*.png"))
            indice += 1
            last_image = ""
            try:
                for imagen in imagenes:
                    last_image = imagen
                    frame = cv2.imread(imagen)
                    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
                    result, array = self.__get_image__(gray)
                    if result:
                        X.append(array)
                        y.append(indice)
                        #rostros.append({"emocion":emocion, "imagen":frame})
            except IOError, (errno, strerror):
                print(last_image)
                print "I/O error({0}): {1}".format(errno, strerror)
            except:
                print(last_image)
                print "Unexpected error:", sys.exc_info()[0]
        if not rostrosPath is None:
            cPickle.dump(rostros, open(rostrosPath,"wb"), cPickle.HIGHEST_PROTOCOL)
        return [X, y]

    @staticmethod
    def create_model_training(savePath = None, XPath = None, yPath=None, path=None, rostrosPath=None):
        detector = detect_emotion(loadFiles=False)
        if XPath is None or yPath is None:
            X, y = detector.__load_images__(path, rostrosPath)
        else:
            X = cPickle.load(open("data/X.x","rb"))
            y = cPickle.load(open("data/y.y","rb"))
        detector.X = X
        detector.y = y
        #svm = LinearSVC()
        #svm.fit(X,y)
        #detector.model = svm
        #Aparentemente ésté clasificador dio mejores resultados
        clf = neighbors.KNeighborsClassifier(200, weights = 'distance')
        clf.fit(X,y)
        detector.model = clf
        cPickle.dump(clf, open("D:\modelo.m", "wb"))
        cPickle.dump(X, open("D:\X.x", "wb"))
        cPickle.dump(y, open("D:\y.y","wb"))
        return detector

    def crossValidation(self):
        scores = cross_val_score(self.model,self.X,self.y)
        print("Precisión: %0.2f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))
        #Matriz de confusión
        yTrue = map(lambda index: self.emociones[index], self.y)
        yPred = map(lambda index: self.emociones[index], self.model.predict(self.X))
        cm = confusion_matrix(yTrue, yPred, self.emociones)
        print(cm)

#detector = detect_emotion.create_model_training()
#detector.crossValidation()