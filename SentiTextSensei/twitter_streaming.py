# coding=utf-8
# python twitter_streamming.py > data/twitter_data.txt

import json
from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream

# Variables that contains the user credentials to access Twitter API
access_token = "79763955-q3UyMKEV5Xlqb90VLouwGuCVFvYcxrFwvdR40aO0O"
access_token_secret = "7n4HfD3yBhb7PHz6ytRB7uQqn8ICQpIXv2Kxx01kJwCBp"
consumer_key = "Ikzkt2CDZAmVFkxk44t4EUjgB"
consumer_secret = "VnrNgvXYJOuXWqoGT1jzErmipsboo1NHw06gfW1hDf8QfJaDWf"

file = open('data/tweets6.txt','a')

class StdOutListener(StreamListener):

    def on_data(self, data):
        try:
            decoded = json.loads(data)
            text = decoded['text'].replace('\n', ' ')
            tweet = '%s\n' % (text)
            file.write(tweet.encode('utf8'))
            print tweet
            return True
        except Exception as e:
            print e # no queremos que el script se pare


    def on_error(self, status):
        print status
        if status == 420:
            # returning False in on_data disconnects the stream
            return False

if __name__ == '__main__':

    print "Iniciando proceso mining-twitter..."
    # This handles Twitter authetification and the connection to Twitter Streaming API
    l = StdOutListener()
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)
    stream = Stream(auth, l)

    locs = [-107.43488, 24.7885, -107.367493, 24.82118]
    stream.filter(track=['java', 'python', 'javascript'], locations= locs, languages=["es"])

    # This line filter Twitter Streams to capture data by the keywords: 'python', 'java', 'c#','javascript'
    #stream.filter(track=['java','python','javascript','linux','informatico','informatica','programar','binario','geek','programadores','unix','android','hacker','code','windows'])
    #keywords = ['java','python','php','javascript','informatico','programador','programar']

    #stream.filter(track=['java','python','php','javascript','informatico','programador','programar'], languages=["es"])
    #stream.filter(track=['java','python','javascript'],locations=locs,languages=["es"])
    #stream.filter(track=['java','python','javascript'],languages=["es"])
