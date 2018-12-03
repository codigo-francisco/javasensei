from __future__ import print_function
import cv2
from facerec.model import PredictableModel
from detect_emotion import detect_emotion
import cPickle
import warnings
import cv2
import time

warnings.filterwarnings("ignore")

emociones = ("enojado", "feliz")#, "neutral", "sorpresa", "triste")
cascade_face = cv2.CascadeClassifier("classifiers/lbpcascade_frontalface.xml")

model = cPickle.load(open("data/modelo2.m","rb"))
video = cv2.VideoCapture(0)

while True:
    ret, frame = video.read()
    im = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    #im = clahe.apply(frame)

    faces = cascade_face.detectMultiScale(frame)
    for (x,y,w,h) in faces[:1]:
        im = im[y:y + h, x:x + w]
        im = cv2.resize(im, (100, 100))
        result = model.predict(im)
        emocion = emociones[result[0]]
        print(emocion, time.time())
        frame = cv2.putText(frame, emocion, (0,50), cv2.FONT_HERSHEY_SIMPLEX, 3, (0,0,0), 2, cv2.LINE_AA)

    cv2.imshow("frame", frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

video.release()
cv2.destroyAllWindows()