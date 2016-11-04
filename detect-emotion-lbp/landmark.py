#!/usr/bin/python
import dlib
from skimage import io

predictor_path = r"D:\javasensei\detect-emotion-lbp\data\shape_predictor_68_face_landmarks.dat"

detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor(predictor_path)
win = dlib.image_window()

img = io.imread(r"D:\rostro.jpg")

win.clear_overlay()
win.set_image(img)

dets = detector(img, 1)

for k, d in enumerate(dets):
    print("Detection {}: Left: {} Top: {} Right: {} Bottom: {}".format(
        k, d.left(), d.top(), d.right(), d.bottom()))
    # Get the landmarks/parts for the face in box d.
    shape = predictor(img, d)
    print("Part 0: {}, Part 1: {} ...".format(shape.part(0),
                                              shape.part(1)))
    # Draw the face landmarks on the screen.
    win.add_overlay(shape)

win.add_overlay(dets)
dlib.hit_enter_to_continue()
