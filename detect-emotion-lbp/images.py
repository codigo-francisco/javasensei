import cPickle
import glob

import cv2

path = "D:/Respaldo Jose Luis/proyecto RVERK/RafD_Ordenado/"
emociones = ("enojado", "feliz", "neutral", "sorpresa", "triste")

X=[]
for emocion in emociones:
    imagenes = glob.glob(path+emocion+"\\*.jpg")
    for imagen in imagenes:
        X.append({"emocion":emocion, "imagen":cv2.imread(imagen)})

cPickle.dump(X, open("data/imagenes_emocion.sav","wb"), cPickle.HIGHEST_PROTOCOL)