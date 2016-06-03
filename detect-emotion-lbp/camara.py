from __future__ import print_function
import cv2
from detect_emotion import detect_emotion
import time
import warnings

warnings.filterwarnings("ignore")

video = cv2.VideoCapture(0)
detector = detect_emotion("data/prueba.m","data/X.x","data/y.y")

while True:
    ret, frame = video.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    result, prediction = detector.predict(gray)

    if result:
        print(prediction, time.time())
        frame = cv2.putText(frame, prediction, (0, 50), cv2.FONT_HERSHEY_SIMPLEX, 3, (0, 0, 0), 2, cv2.LINE_AA)

    cv2.imshow("frame", frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

video.release()
cv2.destroyAllWindows()