#!/usr/bin/env python
# -*- coding: utf-8 -*

import dlib
import cv2
import numpy as np

detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor("D:\javasensei\detect-emotion-lbp\data\shape_predictor_68_face_landmarks.dat")

#cargar imagen
img = cv2.imread("D:\corpus\Engagement\emotion1.png")

detections = detector(img, 1) #Detect the faces in the image

for k, d in enumerate(detections):  # For each detected face
    shape = predictor(img, d)  # Get coordinates
    xlist = []
    ylist = []
    for i in range(1, 68):  # There are 68 landmark points on each face
        cv2.circle(img, (shape.part(i).x, shape.part(i).y), 1, (0, 0, 255), thickness=2)
        xlist.append(float(shape.part(i).x))
        ylist.append(float(shape.part(i).y))

    xmean = np.mean(xlist)  # Get the mean of both axes to determine centre of gravity
    ymean = np.mean(ylist)

    #dibujado de linea
    #for x,y in zip(xlist,ylist):
        #cv2.line(img, (int(xmean), int(ymean)), (int(x),int(y)),(0,255,0),1)

    cv2.circle(img, (int(xmean), int(ymean)), 2, (255, 0, 0), 10)

cv2.imshow("",img)
cv2.waitKey()

