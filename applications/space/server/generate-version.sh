#!/bin/bash

date=`date +"%d/%m/%Y %H:%M"`
host=`uname -a`

git log | head -n 4 > version.txt
echo "Generate WAR date: $date" >> version.txt
echo "Generate host: $host" >> version.txt

info=`git log | head -n 4`
info=`echo ${info/</}`
info=`echo ${info/>/}`
xml="<monet-info>$info</monet-info><monet-generatedate>$date</monet-generatedate><monet-generatehost>$host</monet-generatehost>"
echo $xml > version.xml

