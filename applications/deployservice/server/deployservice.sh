#!/bin/bash

APP_NAME=deployservice
APP_ID=`ps -FC java |grep $APP_NAME| awk 'BEGIN{FS="[ :]+"}{print $2}' |awk 'FNR == 1'`
KILL_APP="kill -9 $APP_ID"

check=`echo $APP_ID`
if [ ! $check = "" ]; then
  echo Shutdown previous process...
  ${KILL_APP}
fi

java -Dfile.encoding=UTF8 -jar `dirname $0`/$APP_NAME.jar&

