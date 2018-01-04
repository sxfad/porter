#!/usr/bin/env sh

# zhangkewei
# 后台启动脚本

PRG="$0"
PRGDIR=`dirname "$PRG"`
EXECUTABLE=node-boot

if [ "$1" = "debug" ]; then
    if [ -z "$2" ]; then
        DEBUG_PORT=5555
    else
        DEBUG_PORT=$2
    fi
    export JAVA_OPTS=" $JAVA_OPTS -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=n"
fi

nohup "$PRGDIR/$EXECUTABLE" > /dev/null 2>&1 &