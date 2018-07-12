#!/usr/bin/env sh

#canal 1.0.25 BUG
#canal连接mysql时，因要读取头信息持续等待问题

PRG="$0"
PRGDIR=`dirname "$PRG"`

#强制覆盖
\cp -rf "$PRGDIR/../bugs/canal.parse.driver-1.0.25.jar"  "$PRGDIR/../lib/"