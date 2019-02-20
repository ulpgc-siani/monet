#!/bin/sh

export JSMTPD_DIR=`dirname "$0"`;
JAVA_OPTS="-Xmx512m"

CDIR=`pwd`

cd $JSMTPD_DIR

if [ -z "$1" ] ; then
        echo "Usage: jsmtpd.sh option";
        echo "jsmtpd.sh debug : run attached with debug port (8000) enabled";
        echo "jsmtpd.sh run   : run attached";
        echo "jsmtpd.sh start : run jsmtpd as daemon process"
        echo "jsmtpd.sh stop  : stop previously started jsmtpd as daemon ";
        exit 0;
fi

CP=.:etc/

for i in lib/* ; do
    CP=${CP}:$i
done

for i in plugins/* ; do
    CP=${CP}:$i
done

for i in plugins/filters/Jasen/* ; do
    CP=${CP}:$i
done


CP=$CP:plugins/filters/ClamAV/jsmtpd-clamav.jar
CP=$CP:plugins/filters/SA/jsmtpd-SA.jar

CP=$CP:plugins/inputIPFilters/jsmtpd-inputIPFilters.jar


if [ "$1" = "debug" ] ; then
        JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"
		echo "Starting jsmtpd (debug)";
        exec java $JAVA_OPTS  -cp $CP org.jsmtpd.Launcher
        cd $CDIR
        exit 0;
		
fi


if [ "$1" = "run" ] ; then
        echo "Starting jsmtpd (attached)";
        exec java $JAVA_OPTS  -cp $CP org.jsmtpd.Launcher
        cd $CDIR
        exit 0;
fi


if [ "$1" = "start" ] ; then
        if [ -f $JSMTPD_DIR/jsmtpd.pid ] ; then
		PID=`cat $JSMTPD_DIR/jsmtpd.pid`;
		if [ `kill -0 $PID` ] ; then
			echo "Jsmtpd seems to be already running, use stop before start"
			cd $CDIR;
			exit 0;
		fi
		rm "$JSMTPD_DIR/jsmtpd.pid"
                echo "Jsmtpd was not shut down properly ...";
        fi
        echo "Starting jsmtpd (daemon)";
        exec java $JAVA_OPTS  -cp $CP org.jsmtpd.Launcher 2>&1 > /dev/null  &
        echo $! > $JSMTPD_DIR/jsmtpd.pid
        echo "JVM pid is $!";
        echo "Log file should be $JSMTPD_DIR/log/jsmtpd.log";
        cd $CDIR
        exit 0;
fi

if [ "$1" == "stop" ] ;  then
        if [ -f $JSMTPD_DIR/jsmtpd.pid ] ; then
                echo "Stopping Jsmtpd";
                PID=`cat $JSMTPD_DIR/jsmtpd.pid`;
                rm "$JSMTPD_DIR/jsmtpd.pid"
                kill $PID
                cd $CDIR
                exit 0;
        else
                echo "No pid file ? Is jsmtpd running ?";
                cd $CDIR
                exit 0;
        fi
fi
        
