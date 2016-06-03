import cv2
import numpy as np
from skimage.morphology import (binary_dilation, label, remove_small_objects)


def filtrarImagen(image):
    filtros = []
    filtros.append(image)
    gauss = cv2.GaussianBlur(image, (3, 3), 0)
    filtros.append(gauss)
    sobel = cv2.Sobel(gauss, cv2.CV_8U, 0, 1)
    filtros.append(sobel)
    _, otsu = cv2.threshold(sobel, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
    filtros.append(otsu)
    dilation = binary_dilation(otsu)
    filtros.append(dilation)
    labels, num = label(dilation, return_num=True)
    sizes = np.bincount(labels.ravel())
    max = 0
    for size in sizes[1:]:
        if size > max:
            max = size
    largest = remove_small_objects(labels, max - 1)
    filtros.append(largest)
    return (largest, filtros)

def obtenerCejaROI(roi_face, x, y, w, h):
    roi = Roi()
    roi.x = x  # *.9
    roi.y = (y - (h * 1.2))
    roi.h = (h * 3) / 3
    roi.w = round(float(w) * 1.8)
    roi.image = roi_face[roi.y:roi.y + roi.h, roi.x: roi.x + roi.w]
    return roi

def obtenerPuntos(roi_filter):
    indexesX = range(roi_filter.shape[1])
    indexesY = range(roi_filter.shape[0])
    left_position = obtenerPosicion(roi_filter, indexesX=indexesX, indexesY=indexesY)

    indexesX = range(roi_filter.shape[1] - 1, -1, -1)  # De fin a 0
    indexesY = range(roi_filter.shape[0] - 1, -1, -1)
    right_position = obtenerPosicion(roi_filter, indexesX=indexesX, indexesY=indexesY)

    return [left_position, right_position]

def obtenerPosicion(array, indexesX, indexesY):
    result = []
    for indexX in indexesX:
        for indexY in indexesY:
            # print indiceY, indiceX, m[indiceY,indiceX]
            if array[indexY, indexX] != 0:
                result = [indexY, indexX]
                break
        else:
            continue
        break

    return result

class Roi:
    x = 0
    y = 0
    h = 0
    w = 0
    image = None
    filter = None
    pointLeft = None
    pointRight = None

    @staticmethod
    def createROI(y, x, w=None):
        roi = Roi()
        roi.x = x
        roi.y = y
        roi.w = w
        return roi

    def parseAttrToInt(self):
        self.x = int(self.x)
        self.y = int(self.y)
        self.h = int(self.h)
        self.w = int(self.w)

    def localizationPoints(self):
        self.filter, filters = filtrarImagen(self.image)
        self.pointLeft, self.pointRight = obtenerPuntos(self.filter)

        return filters

    def localizationPointsCenter(self):
        #Asumimos que es en la parte central de la imagen hacia la izquierda y derecha extremos
        #Aparentemente el filtrado no funciona en los ojos
        self.pointLeft = [0,int(self.h/2)]
        self.pointRight = [self.w,int(self.h/2)]

    def centralPoint(self):
        return [int(self.w/2),int(self.h/2)]

    def getSquareBegin(self, half_patch):
        return (self.x - half_patch, self.y - half_patch)

    def getSquareEnd(self, half_patch):
        return (self.x + half_patch, self.y + half_patch)
