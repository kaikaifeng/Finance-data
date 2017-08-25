# -*- coding: utf-8 -*-

import tensorflow as tf
import cPickle as pickle
import numpy as np

INPUT_NODE = 294
OUTPUT_NODE = 2

LAYER1_NODE = 1000
LAYER2_NODE = 1000
LAYER3_NODE = 1000
LAYER4_NODE = 500
LAYER5_NODE = 500
BATCH_SIZE = 10000

LEARNING_RATE_BASE = 0.8
LEARNING_RATE_DECAY = 0.99
REGULARIZATION_RATE = 0.01
TRAINING_STEPS = 30000
MOVING_AVERAGE_DECAY = 0.99

def get_input_data(positive_number, negative_number, positive, negative, positive_labels, negative_labels):
    inputSet = []
    labels = []
    for i in range(BATCH_SIZE):
        if i % 2 != 0:
            inputSet.append(positive[positive_number % len(positive)])
            labels.append(positive_labels[positive_number % len(positive)])
            positive_number = positive_number + 1
        else:
            inputSet.append(negative[negative_number % len(negative)])
            labels.append(negative_labels[negative_number % len(negative)])
            negative_number = negative_number + 1
    return inputSet, labels, positive_number, negative_number 

def recall(recallSet, current_output, input_count, i):
    count = 0
    for j in range(len(current_output)):
        if current_output[j][0] > current_output[j][1] and recallSet[j][0] > 0.5:
            count = count + 1
    print "After %d trainiing step(s), validation recall using average model is %g" % (i, count / input_count[0])
    
def get_available_gpus():
    """
    code from http://stackoverflow.com/questions/38559755/how-to-get-current-available-gpus-in-tensorflow
    """
    from tensorflow.python.client import device_lib as _device_lib
    local_device_protos = _device_lib.list_local_devices()
    return [x.name for x in local_device_protos if x.device_type == 'GPU']

def inference(input_tensor,
              avg_class,
              weights1,
              biases1,
              weights2,
              biases2,
              weights3,
              biases3,
              weights4,
              biases4,
              weights5,
              biases5,
              weights6,
              biases6):
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

def train(data_train_positive, data_train_negative, data_validate, train_positive_labels, train_negative_labels, validate_labels):
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
                  weights1,
                  biases1,
                  weights2,
                  biases2,
                  weights3,
                  biases3,
                  weights4,
                  biases4,
                  weights5,
                  biases5,
                  weights6,
                  biases6)
    global_step = tf.Variable(0, trainable = False)

    variable_averages = tf.train.ExponentialMovingAverage(MOVING_AVERAGE_DECAY, global_step)
    variables_averages_op = variable_averages.apply(tf.trainable_variables())

    average_y = inference(x,
                          variable_averages,
                          weights1,
                          biases1,
                          weights2,
                          biases2,
                          weights3,
                          biases3,
                          weights4,
                          biases4,
                          weights5,
                          biases5,
                          weights6,
                          biases6)
    cross_entropy = tf.nn.sparse_softmax_cross_entropy_with_logits(labels = tf.argmax(y_, 1), logits = y)
    cross_entropy_mean = tf.reduce_mean(cross_entropy)

    #regularizer = tf.contrib.layers.l2_regularizer(REGULARIZATION_RATE)
    #regularization = regularizer(weights1) + regularizer(weights2)
    #loss = cross_entropy_mean + regularization
    loss = cross_entropy_mean

    learning_rate = tf.train.exponential_decay(LEARNING_RATE_BASE, global_step, (2 * len(data_train_positive)) / BATCH_SIZE, LEARNING_RATE_DECAY)

    train_step = tf.train.GradientDescentOptimizer(learning_rate).minimize(loss, global_step = global_step)
    with tf.control_dependencies([train_step, variables_averages_op]):
        train_op = tf.no_op(name = 'train')

    correct_predicition = tf.equal(tf.argmax(average_y, 1), tf.argmax(y_, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_predicition, tf.float32))

    total_count = tf.reduce_sum(y_, 0)

    print data_train_positive.shape
    print train_positive_labels.shape
    print train_negative_labels.shape
    input_train_positive = data_train_positive[:, : -1]
    input_train_negative = data_train_negative[:, : -1]
    print input_train_positive.shape
    print input_train_negative.shape

    validate_input = data_validate[:, : -1]
    print validate_input.shape
    print validate_labels.shape
    
    saver = tf.train.Saver(max_to_keep = None)

    with tf.Session() as sess:
        tf.initialize_all_variables().run()

        validate_feed = {x: validate_input, y_: validate_labels}
	recall_feed = validate_feed
        test_feed = validate_feed

        input_count = sess.run(total_count, feed_dict = {y_: validate_labels})

        positive_number = 0;
        negative_number = 0;

        for i in range(TRAINING_STEPS):
            if i % 1000 == 0:
                validation_acc = sess.run(accuracy, feed_dict = validate_feed)
                print "After %d trainiing step(s), validation accuracy using average model is %g" % (i, validation_acc)

		current_output = sess.run(average_y, feed_dict = validate_feed)
		recall(validate_labels, current_output, input_count, i)
		print sess.run(loss, feed_dict = validate_feed)
		saver_path = saver.save(sess, 'save/fully/8_25/model.ckpt', global_step = i)

	    xs, ys, positive_number, negative_number = get_input_data(positive_number, negative_number, input_train_positive, input_train_negative, train_positive_labels, train_negative_labels)
	    sess.run(train_op, feed_dict = {x: xs, y_: ys})
	    if i % 100 == 0:
                print sess.run(loss, feed_dict = {x: xs, y_: ys})

        test_acc = sess.run(accuracy, feed_dict = test_feed)
        print "After %d trainiing step(s), validation accuracy using average model is %g" % (TRAINING_STEPS, test_acc)
        current_output = sess.run(average_y, feed_dict = validate_feed)
        recall(validate_labels, current_output, input_count, TRAINING_STEPS)
        saver_path = saver.save(sess, 'save/fully/8_25/model.ckpt', global_step = TRAINING_STEPS)

def main(argv = None):
    #mnist = input_data.read_data_sets("", one_hot = True)
    print get_available_gpus()
    data_train_positive = np.array(pickle.load(open('/home/fengkai/500w_train_positive.pkl')))
    data_train_negative = np.array(pickle.load(open('/home/fengkai/500w_train_negative.pkl')))
    data_validate = np.array(pickle.load(open('/home/fengkai/500w_validate.pkl')))
    train_positive_labels = np.array(pickle.load(open('/home/fengkai/500w_train_positive_label.pkl')))
    train_negative_labels = np.array(pickle.load(open('/home/fengkai/500w_train_negative_label.pkl')))
    validate_label = np.array(pickle.load(open('/home/fengkai/500w_validate_label.pkl')))
    train(data_train_positive, data_train_negative, data_validate, train_positive_labels, train_negative_labels, validate_label)

if __name__ == '__main__':
    tf.app.run()
