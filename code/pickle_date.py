import cPickle as pickle
import csv

def toCSV(output, fileName):
    csvFile = open(fileName, 'wb')
    writer = csv.writer(csvFile)
    writer.writerow(output)
    csvFile.close()

data = pickle.load(open('shares.pkl'))
keys = data.keys()

stack = data[keys[0]]
time = stack['time']
toCSV(time, 'time.csv')


