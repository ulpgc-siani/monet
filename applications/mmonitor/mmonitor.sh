#!/bin/bash

FILE_CURRENT=`readlink -e $0`
DIR_CURRENT=`dirname $FILE_CURRENT`
export DISKS=/
export MIN_DISK_SIZE_GB=20
export SLACK_TOKEN=
export SLACK_CHANNEL=
export TEAMS_URL=
export PROJECT=test
export SERVER_TYPE=
export SERVER_DOMAIN=

cd $DIR_CURRENT
java -jar $DIR_CURRENT/mmonitor.jar
