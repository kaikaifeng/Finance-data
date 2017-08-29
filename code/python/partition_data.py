# -*- coding: utf-8 -*-

import cPickle as pickle
import random

dataSet = pickle.load(open('allFloat_500w.pkl'))
train = []
validate = []

for x in dataSet:
    if random.random() > 0.7:
        validate.append(x)
    else:
        train.append(x)

train_positive = []
train_positive_label = []
train_negative = []
train_negative_label = []
validate_label = []

for x in train:
    if x[-1] < 0.5:
        train_positive.append(x)
	train_positive_label.append([0.0, 1.0])
    else:
        train_negative.append(x)
	train_negative_label.append([1.0, 0.0])

for x in validate:
    if x[-1] < 0.5:
	validate_label.append([0.0, 1.0])
    else:
	validate_label.append([1.0, 0.0])

pickle.dump(train_positive, open('500w_train_positive.pkl', 'wb'), -1)
pickle.dump(train_negative, open('500w_train_negative.pkl', 'wb'), -1)
pickle.dump(validate, open('500w_validate.pkl', 'wb'), -1)
pickle.dump(train_positive_label, open('500w_train_positive_label.pkl', 'wb'), -1)
pickle.dump(train_negative_label, open('500w_train_negative_label.pkl', 'wb'), -1)
pickle.dump(validate_label, open('500w_validate_label.pkl', 'wb'), -1)
