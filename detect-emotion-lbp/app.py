#!/usr/bin/env python
# -*- coding: utf-8 -*

import matplotlib.pyplot as plt
from mpl_toolkits.axes_grid1 import ImageGrid

from util import *

classifier_face = cv2.CascadeClassifier(r"classifiers\lbpcascade_frontalface.xml")
classifier_eyes = cv2.CascadeClassifier(r"classifiers\visionary_EYES_01_LBP_5k_7k_30x60.xml")
classifier_mouth = cv2.CascadeClassifier(
    r"D:\OpenCV-Face-andmore-Tracker\Face(andmore)Tracker\Resources\haarcascades\mouth.xml")
classifier_nose = cv2.CascadeClassifier(
    r"D:\OpenCV-Face-andmore-Tracker\Face(andmore)Tracker\Resources\haarcascades\nose.xml")

# detectar cara desde camara
video = cv2.VideoCapture(2)

# roi of interest
roi_mouth = Roi()
roi_nose = Roi()
roi_eyebrown_left = Roi()
roi_eyebrown_right = Roi()
parts_founded = 0
original = None

while parts_founded < 4:
    parts_founded = 0
    ret, frame = video.read()
    # frame = cv2.imread("D:\\cara2.jpg")

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # detectar rostro
    faces = classifier_face.detectMultiScale(gray)
    for (x, y, w, h) in faces[:1]:
        original = frame[y:y + h, x:x + w].copy()
        frame = cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 0, 0))

        roi_face = gray[y:y + h, x:x + w]

        # Parte posible de la nariz, probando con 1/3 rostro
        h_roi_face = roi_face.shape[0]
        w_roi_face = roi_face.shape[1]
        first_part_face = int(h_roi_face * .3)
        area_nose_face = int(h_roi_face * .1)

        candidate_nose = roi_face[first_part_face:h_roi_face - (first_part_face)]

        noses = classifier_nose.detectMultiScale(candidate_nose)
        for (xNose, yNose, wNose, hNose) in noses[:1]:
            frame = cv2.rectangle(frame, (x + xNose, y + first_part_face + yNose),
                                  (x + xNose + wNose, y + first_part_face + yNose + hNose), (0, 255, 0))

            roi_nose.x = x
            roi_nose.y = y
            roi_nose.w = w
            roi_nose.h = h
            roi_nose.image = candidate_nose[yNose:yNose + hNose, xNose:xNose + wNose]

            parts_founded += 1

            # A partir de donde termino la nariz, sacamos la parte restante para encontrar la boca
            candidate_mouth = roi_face[first_part_face + yNose + hNose:]
            mouths = classifier_mouth.detectMultiScale(candidate_mouth)
            for (xMouth, yMouth, wMouth, hMouth) in mouths[:1]:
                frame = cv2.rectangle(frame, (x + xMouth, y + first_part_face + yNose + hNose + yMouth),
                                      (x + xMouth + wMouth, y + first_part_face + yNose + hNose + yMouth + hMouth),
                                      (0, 0, 255))

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

            # Busqueda de ojo izquierdo
            eyeLeft = classifier_eyes.detectMultiScale(candidate_eyeLeft)
            if len(eyeLeft) > 0:
                (xEyeLeft, yEyeLeft, wEyeLeft, hEyeLeft) = eyeLeft[0]
                frame = cv2.rectangle(frame, (x + xEyeLeft, y + fix_forehead + yEyeLeft),
                                      (x + xEyeLeft + wEyeLeft, y + fix_forehead + yEyeLeft + hEyeLeft),
                                      (150, 150, 200))
                roi_eye_left = candidate_eyeLeft[yEyeLeft:yEyeLeft + hEyeLeft, xEyeLeft:xEyeLeft + wEyeLeft]

                # A partir del ojo se cubre un area esperando que la ceja se encuentre ahí, el filtrado hará el trabajo de descubrirla despues
                frame = cv2.rectangle(frame, (x + int(xEyeLeft * .5), y + fix_forehead + int(yEyeLeft * .3)),
                                      (x + xEyeLeft + int(wEyeLeft * 1.6), y + fix_forehead + yEyeLeft), (0, 0, 0))

                roi_eyebrown_left.w = x + xEyeLeft + int(wEyeLeft * 1.6) - (x + int(xEyeLeft * .5))
                roi_eyebrown_left.h = y + fix_forehead + yEyeLeft - (y + fix_forehead + int(yEyeLeft * .3))
                roi_eyebrown_left.x = int(xEyeLeft * .5) + int(
                    roi_eyebrown_left.w / 2)  # Calcular punto medio del rectangulo
                roi_eyebrown_left.y = fix_forehead + int(yEyeLeft * .3) + int(roi_eyebrown_left.h / 2)

                roi_eyebrown_left.image = gray[
                                          y + fix_forehead + int(yEyeLeft * .3) + 20:y + fix_forehead + yEyeLeft + 20,
                                          x + int(xEyeLeft * .5):x + xEyeLeft + int(wEyeLeft * 1.6)]
                parts_founded += 1

            # Busqueda de ojo derecho
            eyeRight = classifier_eyes.detectMultiScale(candidate_eyeRight)
            if len(eyeRight) > 0:
                (xEyeRight, yEyeRight, wEyeRight, hEyeRight) = eyeRight[0]
                frame = cv2.rectangle(frame, (x + halfFace + xEyeRight, y + fix_forehead + yEyeRight),
                                      (x + halfFace + xEyeRight + wEyeRight, y + fix_forehead + yEyeRight + hEyeRight),
                                      (150, 150, 200))
                roi_eye_right = candidate_eyeRight[yEyeRight:yEyeRight + hEyeRight, xEyeRight:xEyeRight + wEyeRight]

                frame = cv2.rectangle(frame,
                                      (x + halfFace + int(xEyeRight * .5), y + fix_forehead + int(yEyeRight * .3)),
                                      (x + halfFace + xEyeRight + int(wEyeRight * 1.6), y + fix_forehead + yEyeRight),
                                      (0, 0, 0))

                roi_eyebrown_right.w = x + xEyeRight + int(wEyeRight * 1.6) - (x + int(xEyeRight * .5))
                roi_eyebrown_right.h = y + fix_forehead + yEyeRight - (y + fix_forehead + int(yEyeRight * .3))
                roi_eyebrown_right.x = int(xEyeRight * .5) + int(roi_eyebrown_right.w / 2)
                roi_eyebrown_right.y = fix_forehead + int(yEyeRight * .3) + int(roi_eyebrown_right.h / 2)

                roi_eyebrown_right.image = gray[y + fix_forehead + int(yEyeRight * .3):y + fix_forehead + yEyeRight,
                                     x + halfFace + int(xEyeRight * .5):x + halfFace + xEyeRight + int(wEyeRight * 1.6)]

                parts_founded += 1

    cv2.imshow("frame", frame)

    if cv2.waitKey(1) & 0xFF == ord("q"):
        video.release()
        break

if parts_founded == 4:
    # Preprocesamiento y extracción de caractersiticas
    # Filtrado en boca y posicion izquierda y derecha
    roi_mouth.filter, filters_mouth = filtrarImagen(roi_mouth.image)
    roi_mouth.pointLeft, roi_mouth.pointRight = obtenerPuntos(roi_mouth.filter)
    # Filtrado en ceja derecha y posicion izquierda y derecha
    roi_eyebrown_right.filter, filters_eyebrown_right = filtrarImagen(roi_eyebrown_right.image)
    roi_eyebrown_right.pointLeft, roi_eyebrown_right.pointRight = obtenerPuntos(roi_eyebrown_right.filter)
    # Filtrado en ceja izquierda y posicion izquierda y derecha
    roi_eyebrown_left.filter, filters_eyebrown_left = filtrarImagen(roi_eyebrown_left.image)
    roi_eyebrown_left.pointLeft, roi_eyebrown_left.pointRight = obtenerPuntos(roi_eyebrown_left.filter)

    # Parte de la presentación, los filtros
    fig = plt.figure(1, (8., 6.))
    grid = ImageGrid(fig, 111,
                     nrows_ncols=(7, 3),
                     axes_pad=0.1
                     )

    # agregamos los filtros
    for index, filter in zip(range(0, len(filters_mouth) * 3, 3), filters_mouth):
        grid[index].imshow(filter, "gray")
    for index, filter in zip(range(1, len(filters_eyebrown_left) * 3, 3), filters_eyebrown_left):
        grid[index].imshow(filter, "gray")
    for index, filter in zip(range(2, len(filters_eyebrown_right) * 3, 3), filters_eyebrown_right):
        grid[index].imshow(filter, "gray")

    plt.show()

    # obtenidos los puntos, aplicamos los facial patches... por ahora solo 3
    width_patch = roi_face.shape[1] / 9
    half_patch = width_patch / 2
    color_square = (255, 255, 0)
    fix_eyebrow = 20

    # Estas areas son los puntos que ya se encontraron
    p1 = Roi.createROI(roi_mouth.y + roi_mouth.pointLeft[0], roi_mouth.x + roi_mouth.pointLeft[1])
    p4 = Roi.createROI(roi_mouth.y + roi_mouth.pointRight[0], roi_mouth.x + roi_mouth.pointRight[1])
    p18 = Roi.createROI((roi_eyebrown_left.y + roi_eyebrown_left.pointRight[0]) - fix_eyebrow,
                        roi_eyebrown_left.x + roi_eyebrown_left.pointRight[1])
    p19 = Roi.createROI((roi_eyebrown_right.y + roi_eyebrown_right.pointLeft[0]) - fix_eyebrow,
                        roi_eyebrown_right.x + roi_eyebrown_right.pointLeft[1])
    # Este punto va en medio de p18 y p19
    p17 = Roi.createROI((p18.y + p19.y) / 2, (p18.x + p19.x) / 2)
    # Este punto va justo debajo de p16
    p16 = Roi.createROI(p17.y + width_patch, p17.x)
    # Estos puntos estan justo debajo de p1 y p4
    p9 = Roi.createROI(p1.y + width_patch, p1.x)
    p11 = Roi.createROI(p4.y + width_patch, p4.x)
    # Este punto va justo en medio de p9 y p11
    p10 = Roi.createROI((p9.y + p11.y) / 2, (p9.x + p11.x) / 2)

    # Dibujado de los cuadrados
    original = cv2.rectangle(original, p1.getSquareBegin(half_patch), p1.getSquareEnd(half_patch), color_square)
    original = cv2.rectangle(original, p4.getSquareBegin(half_patch), p4.getSquareEnd(half_patch), color_square)
    original = cv2.rectangle(original, p18.getSquareBegin(half_patch), p18.getSquareEnd(half_patch), color_square)
    original = cv2.rectangle(original, p19.getSquareBegin(half_patch), p19.getSquareEnd(half_patch), color_square)

    original = cv2.rectangle(original, p16.getSquareBegin(half_patch), p16.getSquareEnd(half_patch), color_square)
    original = cv2.rectangle(original, p17.getSquareBegin(half_patch), p17.getSquareEnd(half_patch), color_square)

    original = cv2.rectangle(original, p9.getSquareBegin(half_patch), p9.getSquareEnd(half_patch), color_square)
    original = cv2.rectangle(original, p11.getSquareBegin(half_patch), p11.getSquareEnd(half_patch), color_square)
    original = cv2.rectangle(original, p10.getSquareBegin(half_patch), p10.getSquareEnd(half_patch), color_square)

    plt.imshow(original)
    plt.show()
