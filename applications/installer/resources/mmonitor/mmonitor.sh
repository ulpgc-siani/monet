#!/bin/bash

FILE_CURRENT=`readlink -e $0`
DIR_CURRENT=`dirname $FILE_CURRENT`
export DISKS=#disks#
export MAX_DISK_SIZE_GB=#max_size_gb#
export SLACK_TOKEN=#slack_token#
export SLACK_CHANNEL=#slack_channel#
export PROJECT=#project_name#
export SERVER_TYPE=#server_type#
export SERVER_DOMAIN=#server_domain#

cd $DIR_CURRENT
java -jar $DIR_CURRENT/mmonitor.jar
