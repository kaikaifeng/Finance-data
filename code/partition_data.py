import cPickle as pickle
import random

dataSet = pickle.load(open('allFloat_500w.pkl'))
train = []
validate = []

for x in dateSet:
    if random.random() > 0.7:
        validate.append(x)
    else:
        train.append(x)

train_positive = []
train_negative = []

for x in train:
    if x[-1] < 0.5:
        train_positive.append(x)
    else:
        train_negative.append(x)

pickle.dump(train_positive, open('500w_train_positive.pkl', 'wb'), -1)
pickle.dump(train_negative, open('500w_train_negative.pkl', 'wb'), -1)
pickle.dump(validate, open('500w_validate.pkl', 'wb'), -1)
