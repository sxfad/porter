@echo off

setlocal enabledelayedexpansion
set JAVA_PROCESS_TITLE=PorterBoot
set APP_HOME=%~dp0%..
set JAR_PATH=%APP_HOME%\lib

set JVM_OPTS= -Xms256M -Xmx256M -Dapp.home=%APP_HOME%

set CMD_LINE_ARGS=%*

if defined JAVA_HOME (
 set _EXECJAVA="%JAVA_HOME%\bin\java"
)

if not defined JAVA_HOME (
 echo "JAVA_HOME not set."
 set _EXECJAVA=java
)

set CLASSPATH=.
start "%JAVA_PROCESS_TITLE%" %_EXECJAVA%  %JVM_OPTS%  -jar %JAR_PATH%\{{RUN_JAR}} {{MAIN_CLASS}} %CMD_LINE_ARGS%  --spring.config.location=%APP_HOME%\config\
endlocal