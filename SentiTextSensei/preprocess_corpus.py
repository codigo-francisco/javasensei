# Limpieza y preprocesamiento del corpus TASS
# Corpus TASS: http://www.sepln.org/workshops/tass/2015/tass2015.php#corpus
# general-tweets-test-tagged.xml --> general-tweets-test-tagged.csv
# general-tweets-train-tagged.xml --> general-tweets-train-tagged.csv
# corpus.csv --> corpus_prog.csv + general-tweets-train-tagged.csv + general-tweets-test-tagged.csv

# coding=utf-8
import pandas as pd
from lxml import objectify

pd.set_option('max_colwidth',1000)

def get_polaridad(polaridad):
    texto = 'negativo'
    if polaridad=='P+' or polaridad=='P':
        texto = 'positivo'
    elif polaridad=='N+' or polaridad=='N':
        texto = 'negativo'
    return texto

def procesar(fuente,destino):
    try:
        corpus = pd.read_csv(destino, sep='|', encoding='utf-8',header=None)
    except:
        xml = objectify.parse(open(fuente))
        root = xml.getroot()
        corpus = pd.DataFrame(columns=('polarity', 'content'))
        tweets = root.getchildren()
        for i in range(0,len(tweets)):
            tweet = tweets[i]
            if tweet.sentiments.polarity.value.text != 'NEU' or tweet.sentiments.polarity.value.text != 'NONE'\
                    or len(tweet.sentiments.polarity.value.text)>1:
                tokens =(tweet.content.text).split(" ")
                if len(tokens)>3:
                    row = dict(zip(['polarity', 'content'],
                                   [get_polaridad(tweet.sentiments.polarity.value.text),tweet.content.text]))
                    row_s = pd.Series(row)
                    row_s.name = i
                    corpus = corpus.append(row_s)
        corpus.to_csv(destino, index=False, sep='|', encoding='utf-8',header=None)

def unir_corpus():
    archivos = ['data/corpus_prog.csv','data/general-tweets-train-tagged.csv', 'data/general-tweets-test-tagged.csv']
    with open('data/corpus.csv', 'w') as outfile:
        for fname in archivos:
            with open(fname) as infile:
                for line in infile:
                    outfile.write(line)

def main():
    print("procesando corpus 1...")
    procesar('data/general-tweets-train-tagged.xml','data/general-tweets-train-tagged.csv')
    print("procesando corpus 2...")
    procesar('data/general-tweets-test-tagged.xml', 'data/general-tweets-test-tagged.csv')
    print("generando corpus ok...")
    unir_corpus()
    print("fin proceso...")

if __name__ == '__main__':
    main()
