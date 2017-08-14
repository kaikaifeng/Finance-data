#!/bin/bash

nohup java -jar controller.jar 5 20 100 >> controller.log &

usleep 1000000

nohup java -jar worker.jar 192.168.111.3 >> worker.log &
ssh hadoop@hadoop1 << eeooff
cd /home/hadoop/shapelet
nohup java -jar worker.jar 192.168.111.3 >> worker.log &
eeooff
ssh hadoop@hadoop2 << eeooff
cd /home/hadoop/shapelet
nohup java -jar worker.jar 192.168.111.3 >> worker.log &
eeooff
ssh hadoop@hadoop4 << eeooff
cd /home/hadoop/shapelet
nohup java -jar worker.jar 192.168.111.3 >> worker.log &
eeooff
ssh hadoop@hadoop5 << eeooff
cd /home/hadoop/shapelet
nohup java -jar worker.jar 192.168.111.3 >> worker.log &
eeooff
