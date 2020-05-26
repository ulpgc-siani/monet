#-------------------------------------------------------------------------------
function upgrade_tomcat {
  tomcat_current_version=`cat /opt/tomcat-public/RELEASE-NOTES |grep "Apache Tomcat Version" | sed 's/^ *//;s/ *$//' |sed -E 's/^Apache Tomcat Version (.*)$/\1/'`
  tomcat_last_version=`curl -s 'https://tomcat.apache.org/download-80.cgi' |grep -Po '<h3 id="8\.5.*>(.*)</h3>'|sed -E 's/^<h3 id="8\.5.*>(.*)<\/h3>$/\1/'`

  show_info_text "Upgrade tomcat-public (from version $tomcat_current_version to $tomcat_last_version)..."

  if [ "$tomcat_current_version" != "$tomcat_last_version" ]; then
    url_tomcat_base="https://archive.apache.org/dist/tomcat/tomcat-8/v$tomcat_last_version/bin"
    tomcat_app="apache-tomcat-$tomcat_last_version.tar.gz"
    tomcat_app_file="$DIR_APPS/$tomcat_app"

    if [ ! -f "$tomcat_app_file" ]; then
      show_status " Downloading new version..."
      wget --no-check-certificate "$url_tomcat_base/$tomcat_app" -O "$tomcat_app_file" &>> $FILELOG || rm "$tomcat_app_file"
      result=$?
      if [ "$result" != "0" ]; then
        echo "  Error: failed to download Apache Tomcat"
        exit
      fi
      show_status_ok
    fi

    show_status " Stop services... "
    TOMCATPUBLIC_ID=`ps -FC java |grep tomcat-public| awk 'BEGIN{FS="[ :]+"}{print $2}' |awk 'FNR == 1'`
    if [ "$TOMCATPUBLIC_ID" != "" ]; then
      kill -9 $TOMCATPUBLIC_ID &>> $FILELOG
      if [ "$OS_VERSION" = "6" ]; then
        /etc/init.d/tomcat-public kill &> /dev/null
      else
        systemctl stop tomcat-public
      fi
    fi
    TOMCATLOCAL_ID=`ps -FC java |grep tomcat-local| awk 'BEGIN{FS="[ :]+"}{print $2}' |awk 'FNR == 1'`
    if [ "$TOMCATLOCAL_ID" != "" ]; then
      kill -9 $TOMCATLOCAL_ID &>> $FILELOG
      if [ "$OS_VERSION" = "6" ]; then
        /etc/init.d/tomcat-local kill &> /dev/null
      else
        systemctl stop tomcat-local
      fi
    fi
    show_status_ok

    show_status " Backup old versions..."
    BACKUP_DIR_TOMCATPUBLIC=$(get_backup_dir $DIR_APP_TOMCATPUBLIC)
    mv $DIR_APP_TOMCATPUBLIC $BACKUP_DIR_TOMCATPUBLIC
    BACKUP_DIR_TOMCATLOCAL=$(get_backup_dir $DIR_APP_TOMCATLOCAL)
    mv $DIR_APP_TOMCATLOCAL $BACKUP_DIR_TOMCATLOCAL
    show_status_ok

    show_status " Deploy new version (tomcat-public)..."
    mkdir "$DIR_APP_TOMCATPUBLIC"
    tar xzf "$tomcat_app_file" -C "$DIR_APP_TOMCATPUBLIC"
    dir_into_tar=`dir $DIR_APP_TOMCATPUBLIC | tr -d "\n"`
    mv "$DIR_APP_TOMCATPUBLIC"/"$dir_into_tar"/* "$DIR_APP_TOMCATPUBLIC"
    rm -rf "$DIR_APP_TOMCATPUBLIC"/"$dir_into_tar"/
    rm -rf "$DIR_APP_TOMCATPUBLIC"/webapps/*
    show_status_ok

    show_status " Deploy new version (tomcat-local)..."
    mkdir "$DIR_APP_TOMCATLOCAL"
    tar xzf "$tomcat_app_file" -C "$DIR_APP_TOMCATLOCAL"
    dir_into_tar=`dir $DIR_APP_TOMCATLOCAL | tr -d "\n"`
    mv "$DIR_APP_TOMCATLOCAL"/"$dir_into_tar"/* "$DIR_APP_TOMCATLOCAL"
    rm -rf "$DIR_APP_TOMCATLOCAL"/"$dir_into_tar"/
    rm -rf "$DIR_APP_TOMCATLOCAL"/webapps/*
    show_status_ok

    show_status " Configure (tomcat-public)..."
    cp "$BACKUP_DIR_TOMCATPUBLIC"/webapps/*.war "$DIR_APP_TOMCATPUBLIC"/webapps/
    cp -f "$BACKUP_DIR_TOMCATPUBLIC"/bin/startup.sh "$DIR_APP_TOMCATPUBLIC"/bin
    cp -f "$BACKUP_DIR_TOMCATPUBLIC"/bin/debugging.sh "$DIR_APP_TOMCATPUBLIC"/bin
    sed -i ':a;N;$!ba;s/connectionTimeout="20000"\n               redirectPort="8443"/connectionTimeout="20000" redirectPort="8443" compression="on" compressionMinSize="2048" noCompressionUserAgents="gozilla, traviata" compressableMimeType="text\/html, text\/xml, text\/css, application\/javascript"/g' "$DIR_APP_TOMCATPUBLIC"/conf/server.xml
    cp -f "$BACKUP_DIR_TOMCATPUBLIC"/lib/log4j-1.2.17.jar "$DIR_APP_TOMCATPUBLIC"/lib &>> $FILELOG
    cp -f "$BACKUP_DIR_TOMCATPUBLIC"/lib/log4j.properties "$DIR_APP_TOMCATPUBLIC"/lib &>> $FILELOG
    cp -f "$BACKUP_DIR_TOMCATPUBLIC"/lib/vl-logging-1.1.43.jar "$DIR_APP_TOMCATPUBLIC"/lib &>> $FILELOG
    chown tomcat-public.monet "$DIR_APP_TOMCATPUBLIC"/* -R
    show_status_ok

    show_status " Configure (tomcat-local)..."
    cp "$BACKUP_DIR_TOMCATLOCAL"/webapps/*.war "$DIR_APP_TOMCATLOCAL"/webapps/
    cp -f "$BACKUP_DIR_TOMCATLOCAL"/bin/startup.sh "$DIR_APP_TOMCATLOCAL"/bin
    cp -f "$BACKUP_DIR_TOMCATLOCAL"/bin/debugging.sh "$DIR_APP_TOMCATLOCAL"/bin
    sed -i "s/8005/8006/g" "$DIR_APP_TOMCATLOCAL"/conf/server.xml
    sed -i "s/8080/8081/g" "$DIR_APP_TOMCATLOCAL"/conf/server.xml
    sed -i "s/8443/8444/g" "$DIR_APP_TOMCATLOCAL"/conf/server.xml
    sed -i "s/8009/8010/g" "$DIR_APP_TOMCATLOCAL"/conf/server.xml
    cp -f "$BACKUP_DIR_TOMCATLOCAL"/lib/log4j-1.2.17.jar "$DIR_APP_TOMCATLOCAL"/lib
    cp -f "$BACKUP_DIR_TOMCATLOCAL"/lib/log4j.properties "$DIR_APP_TOMCATLOCAL"/lib
    cp -f "$BACKUP_DIR_TOMCATLOCAL"/lib/vl-logging-1.1.43.jar "$DIR_APP_TOMCATLOCAL"/lib
    chown tomcat-local.monet "$DIR_APP_TOMCATLOCAL"/* -R
    show_status_ok

    show_status " Start services... "
    if [ "$OS_VERSION" = "6" ]; then
      /etc/init.d/tomcat-public start &> /dev/null
      /etc/init.d/tomcat-local start &> /dev/null
    else
      systemctl start tomcat-public
      systemctl start tomcat-local
    fi
    show_status_ok
  else
    show_info_text " Current version is updated."
  fi
}
