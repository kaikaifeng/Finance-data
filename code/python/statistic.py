# -*- coding: utf-8 -*-

import cPickle as pickle
import csv


def F1(precision, recall):
    return 2 * (precision * recall) / (precision + recall)

def toCSV(output, fileName):
    csvFile = open(fileName, 'wb')
    writer = csv.writer(csvFile)
    writer.writerow(output)
    csvFile.close()

recall_negatives = pickle.load(open('recall_negatives.pkl'))
recall_positives = pickle.load(open('recall_positives.pkl'))
precision_negatives = pickle.load(open('precision_negatives.pkl'))
precision_positives = pickle.load(open('precision_positives.pkl'))

F1_negatives = []
F1_positives = []

for i in range(len(recall_negatives)):
    F1_negatives.append(F1(recall_negatives[i], precision_negatives[i]))
    F1_positives.append(F1(recall_positives[i], precision_positives[i]))

toCSV(F1_negatives, 'F1_negatives.csv')
toCSV(F1_positives, 'F1_positives.csv')


