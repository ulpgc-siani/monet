#!/bin/bash

date=`date +"%d/%m/%Y %H:%M"`
host=`uname -a`
relative_dir=target/space/WEB-INF
mkdir -p $relative_dir
git log | head -n 4 > $relative_dir/version.txt
echo "Generate WAR date: $date" >> $relative_dir/version.txt
echo "Generate host: $host" >> $relative_dir/version.txt

info=`git log | head -n 4`
info=`echo ${info/</}`
info=`echo ${info/>/}`
xml="<monet-info>$info</monet-info><monet-generatedate>$date</monet-generatedate><monet-generatehost>$host</monet-generatehost>"
echo $xml > $relative_dir/version.xml
