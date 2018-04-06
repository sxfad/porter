#!/usr/bin/env sh

#canal 1.0.25 BUG
#https://github.com/alibaba/canal/issues/482

PRG="$0"
PRGDIR=`dirname "$PRG"`

#强制覆盖
\cp -rf "$PRGDIR/../bugs/canal.parse-1.0.25.jar"  "$PRGDIR/../lib/"