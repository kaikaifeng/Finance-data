# -*- coding: utf-8 -*-

import tensorflow as tf
import cPickle as pickle
import numpy as np

INPUT_NODE = 294
OUTPUT_NODE = 2

LAYER1_NODE = 1000
LAYER2_NODE = 1000
LAYER3_NODE = 1000
LAYER4_NODE = 1000
LAYER5_NODE = 1000
BATCH_SIZE = 10000

LEARNING_RATE_BASE = 0.8
LEARNING_RATE_DECAY = 0.997
REGULARIZATION_RATE = 0.01
TRAINING_STEPS = 100000
MOVING_AVERAGE_DECAY = 0.997

def recall(recallSet, current_output, input_count, i, boolean):
    count = 0
    if boolean:
	for j in range(len(current_output)):
            if current_output[j][0] > current_output[j][1] and recallSet[j][0] > 0.5:
                count = count + 1
	print "After %d trainiing step(s), validation recall negative using average model is %g" % (i, count / input_count[0])
	return count / input_count[0]
    else:
	for j in range(len(current_output)):
	    if current_output[j][1] > current_output[j][0] and recallSet[j][1] > 0.5:
		count = count + 1
	print "After %d trainiing step(s), validation recall positive using average model is %g" % (i, count / input_count[1])
	return count / input_count[1]

def precision(precisionSet, current_output, i, boolean):
    count = 0
    if boolean:
	for j in range(len(current_output)):
	    if current_output[j][0] > current_output[j][1] and precisionSet[j][0] > 0.5:
		count = count + 1
	sum = 0.0
	for j in range(len(current_output)):
	    if current_output[j][0] > current_output[j][1]:
		sum = sum + 1
	if sum != 0:
		print "After %d training step(s), validation precision negative using average model is %g" % (i, count / sum)
        	return count / sum
	else:
		print "After %d training step(s), validation precision negative using average model is %g" % (i, 0.0)
		return 0.0
    else:
	for j in range(len(current_output)):
            if current_output[j][1] > current_output[j][0] and precisionSet[j][1] > 0.5:
                count = count + 1
        sum = 0.0
        for j in range(len(current_output)):
            if current_output[j][1] > current_output[j][0]:
                sum = sum + 1
	if sum != 0:
        	print "After %d training step(s), validation precision positive using average model is %g" % (i, count / sum)
		return count / sum
	else:
		print "After %d training step(s), validation precision positive using average model is %g" % (i, 0.0)
                return 0.0

def F1(precision, recall):
    return 2 * (precision * recall) / (precision + recall)

def inference(input_tensor,
              avg_class,
              weights1, biases1, 
              weights2, biases2,
              weights3, biases3,
              weights4, biases4,
              weights5, biases5,
              weights6, biases6):
    if avg_class == None:
        layer1 = tf.nn.sigmoid(tf.matmul(input_tensor, weights1) + biases1)
        layer2 = tf.nn.sigmoid(tf.matmul(layer1, weights2) + biases2)
        layer3 = tf.nn.sigmoid(tf.matmul(layer2, weights3) + biases3)
        layer4 = tf.nn.sigmoid(tf.matmul(layer3, weights4) + biases4)
        layer5 = tf.nn.sigmoid(tf.matmul(layer4, weights5) + biases5)
        return tf.matmul(layer5, weights6) + biases6
    else:
        layer1 = tf.nn.sigmoid(tf.matmul(input_tensor, avg_class.average(weights1)) + avg_class.average(biases1))
        layer2 = tf.nn.sigmoid(tf.matmul(layer1, avg_class.average(weights2)) + avg_class.average(biases2))
        layer3 = tf.nn.sigmoid(tf.matmul(layer2, avg_class.average(weights3)) + avg_class.average(biases3))
        layer4 = tf.nn.sigmoid(tf.matmul(layer3, avg_class.average(weights4)) + avg_class.average(biases4))
        layer5 = tf.nn.sigmoid(tf.matmul(layer4, avg_class.average(weights5)) + avg_class.average(biases5))
        return tf.matmul(layer5, avg_class.average(weights6)) + biases6

def train(data_validate, validate_labels):
    x = tf.placeholder(tf.float32, [None, INPUT_NODE], name = 'x-input')
    y_ = tf.placeholder(tf.float32, [None, OUTPUT_NODE], name = 'y-input')

    weights1 = tf.Variable(tf.truncated_normal([INPUT_NODE, LAYER1_NODE], stddev = 0.1))
    biases1 = tf.Variable(tf.constant(0.1, shape = [LAYER1_NODE]))
    weights2 = tf.Variable(tf.truncated_normal([LAYER1_NODE, LAYER2_NODE], stddev = 0.1))
    biases2 = tf.Variable(tf.constant(0.1, shape = [LAYER2_NODE]))
    weights3 = tf.Variable(tf.truncated_normal([LAYER2_NODE, LAYER3_NODE], stddev = 0.1))
    biases3 = tf.Variable(tf.constant(0.1, shape = [LAYER3_NODE]))
    weights4 = tf.Variable(tf.truncated_normal([LAYER3_NODE, LAYER4_NODE], stddev = 0.1))
    biases4 = tf.Variable(tf.constant(0.1, shape = [LAYER4_NODE]))
    weights5 = tf.Variable(tf.truncated_normal([LAYER4_NODE, LAYER5_NODE], stddev = 0.1))
    biases5 = tf.Variable(tf.constant(0.1, shape = [LAYER5_NODE]))
    weights6 = tf.Variable(tf.truncated_normal([LAYER5_NODE, OUTPUT_NODE], stddev = 0.1))
    biases6 = tf.Variable(tf.constant(0.1, shape = [OUTPUT_NODE]))

    y = inference(x,
                  None,
                  weights1, biases1,
                  weights2, biases2,
                  weights3, biases3,
                  weights4, biases4,
                  weights5, biases5,
                  weights6, biases6)
    global_step = tf.Variable(0, trainable = False)

    variable_averages = tf.train.ExponentialMovingAverage(MOVING_AVERAGE_DECAY, global_step)
    variables_averages_op = variable_averages.apply(tf.trainable_variables())

    average_y = inference(x,
                          variable_averages,
                          weights1, biases1,
                          weights2, biases2,
                          weights3, biases3,
                          weights4, biases4,
                          weights5, biases5,
                          weights6, biases6)
    cross_entropy = tf.nn.sparse_softmax_cross_entropy_with_logits(labels = tf.argmax(y_, 1), logits = y)
    cross_entropy_mean = tf.reduce_mean(cross_entropy)

    #regularizer = tf.contrib.layers.l2_regularizer(REGULARIZATION_RATE)
    #regularization = regularizer(weights1) + regularizer(weights2)
    #loss = cross_entropy_mean + regularization
    loss = cross_entropy_mean

    correct_predicition = tf.equal(tf.argmax(average_y, 1), tf.argmax(y_, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_predicition, tf.float32))

    total_count = tf.reduce_sum(y_, 0)

    validate_input = data_validate[:, : -1]
    
    saver = tf.train.Saver(max_to_keep = None)

    config = tf.ConfigProto(
        device_count = {'GPU': 0}
    )

    with tf.Session(config = config) as sess:
        tf.initialize_all_variables().run()

        validate_feed = {x: validate_input, y_: validate_labels}
	recall_feed = validate_feed
        test_feed = validate_feed
	
	input_count = sess.run(total_count, feed_dict = {y_: validate_labels})
	
	accuracies = []
	recall_positives = []
	recall_negatives = []
	precision_positives = []
	precision_negatives = []
	F1_positives = []
	F1_negatives = []
	
	for i in range(TRAINING_STEPS / 1000):
	    saver.restore(sess, 'save/fully/8_25/model.ckpt-' + str(i * 1000))
            validation_acc = sess.run(accuracy, feed_dict = validate_feed)
            print "After %d trainiing step(s), validation accuracy using average model is %g" % (i * 1000, validation_acc)
	    accuracies.append(validation_acc)

	    current_output = sess.run(average_y, feed_dict = validate_feed)
	    recall_positives.append(recall(validate_labels, current_output, input_count, i * 1000, True))
	    recall_negatives.append(recall(validate_labels, current_output, input_count, i * 1000, False))
	    precision_negatives.append(precision(validate_labels, current_output, i * 1000, True))
	    precision_positives.append(precision(validate_labels, current_output, i * 1000, False))
	    F1_negatives.append(F1(precision_negatives[i], recall_negatives[i]))
	    F1_positives.append(F1(precision_positives[i], recall_negatives[i]))

	saver.restore(sess, 'save/fully/8_25/model.ckpt-' + str(TRAINING_STEPS))
        test_acc = sess.run(accuracy, feed_dict = test_feed)
        print "After %d trainiing step(s), validation accuracy using average model is %g" % (TRAINING_STEPS, test_acc)
	accuracies.append(test_acc)
        current_output = sess.run(average_y, feed_dict = validate_feed)
        recall_positives.append(recall(validate_labels, current_output, input_count, TRAINING_STEPS, True))
        recall_negatives.append(recall(validate_labels, current_output, input_count, TRAINING_STEPS, False))
        precision_negatives.append(precision(validate_labels, current_output, TRAINING_STEPS, True))
        precision_positives.append(precision(validate_labels, current_output, TRAINING_STEPS, False))
        F1_negatives.append(F1(precision_negatives[TRAINING_STEPS / 1000], recall_negatives[TRAINING_STEPS / 1000]))
        F1_positives.append(F1(precision_positives[TRAINING_STEPS / 1000], recall_negatives[TRAINING_STEPS / 1000]))
	
	pickle.dump(accuracies, open('accuracies.pkl','wb'), -1)
	pickle.dump(recall_positives, open('recall_positives.pkl', 'wb'), -1)
	pickle.dump(recall_negatives, open('recall_negatives.pkl', 'wb'), -1)
	pickle.dump(precision_positives, open('precision_positives.pkl', 'wb'), -1)
	pickle.dump(precision_negatives, open('precision_negatives.pkl', 'wb'), -1)
	pickle.dump(F1_positives, open('F1_positives.pkl'), -1)
	pickle.dump(F1_negatives, open('F1_negatives.pkl'), -1)

def main(argv = None):
    data_validate = np.array(pickle.load(open('/home/fengkai/500w_validate.pkl')))
    validate_label = np.array(pickle.load(open('/home/fengkai/500w_validate_label.pkl')))
    train(data_validate, validate_label)

if __name__ == '__main__':
    tf.app.run()


