import tensorflow as tf
import cPickle as pickle
import numpy as np

INPUT_NODE = 303
OUTPUT_NODE = 2

LAYER1_NODE = 500
BATCH_SIZE = 1000

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

def recall(recallSet, current_output, input_count):
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

def inference(input_tensor, avg_class, weights1, biases1, weights2, biases2):
    if avg_class == None:
        layer1 = tf.nn.relu(tf.matmul(input_tensor, weights1) + biases1)
        return tf.matmul(layer1, weights2) + biases2
    else:
        layer1 = tf.nn.relu(tf.matmul(input_tensor, avg_class.average(weights1)) + avg_class.average(biases1))
        return tf.matmul(layer1, avg_class.average(weights2)) + biases2

def train(data_train_positive, data_train_negative, data_validate):
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
    cross_entropy = tf.nn.sparse_softmax_cross_entropy_with_logits(labels = tf.argmax(y_, 1), logits = tf.clip_by_value(y, 1e-10, 1.0))
    cross_entropy_mean = tf.reduce_mean(cross_entropy)

    #regularizer = tf.contrib.layers.l2_regularizer(REGULARIZATION_RATE)
    #regularization = regularizer(weights1) + regularizer(weights2)
    #loss = cross_entropy_mean + regularization
    loss = cross_entropy_mean
    #tf.Print(loss, [loss])

    learning_rate = tf.train.exponential_decay(LEARNING_RATE_BASE, global_step, (2 * len(data_train_positive)) / BATCH_SIZE, LEARNING_RATE_DECAY)

    train_step = tf.train.GradientDescentOptimizer(learning_rate).minimize(loss, global_step = global_step)
    with tf.control_dependencies([train_step, variables_averages_op]):
        train_op = tf.no_op(name = 'train')

    correct_predicition = tf.equal(tf.argmax(average_y, 1), tf.argmax(y_, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_predicition, tf.float32))

    total_count = tf.reduce_sum(y_, 0)
    #softmax average_y at first
    #softmax = tf.nn.softmax(average_y)
    #only valid for binary classification 
    #recalled = tf.reduce_sum(tf.sign(softmax - 0.5) + 1.0, 0) / 2.0
    #recall = tf.div(recalled, total_count)[0]

    #dataSet = np.array(pickle.load(open('/home/fengkai/part_500000.pkl')))
    print data_train_positive.shape
    train_positive_labels = data_train_positive[:, -1].reshape(data_train_positive.shape[0], 1)
    train_negative_labels = data_train_negative[:, -1].reshape(data_train_negative.shape[0], 1)
    print train_positive_labels.shape
    print train_negative_labels.shape
    input_train_positive = data_train_positive[:, : -1]
    input_train_negative = data_train_negative[:, : -1]
    print input_train_positive.shape
    print input_train_negative.shape

    validate_input = data_validate[:, : -1]
    validate_labels = data_validate[:, -1].reshape(data_validate.shape[0], 1)

    with tf.Session() as sess:
        tf.initialize_all_variables().run()

        #validate_feed = {x: mnist.validation.images, y_: mnist.validation.labels}
        validate_feed = {x: validate_input, y_: validate_labels}
	recall_feed = validate_feed
        #test_feed = {x: mnist.test.images, y_: mnist.test.labels}
        test_feed = validate_feed

        input_count = sess.run(total_count, feed_dict = {y_: validate_labels})

        positive_number = 0;
        negative_number = 0;

        for i in range(TRAINING_STEPS):
            if i % 1000 == 0:
                validation_acc = sess.run(accuracy, feed_dict = validate_feed)
                print "After %d trainiing step(s), validation accuracy using average model is %g" % (i, validation_acc)
                #validation_recall = sess.run(recalled, feed_dict = recall_feed)[0] / t_count[0]
		
                current_output = sess.run(average_y, feed_dict = validate_feed)
                #count = 0
                #for j in range(len(current_output)):
                #    if current_output[j][0] > current_output[j][1] and labels[j][0] > 0.5:
                #        count = count + 1
                #print "After %d trainiing step(s), validation recall using average model is %g" % (i, count / input_count[0])
                recall(validate_labels, current_output, input_count)
		print sess.run(loss, feed_dict = validate_feed)
		
            #xs, ys = mnist.train.next_batch(BATCH_SIZE)
            #start = (i * BATCH_SIZE) % len(dataSet)
            #end = min(start + BATCH_SIZE, len(dataSet))
            #xs = inputSet[start: end]
            #ys = labels[start: end]
            #print xs.shape
	    xs, ys, positive_number, negative_number = get_input_data(positive_number, negative_number, input_train_positive, input_train_negative, train_positive_labels, train_negative_labels)
            sess.run(train_op, feed_dict = {x: xs, y_: ys})
            #sess.run(tf.Print(loss, [loss]))
            #print sess.run(loss, feed_dict = {x: xs, y_: ys})

        test_acc = sess.run(accuracy, feed_dict = test_feed)
        print "After %d trainiing step(s), validation accuracy using average model is %g" % (TRAINING_STEPS, test_acc)
        #test_recall = sess.run(recall, feed_dict = recall_feed)
        #print "After %d trainiing step(s), validation recall using average model is %g" % (TRAINING_STEPS, test_recall)
        current_output = sess.run(average_y, feed_dict = validate_feed)
        recall(validate_labels, current_output, input_count)

def main(argv = None):
    #mnist = input_data.read_data_sets("", one_hot = True)
    print get_available_gpus()
    data_train_positive = np.array(pickle.load(open('/home/fengkai/500w_train_positive.pkl')))
    data_train_negative = np.array(pickle.load(open('/home/fengkai/500w_train_negative.pkl')))
    data_validate = np.array(pickle.load(open('/home/fengkai/500w_validate.pkl')))
    #labels = np.array(pickle.load(open('/home/fengkai/labels_500w.pkl')))
    train(data_train_positive, data_train_negative, data_validate)

if __name__ == '__main__':
    tf.app.run()
