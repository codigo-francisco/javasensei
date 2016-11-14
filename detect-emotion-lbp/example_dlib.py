#!/usr/bin/python
import dlib
from skimage import io
import cv2

detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor("D:\dlib\python_examples\shape_predictor_68_face_landmarks.dat")

img = io.imread("D:\corpus\Engagement\emotion5.png")

dets = detector(img, 1)

for k, d in enumerate(dets):

    # Get the landmarks/parts for the face in box d.
    shape = predictor(img, d)

    punto = 1

    for point in shape.parts():
        print("Punto " + str(punto))

        cv2.circle(img, (point.x, point.y), 4, (255, 242, 120))
        cv2.imshow("imagen", img)
        cv2.waitKey()
        punto += 1