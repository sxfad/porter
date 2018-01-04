#!/usr/bin/env sh

PRG="$0"
PRGDIR=`dirname "$PRG"`
EXECUTABLE=node-boot

JAVA_OPTS=" -Xms3G -Xmx3G "

exec "$PRGDIR/$EXECUTABLE" > /dev/null &