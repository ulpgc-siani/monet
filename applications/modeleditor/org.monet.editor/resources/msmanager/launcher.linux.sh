#!/bin/bash -f

cd "$1"

DIR="$1"
MACHINE_TYPE=`uname -m`
if [ "$MACHINE_TYPE" = "x86_64" ]; then
  DIR=$1/linux/64
else
  DIR=$1/linux/32
fi

chmod +x $DIR/msmanager
$DIR/msmanager
