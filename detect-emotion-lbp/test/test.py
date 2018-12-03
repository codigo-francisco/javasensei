from multiprocessing import cpu_count
from multiprocessing import Pool
import cPickle
from sklearn.svm import LinearSVC
from sklearn.metrics import confusion_matrix
import numpy as np
from sklearn.utils import shuffle
import warnings

warnings.simplefilter("ignore")

def training(data):
    pd = data[0]
    pl = data[1]

    pd, pl = shuffle(pd,pl)

    svc = LinearSVC()
    svc.fit(pd,pl)

    score = svc.score(pd,pl)
    cm = confusion_matrix(pl,map(svc.predict,pd))

    return [score,cm]

if __name__ == "__main__":
    pd = cPickle.load(file("../data/td.x","rb"))
    pl = cPickle.load(file("../data/tl.y","rb"))
    methods = []

    for x in range(10):
        methods.append([pd,pl])

    amount_processors = cpu_count()
    p = Pool(amount_processors)
    results = p.map(training,methods)
    values = np.asarray(map(lambda p: p[0],results))
    mean = values.mean()
    sd = values.std()

    print("Amount of Processors: "+str(amount_processors))
    print(values)
    print(mean,sd)