import cv2, glob, random, math, numpy as np, dlib, itertools
import cPickle
from sklearn.svm import SVC

__author__ = "Paul van Gent, 2016"  # Please leave this line in

emotions = ["Boredom","Engagement", "Excitement", "Frustration"]  # Emotion list
clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8, 8))
detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor("data/shape_predictor_68_face_landmarks.dat")  # Or set this to whatever you named the downloaded file
clf = SVC(kernel='linear', probability=True,
          tol=1e-3)  # , verbose = True) #Set the classifier as a support vector machines with polynomial kernel


def get_files(emotion):  # Define function to get file list, randomly shuffle it and split 80/20
    files = glob.glob("D:\Subcorpus\%s\*.png"%emotion)
    random.shuffle(files)
    #top files
    #files = files[:200]
    training = files[:int(len(files) * 0.9)]  # get first 80% of file list
    prediction = files[-int(len(files) * 0.1):]  # get last 20% of file list
    return training, prediction


def get_landmarks(image):
    image = cv2.resize(image, (500, 500))
    detections = detector(image, 1)
    for k, d in enumerate(detections):  # For all detected face instances individually
        shape = predictor(image, d)  # Draw Facial Landmarks with the predictor class
        xlist = []
        ylist = []
        for i in range(1, 68):  # Store X and Y coordinates in two lists
            xlist.append(float(shape.part(i).x))
            ylist.append(float(shape.part(i).y))

        xmean = np.mean(xlist)  # Get the mean of both axes to determine centre of gravity
        ymean = np.mean(ylist)
        xcentral = [(x - xmean) for x in xlist]  # get distance between each point and the central point in both axes
        ycentral = [(y - ymean) for y in ylist]

        if xlist[26] == xlist[29]:  # If x-coordinates of the set are the same, the angle is 0, catch to prevent 'divide by 0' error in function
            anglenose = 0
        else:
            anglenose = int(math.atan((ylist[26] - ylist[29]) / (xlist[26] - xlist[29])) * 180 / math.pi)

        if anglenose < 0:
            anglenose += 90
        else:
            anglenose -= 90

        landmarks_vectorised = []
        for x, y, w, z in zip(xcentral, ycentral, xlist, ylist):
            landmarks_vectorised.append(x)
            landmarks_vectorised.append(y)
            meannp = np.asarray((ymean, xmean))
            coornp = np.asarray((z, w))
            dist = np.linalg.norm(coornp - meannp)
            anglerelative = (math.atan((z - ymean) / (w - xmean)) * 180 / math.pi) - anglenose
            landmarks_vectorised.append(dist)
            landmarks_vectorised.append(anglerelative)

    if len(detections) < 1:
        landmarks_vectorised = "error"
    return landmarks_vectorised


def make_sets():
    training_data = []
    training_labels = []
    prediction_data = []
    prediction_labels = []
    for emotion in emotions:
        training, prediction = get_files(emotion)
        # Append data to training and prediction list, and generate labels 0-7
        for item in training:
            image = cv2.imread(item)  # open image
            gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)  # convert to grayscale
            clahe_image = clahe.apply(gray)
            landmarks_vectorised = get_landmarks(clahe_image)
            if landmarks_vectorised == "error":
                pass
            else:
                training_data.append(landmarks_vectorised)  # append image array to training data list
                training_labels.append(emotions.index(emotion))

        for item in prediction:
            image = cv2.imread(item)
            gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
            clahe_image = clahe.apply(gray)
            landmarks_vectorised = get_landmarks(clahe_image)
            if landmarks_vectorised == "error":
                pass
            else:
                prediction_data.append(landmarks_vectorised)
                prediction_labels.append(emotions.index(emotion))

    return training_data, training_labels, prediction_data, prediction_labels

training_data, training_labels, prediction_data, prediction_labels = make_sets()

#Guardado
#cPickle.dump(training_data, open("data/td.x","wb"))
#cPickle.dump(training_labels, open("data/tl.y","wb"))
#cPickle.dump(prediction_data, open("data/pd.x","wb"))
#cPickle.dump(prediction_labels, open("data/pl.y","wb"))