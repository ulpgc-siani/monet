#!/bin/bash

APP_NAME=deployservice
APP_ID=`ps -FC java |grep $APP_NAME| awk 'BEGIN{FS="[ :]+"}{print $2}' |awk 'FNR == 1'`
KILL_APP="kill -9 $APP_ID"

check=`echo $APP_ID`
if [ ! $check = "" ]; then
  echo Shutdown previous process...
  ${KILL_APP}
fi

if [ "$1" = "debug" ]; then
  java -Dfile.encoding=UTF8 -agentlib:jdwp=transport=dt_socket,address=9090,server=y,suspend=n -jar `dirname $0`/$APP_NAME.jar&
else
  java -Dfile.encoding=UTF8 -jar `dirname $0`/$APP_NAME.jar&
fi
