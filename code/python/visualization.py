from matplotlib.pyplot import *

def toList(string):
    return [float(x) for x in string.split(',')]

F1_negatives = open('F1_negatives.csv').read()
F1_positives = open('F1_positives.csv').read()

negatives = toList(F1_negatives)
positives = toList(F1_positives)

xlabel('TRAINING STEP(S)')
ylabel('F1 SCORE')
title('F1 SCORES')

steps = []

for i in range(101):
    steps.append(i * 1000)

plot(steps, negatives, c = 'red')
plot(steps, positives, c = 'blue')

savefig('F1.jpg')
show()


