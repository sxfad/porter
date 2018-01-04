#!/usr/bin/env sh

signalId=`kill -l USR2`

node_pid=`jps -v |grep "NodeBootApplication"|awk '{printf  " "$1}'`

if [ -n "$node_pid" ]
then
    kill -${signalId} ${node_pid}
	echo "[${node_pid}]datas-node shutdown Successfully"
fi
