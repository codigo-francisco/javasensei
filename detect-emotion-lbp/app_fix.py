#!/usr/bin/env python
# -*- coding: utf-8 -*

import matplotlib.pyplot as plt
from mpl_toolkits.axes_grid1 import ImageGrid
from skimage.feature import local_binary_pattern

from util import *

classifier_face = cv2.CascadeClassifier(r"classifiers/lbpcascade_frontalface.xml")
classifier_eyes1 = cv2.CascadeClassifier(r"classifiers/eyes_lbp.xml")
classifier_eyes2 = cv2.CascadeClassifier(r"classifiers/eye.xml")
classifier_mouth = cv2.CascadeClassifier(r"classifiers/mouth.xml")
classifier_nose = cv2.CascadeClassifier(r"classifiers/nose.xml")

# roi of interest
roi_mouth = Roi()
roi_nose = Roi()
roi_eye_left = Roi()
roi_eye_right = Roi()
roi_eyebrown_left = Roi()
roi_eyebrown_right = Roi()
parts_founded = 0
original = None

#frame = cv2.imread("D:\\1.jpg")
frame = cv2.imread(r"D:\javasensei\Subcorpus\Boredom\emotion5299.png")
gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

cv2.imshow("Cara", frame)
cv2.waitKey()

# detectar rostro
faces = classifier_face.detectMultiScale(gray)
for (x, y, w, h) in faces[:1]:
    original = frame[y:y + h, x:x + w].copy()
    frame = cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 0, 0))
    roi_face = gray[y:y + h, x:x + w]
    original_gray = roi_face.copy()

    cv2.imshow("Deteccion del rostro", frame)
    cv2.waitKey()

    # Parte posible de la nariz, probando con 1/3 rostro
    h_roi_face = roi_face.shape[0]
    w_roi_face = roi_face.shape[1]
    first_part_face = int(h_roi_face * .3)

    candidate_nose = roi_face[first_part_face:h_roi_face - first_part_face]

    #dibujado de candidato de nariz
    nariz = cv2.rectangle(frame.copy(), (x,y+first_part_face),(x+w_roi_face,y+(h_roi_face - first_part_face)),(0,0,0))
    cv2.imshow("Candidato nariz",nariz)
    cv2.waitKey()

    noses = classifier_nose.detectMultiScale(candidate_nose)
    for (xNose, yNose, wNose, hNose) in noses[:1]:
        roi_nose.x = xNose
        roi_nose.y = first_part_face + yNose
        roi_nose.w = wNose
        roi_nose.h = hNose
        roi_nose.image = candidate_nose[yNose:yNose + hNose, xNose:xNose + wNose]

        parts_founded += 1

        # dibujado nariz
        frame = cv2.rectangle(frame, (x + xNose, y + first_part_face + yNose),
                              (x + xNose + wNose, y + first_part_face + yNose + hNose), (0, 255, 0))
        cv2.imshow("Nariz", frame)
        cv2.waitKey()

        # A partir de donde termino la nariz, sacamos la parte restante para encontrar la boca
        candidate_mouth = roi_face[first_part_face + yNose + hNose: h_roi_face - int(h_roi_face*.1)]

        #dibujado candidato boca
        boca = cv2.rectangle(frame.copy(), (x,y+first_part_face+yNose+hNose),(x+w_roi_face,y + h_roi_face - int(h_roi_face*.1)),(0,0,0))
        cv2.imshow("Candidato boca", boca)
        cv2.waitKey()

        mouths = classifier_mouth.detectMultiScale(candidate_mouth)
        for (xMouth, yMouth, wMouth, hMouth) in mouths[:1]:
            roi_mouth.x = xMouth
            roi_mouth.y = first_part_face + yNose + hNose + yMouth
            roi_mouth.w = wMouth
            roi_mouth.h = hMouth
            roi_mouth.image = candidate_mouth[yMouth:yMouth + hMouth, xMouth:xMouth + wMouth]

            parts_founded += 1

            #dibujado de la boca
            frame = cv2.rectangle(frame, (x + xMouth, y + first_part_face + yNose + hNose + yMouth),
                              (x + xMouth + wMouth, y + first_part_face + yNose + hNose + yMouth + hMouth),
                              (0, 0, 255))

            cv2.imshow("Boca", frame)
            cv2.waitKey()

        # A partir de donde comienza la nariz, sacamos la parte restante para tratar de encontrar los ojos
        fix_forehead = int(h_roi_face * .15)
        halfFace = int(w_roi_face / 2)
        forehead = roi_face[fix_forehead:first_part_face + yNose]

        #dibujado de la frente
        frente = cv2.rectangle(frame.copy(),(x,y+fix_forehead),(x+w_roi_face, y+first_part_face+yNose),(0,0,0))
        cv2.imshow("Frente", frente)
        cv2.waitKey()

        # Dividimos la imagen en 2 para tratar de encontrar ojoz izquierdo y ojo derecho
        candidate_eyeLeft = forehead[:, :halfFace]
        candidate_eyeRight = forehead[:, halfFace:]

        #dibujado de candidato a ojo izquierdo
        ojoIzquierdo = cv2.rectangle(frame.copy(),(x, y+fix_forehead),(x+halfFace,y+first_part_face+yNose),(0,0,0))
        cv2.imshow("Candidato ojo izquierdo", ojoIzquierdo)
        cv2.waitKey()

        def searchEyeLeft(classifier, frame, fix=1):
            result = False
            eyeLeft = classifier.detectMultiScale(candidate_eyeLeft)

            if len(eyeLeft) > 0:
                (xEyeLeft, yEyeLeft, wEyeLeft, hEyeLeft) = eyeLeft[0]
                roi_eye_left.x = xEyeLeft
                roi_eye_left.y = fix_forehead + yEyeLeft
                roi_eye_left.w = wEyeLeft
                roi_eye_left.h = hEyeLeft
                roi_eye_left.image = candidate_eyeLeft[yEyeLeft:yEyeLeft + hEyeLeft, xEyeLeft:xEyeLeft + wEyeLeft]

                # Dibujado del ojo izquierdo
                cv2.rectangle(frame, (x + xEyeLeft, y + fix_forehead + yEyeLeft),
                                      (x + xEyeLeft + wEyeLeft, y + fix_forehead + yEyeLeft + hEyeLeft),
                                      (30, 30, 50))
                cv2.imshow("Ojo izquierdo", frame)
                cv2.waitKey()

                # A partir del ojo se cubre un area esperando que la ceja se encuentre ahí, el filtrado hará el trabajo de descubrirla despues
                roi_eyebrown_left.w = xEyeLeft + int(wEyeLeft * 1.6)
                roi_eyebrown_left.h = fix_forehead + int(yEyeLeft * fix)
                roi_eyebrown_left.x = int(xEyeLeft * .5)
                roi_eyebrown_left.y = fix_forehead + int(yEyeLeft * .3)

                roi_eyebrown_left.image = gray[
                                          y + fix_forehead + int(yEyeLeft * .3):y + fix_forehead + int(yEyeLeft * fix),
                                          x + int(xEyeLeft * .5):x + xEyeLeft + int(wEyeLeft * 1.6)]
                result = True

                # Dibujado de candidato a ceja
                frame = cv2.rectangle(frame, (x + int(xEyeLeft * .5), y + fix_forehead + int(yEyeLeft * .3)),
                                      (x + xEyeLeft + int(wEyeLeft * 1.6), y + fix_forehead + int(yEyeLeft * fix)),
                                      (0, 0, 0))

                cv2.imshow("Ceja izquierda", frame)
                cv2.waitKey()

            return result

        # Busqueda de ojo izquierdo con detector 1
        resultEyeLeft = searchEyeLeft(classifier_eyes1, frame)
        if not resultEyeLeft:
            #Busqueda de ojo derecho con detector 2
            resultEyeLeft = searchEyeLeft(classifier_eyes2, frame, 1.5)
        parts_founded += resultEyeLeft

        #dibujado de candidato a ojo derecho
        ojoDerecho = cv2.rectangle(frame.copy(), (x + halfFace, y + fix_forehead),
                                   (x + w_roi_face, y + first_part_face + yNose), (0, 0, 0))
        cv2.imshow("Candidado ojo derecho", ojoDerecho)
        cv2.waitKey()

        def searchEyeRight(classifier, frame, fix = 1):
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

                # Dibujado del ojo derecho
                frame = cv2.rectangle(frame, (x + halfFace + xEyeRight, y + fix_forehead + yEyeRight),
                                      (x + halfFace + xEyeRight + wEyeRight, y + fix_forehead + yEyeRight + hEyeRight),
                                      (30, 30, 50))

                cv2.imshow("Ojo derecho", frame)
                cv2.waitKey()

                roi_eyebrown_right.w = xEyeRight + int(wEyeRight * 1.6)
                roi_eyebrown_right.h = fix_forehead + int(yEyeRight * fix)
                roi_eyebrown_right.x = halfFace + int(xEyeRight * .5)
                roi_eyebrown_right.y = fix_forehead + int(yEyeRight * .3)

                roi_eyebrown_right.image = gray[y + fix_forehead + int(yEyeRight * .3):y + fix_forehead + int(
                    yEyeRight * 1.5), x + halfFace + int(xEyeRight * .5):x + halfFace + xEyeRight + int(
                                               wEyeRight * fix)]

                result = True

                frame = cv2.rectangle(frame,
                                      (x + halfFace + int(xEyeRight * .5), y + fix_forehead + int(yEyeRight * .3)),
                                      (x + halfFace + xEyeRight + int(wEyeRight * fix),
                                       y + fix_forehead + int(yEyeRight * 1.5)),
                                      (0, 0, 0))
                cv2.imshow("Ceja derecha", frame)
                cv2.waitKey()
            return result
        # Busqueda de ojo derecho
        resultEyeRight = searchEyeRight(classifier_eyes1, frame)
        if not resultEyeRight:
            resultEyeRight = searchEyeRight(classifier_eyes2, frame, 1.5)
        parts_founded += resultEyeRight

# Preprocesamiento y extracción de caractersiticas
# Filtrado en boca y posicion izquierda y derecha
filters_mouth = roi_mouth.localizationPoints()
#ojos
filters_eye_left = roi_eye_left.localizationPointsCenter()
filters_eye_right = roi_eye_right.localizationPointsCenter()
#nariz
filters_nose = roi_nose.localizationPointsCenter()
# Filtrado en ceja derecha y posicion izquierda y derecha
filters_eyebrown_right = roi_eyebrown_right.localizationPoints()
# Filtrado en ceja izquierda y posicion izquierda y derecha
filters_eyebrown_left = roi_eyebrown_left.localizationPoints()

#Parte de la presentación, los filtros
fig = plt.figure(1, (8., 6.))
grid = ImageGrid(fig, 111, nrows_ncols=(7, 3), axes_pad=0.1)
# agregamos los filtros
for index, filter in zip(range(0, 21, 3), filters_mouth):
    grid[index].imshow(filter, "gray")
for index, filter in zip(range(1, 21, 3), filters_eyebrown_left):
    grid[index].imshow(filter, "gray")
for index, filter in zip(range(2, 21, 3), filters_eyebrown_right):
    grid[index].imshow(filter, "gray")
plt.show()

#Se muestran la localización de los facial landmarks
originalLandMark = original.copy()
radius = 5
colorLandMark = (0,0,0)

cv2.circle(originalLandMark,(roi_mouth.x+roi_mouth.pointLeft[1], roi_mouth.y+roi_mouth.pointLeft[0]),radius, colorLandMark,-1)
cv2.circle(originalLandMark,(roi_mouth.x+roi_mouth.pointRight[1], roi_mouth.y+roi_mouth.pointRight[0]),radius, colorLandMark,-1)

cv2.circle(originalLandMark,(roi_eyebrown_left.x+roi_eyebrown_left.pointLeft[1], roi_eyebrown_left.y+roi_eyebrown_left.pointLeft[0]),radius, colorLandMark,-1)
cv2.circle(originalLandMark,(roi_eyebrown_left.x+roi_eyebrown_left.pointRight[1], roi_eyebrown_left.y+roi_eyebrown_left.pointRight[0]),radius, colorLandMark,-1)

cv2.circle(originalLandMark,(roi_eyebrown_right.x+roi_eyebrown_right.pointLeft[1], roi_eyebrown_right.y+roi_eyebrown_right.pointLeft[0]),radius, colorLandMark,-1)
cv2.circle(originalLandMark,(roi_eyebrown_right.x+roi_eyebrown_right.pointRight[1], roi_eyebrown_right.y+roi_eyebrown_right.pointRight[0]),radius, colorLandMark,-1)

cv2.circle(originalLandMark,(roi_nose.x + roi_nose.pointLeft[0] , roi_nose.y + roi_nose.pointLeft[1], ), radius, colorLandMark, -1)
cv2.circle(originalLandMark,(roi_nose.x + roi_nose.pointRight[0] , roi_nose.y + roi_nose.pointRight[1], ), radius, colorLandMark, -1)

cv2.circle(originalLandMark,(roi_eye_left.x + roi_eye_left.pointLeft[0] , roi_eye_left.y + roi_eye_left.pointLeft[1], ), radius, colorLandMark, -1)
cv2.circle(originalLandMark,(roi_eye_left.x + roi_eye_left.pointRight[0] , roi_eye_left.y + roi_eye_left.pointRight[1], ), radius, colorLandMark, -1)

cv2.circle(originalLandMark,(roi_eye_right.x + roi_eye_right.pointLeft[0] , roi_eye_right.y + roi_eye_right.pointLeft[1], ), radius, colorLandMark, -1)
cv2.circle(originalLandMark,(roi_eye_right.x + roi_eye_right.pointRight[0] , roi_eye_right.y + roi_eye_right.pointRight[1], ), radius, colorLandMark, -1)

cv2.imshow("Facial landmarks", originalLandMark)
cv2.waitKey()

# obtenidos los puntos, aplicamos los facial patches...
width_patch = roi_face.shape[1] / 9
half_patch = width_patch / 2
color_square = (0, 0, 0)
puntos = {}

# Estas areas son los puntos que ya se encontraron
puntos["p1"] = Roi.createROI(roi_mouth.y + roi_mouth.pointLeft[0], roi_mouth.x + roi_mouth.pointLeft[1])
puntos["p4"] = Roi.createROI(roi_mouth.y + roi_mouth.pointRight[0], roi_mouth.x + roi_mouth.pointRight[1])
puntos["p18"] = Roi.createROI((roi_eyebrown_left.y + roi_eyebrown_left.pointRight[0]),
                    roi_eyebrown_left.x + roi_eyebrown_left.pointRight[1])
puntos["p19"] = Roi.createROI((roi_eyebrown_right.y + roi_eyebrown_right.pointLeft[0]),
                    roi_eyebrown_right.x + roi_eyebrown_right.pointLeft[1])
# p16 va en la parte central de los ojos
puntos["p16"] = Roi.createROI(int(((roi_eye_left.y+roi_eye_left.pointRight[1])+roi_eye_right.y+roi_eye_right.pointLeft[1])/2),
                    int(((roi_eye_left.x+roi_eye_left.pointRight[0])+roi_eye_right.x+roi_eye_right.pointLeft[0])/2))
# p17 va justo arriba de p16
puntos["p17"] = Roi.createROI(puntos["p16"].y - width_patch, puntos["p16"].x)
# Estos puntos estan justo debajo de p1 y p4
puntos["p9"] = Roi.createROI(puntos["p1"].y + width_patch, puntos["p1"].x)
puntos["p11"] = Roi.createROI(puntos["p4"].y + width_patch, puntos["p4"].x)
# Este punto va justo en medio de p9 y p11
puntos["p10"] = Roi.createROI((puntos["p9"].y + puntos["p11"].y) / 2, (puntos["p9"].x + puntos["p11"].x) / 2)
#Estos puntos van justo debajo del ojo (parte central)
puntos["p14"] = Roi.createROI(roi_eye_right.y + roi_eye_right.h + int(roi_eye_right.h/2), roi_eye_right.x+int(roi_eye_right.w/2))
puntos["p15"] = Roi.createROI(roi_eye_left.y + roi_eye_left.h + int(roi_eye_left.h/2), roi_eye_left.x+int(roi_eye_left.w/2))
#Lado izquierdo de la nariz
puntos["p2"] = Roi.createROI(roi_nose.y + roi_nose.pointLeft[1], roi_nose.x + roi_nose.pointLeft[0]-half_patch)
puntos["p7"] = Roi.createROI(puntos["p2"].y, puntos["p2"].x-width_patch) #justo a la izquierda de p2
puntos["p8"] = Roi.createROI(puntos["p7"].y + width_patch, puntos["p7"].x)#debajo de p7
#Lado derecho de la nariz
puntos["p5"] = Roi.createROI(roi_nose.y + roi_nose.pointRight[1], roi_nose.x + roi_nose.pointRight[0]+half_patch)
puntos["p13"] = Roi.createROI(puntos["p5"].y, puntos["p5"].x + width_patch) #justo a la derecha de p5
puntos["p12"] = Roi.createROI(puntos["p13"].y + width_patch, puntos["p13"].x)#debajo de p13
#punto central entre nariz y ojo
puntos["p3"] = Roi.createROI(int(((roi_eye_left.y + roi_eye_left.pointRight[1])+(roi_nose.y + roi_nose.pointLeft[1]))/2),
                             int(((roi_eye_left.x +roi_eye_left.pointRight[0])+(roi_nose.x + roi_nose.pointLeft[0]))/2)) #Izquierdo
puntos["p6"] = Roi.createROI(int(((roi_eye_right.y + roi_eye_right.pointLeft[1]) + (roi_nose.y + roi_nose.pointRight[1])) / 2),
                            int(((roi_eye_right.x + roi_eye_right.pointLeft[0]) + (roi_nose.x + roi_nose.pointRight[0])) / 2))#Derecho

# Dibujado de los cuadrados
originalFacialPatches = original.copy()
for pKey in puntos:
    pValue = puntos[pKey]
    cv2.rectangle(originalFacialPatches, pValue.getSquareBegin(half_patch), pValue.getSquareEnd(half_patch), color_square)
plt.imshow(originalFacialPatches)
plt.show()

#Dibujado del operador LBP
grid = ImageGrid(plt.figure(1), 111, (5, 4), 19, axes_pad=.5)
lbps = []
original_gray_lbp = original_gray.copy()
for index, pKey in zip(range(len(puntos)),puntos):
    pValue = puntos[pKey]
    image = original_gray_lbp[pValue.y:pValue.y + half_patch, pValue.x:pValue.x + half_patch]
    lbp = local_binary_pattern(image, 8, 8, "uniform")
    lbps.append(lbp)
    grid[index].imshow(lbp)
    grid[index].set_title(pKey)
plt.show()

#Dibujado de histogramas
fig = plt.figure()
for index,lbp in zip(range(1,3),lbps[:2]):
    ax = fig.add_subplot(1,2,index)
    ax.hist(lbp.ravel(),256,normed=True)
plt.show()

