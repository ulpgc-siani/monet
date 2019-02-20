#!/bin/bash

if [ "$1" == "" ]
then
  echo "Usage sudo ./compress.sh <file vmdk to compress>"
  exit
fi

# Make sure only root can run our script
if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

TUUID_OLD='ddb.uuid.image = "8b1a5303-4644-4420-918f-e901a97ac061"'
UUID=`strings -a -t x "$1" |grep -m 1 ddb.uuid.image | cut -d"\"" -f2`
TUUID_NEW="ddb.uuid.image = \"$UUID\""

FILE_USER=`stat -c %U "$1"`
FILE_GROUP=`stat -c %G "$1"`

cp empty.vmdk temp.vmdk
mkdir diskA
mkdir diskB
vmware-mount -r "$1" diskA
vmware-mount temp.vmdk diskB
sudo cp -r --preserve diskA/* diskB/
vmware-mount -x
rmdir diskA
rmdir diskB

mv -f "$1" "$1.backup"
mv temp.vmdk "$1"
sudo chown $FILE_USER.$FILE_GROUP "$1"

sed -i "s/$TUUID_OLD/$TUUID_NEW/g" "$1"
