#!/usr/bin/env sh

# zhangkewei
# 后台启动脚本

PRG="$0"
BIN_HOME=`dirname "$PRG"`
[ -z "$APP_HOME" ] && APP_HOME=`cd "$BIN_HOME/.." >/dev/null; pwd`
JAR_PATH=$APP_HOME/lib
_EXECJAVA=${JAVA_HOME}/bin/java
[ -z "$JAVA_HOME" ] && _EXECJAVA=java
JVM_OPTS=" -Xms256M -Xmx256M -Dapp.home=$APP_HOME/"

if [ "$1" = "debug" ]; then
    if [ -z "$2" ]; then
        DEBUG_PORT=5555
    else
        DEBUG_PORT=$2
    fi
    export JAVA_OPTS=" $JAVA_OPTS -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=n"
fi



save_spring () {
    index=0
    declare -a temp_array
    for i do
        if [[ "$i" =~ ^--[a-zA-Z].* ]];then
           temp_array[index]=${i}
           index=$((index+1));
        fi
    done
    echo ${temp_array[@]}
}

SPRING_ARGS=$(save_spring "$@")

nohup $_EXECJAVA $JVM_OPTS -jar ${JAR_PATH}/{{RUN_JAR}} {{MAIN_CLASS}} $SPRING_ARGS  --spring.config.location=${APP_HOME}/config/ > /dev/null 2>&1 &