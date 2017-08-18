import tensorflow as tf
#from tensorflow.examples.tutorials.mnist import input_data
import cPickle as pickle
import numpy as np

INPUT_NODE = 303
OUTPUT_NODE = 2

LAYER1_NODE = 500
BATCH_SIZE = 100

LEARNING_RATE_BASE = 0.8
LEARNING_RATE_DECAY = 0.99
REGULARIZATION_RATE = 0.0001
TRAINING_STEPS = 30000
MOVING_AVERAGE_DECAY = 0.99

def get_available_gpus():
    """
    code from http://stackoverflow.com/questions/38559755/how-to-get-current-available-gpus-in-tensorflow
    """
    from tensorflow.python.client import device_lib as _device_lib
    local_device_protos = _device_lib.list_local_devices()
    return [x.name for x in local_device_protos if x.device_type == 'GPU']

def inference(input_tensor, avg_class, weights1, biases1, weights2, biases2):
    if avg_class == None:
        layer1 = tf.nn.relu(tf.matmul(input_tensor, weights1) + biases1)
        return tf.matmul(layer1, weights2) + biases2
    else:
        layer1 = tf.nn.relu(tf.matmul(input_tensor, avg_class.average(weights1)) + avg_class.average(biases1))
        return tf.matmul(layer1, avg_class.average(weights2)) + biases2

def train(dataSet, labels):
    x = tf.placeholder(tf.float32, [None, INPUT_NODE], name = 'x-input')
    y_ = tf.placeholder(tf.float32, [None, OUTPUT_NODE], name = 'y-input')

    weights1 = tf.Variable(tf.truncated_normal([INPUT_NODE, LAYER1_NODE], stddev = 0.1))
    biases1 = tf.Variable(tf.constant(0.1, shape = [LAYER1_NODE]))
    weights2 = tf.Variable(tf.truncated_normal([LAYER1_NODE, OUTPUT_NODE], stddev = 0.1))
    biases2 = tf.Variable(tf.constant(0.1, shape = [OUTPUT_NODE]))

    y = inference(x, None, weights1, biases1, weights2, biases2)
    global_step = tf.Variable(0, trainable = False)

    variable_averages = tf.train.ExponentialMovingAverage(MOVING_AVERAGE_DECAY, global_step)
    variables_averages_op = variable_averages.apply(tf.trainable_variables())

    average_y = inference(x, variable_averages, weights1, biases1, weights2, biases2)
    cross_entropy = tf.nn.sparse_softmax_cross_entropy_with_logits(labels = tf.argmax(y_, 1), logits = y)
    cross_entropy_mean = tf.reduce_mean(cross_entropy)

    regularizer = tf.contrib.layers.l2_regularizer(REGULARIZATION_RATE)
    regularization = regularizer(weights1) + regularizer(weights2)
    loss = cross_entropy_mean + regularization

    learning_rate = tf.train.exponential_decay(LEARNING_RATE_BASE, global_step, len(dataSet) / BATCH_SIZE, LEARNING_RATE_DECAY)

    train_step = tf.train.GradientDescentOptimizer(learning_rate).minimize(loss, global_step = global_step)
    with tf.control_dependencies([train_step, variables_averages_op]):
        train_op = tf.no_op(name = 'train')

    correct_predicition = tf.equal(tf.argmax(average_y, 1), tf.argmax(y_, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_predicition, tf.float32))

    total_count = tf.reduce_sum(y_, 1)
    recalled = tf.reduce_sum(tf.sign(average_y - 0.5), 1)
    recall = tf.div(recalled, total_count)[1]

    #dataSet = np.array(pickle.load(open('/home/fengkai/part_500000.pkl')))
    print dataSet.shape
    #labels = dataSet[:, -1].reshape(dataSet.shape[0], 1)
    print labels.shape
    inputSet = dataSet[:, : -1]
    print inputSet.shape

    with tf.Session() as sess:
        tf.initialize_all_variables().run()

        #validate_feed = {x: mnist.validation.images, y_: mnist.validation.labels}
        validate_feed = {x: inputSet, y_: labels}
	recall_feed = validate_feed
        #test_feed = {x: mnist.test.images, y_: mnist.test.labels}
        test_feed = validate_feed

        for i in range(TRAINING_STEPS):
            if i % 1000 == 0:
                validation_acc = sess.run(accuracy, feed_dict = validate_feed)
                print "After %d trainiing step(s), validation accuracy using average model is %g" % (i, validation_acc)
		validation_recall = sess.run(recall, feed_dict = recall_feed)
		print "After %d trainiing step(s), validation recall using average model is %g" % (i, validation_recall)
            #xs, ys = mnist.train.next_batch(BATCH_SIZE)
            start = (i * BATCH_SIZE) % len(dataSet)
            end = min(start + BATCH_SIZE, len(dataSet))
            xs = inputSet[start: end]
            ys = labels[start: end]
            #print xs.shape 
            sess.run(train_op, feed_dict = {x: xs, y_: ys})

        test_acc = sess.run(accuracy, feed_dict = test_feed)
        print "After %d trainiing step(s), validation accuracy using average model is %g" % (TRAINING_STEPS, test_acc)
	test_recall = sess.run(recall, feed_dict = recall_feed)
	print "After %d trainiing step(s), validation recall using average model is %g" % (TRAINING_STEPS, test_recall)

def main(argv = None):
    #mnist = input_data.read_data_sets("", one_hot = True)
    print get_available_gpus()
    dataSet = np.array(pickle.load(open('/home/fengkai/allFloat_500w.pkl')))
    labels = np.array(pickle.load(open('/home/fengkai/labels_500w.pkl')))
    train(dataSet, labels)

if __name__ == '__main__':
    tf.app.run()
