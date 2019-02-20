#-------------------------------------------------------------------------------
function install_tomcat-public {
  show_info " Install tomcat-public..."

  show_status "  Configure... "

  adduser tomcat-public -g monet
  chmod g+rwx /home/tomcat-public
  if [ -d "$DIR_APP_TOMCATPUBLIC" ]; then
    show_error "tomcat-public app directory exists. Try uninstalling first."
    exit
  fi

  mkdir "$DIR_APP_TOMCATPUBLIC"
  tar xzf "$TOMCAT_APP_FILE" -C "$DIR_APP_TOMCATPUBLIC"
  dir_into_tar=`dir $DIR_APP_TOMCATPUBLIC | tr -d "\n"`
  mv "$DIR_APP_TOMCATPUBLIC"/"$dir_into_tar"/* "$DIR_APP_TOMCATPUBLIC"
  rm -rf "$DIR_APP_TOMCATPUBLIC"/"$dir_into_tar"/
  rm -rf "$DIR_APP_TOMCATPUBLIC"/webapps/*

  cp "$DIR_RESOURCES"/tomcat/lib/* "$DIR_APP_TOMCATPUBLIC"/lib
  cp "$DIR_RESOURCES"/tomcat-public/bin/* "$DIR_APP_TOMCATPUBLIC"/bin
  chown tomcat-public.monet "$DIR_APP_TOMCATPUBLIC"/* -R

  sed -i ':a;N;$!ba;s/connectionTimeout="20000"\n               redirectPort="8443"/connectionTimeout="20000" redirectPort="8443" compression="on" compressionMinSize="2048" noCompressionUserAgents="gozilla, traviata" compressableMimeType="text\/html, text\/xml, text\/css, application\/javascript"/g' "$DIR_APP_TOMCATPUBLIC"/conf/server.xml

  sed -i "s/#logginhub#/$LOGGINGHUB/g" "$DIR_APP_TOMCATPUBLIC"/lib/log4j.properties
  sed -i "s/#source_application#/tomcat-public/g" "$DIR_APP_TOMCATPUBLIC"/lib/log4j.properties

  if [ -d "$APPS_DIR" ]; then
    mkdir -p "$APPS_DIR"/tomcat-public/webapps
    chown tomcat-public.monet "$APPS_DIR"/tomcat-public/webapps
    rmdir "$DIR_APP_TOMCATPUBLIC"/webapps
    ln -s "$APPS_DIR"/tomcat-public/webapps "$DIR_APP_TOMCATPUBLIC"/webapps
  fi

  show_status_ok

  show_status "  Install service... "
  if [ "$OS_VERSION" = "6" ]; then
    cp "$DIR_RESOURCES/tomcat-public/services/tomcat-public" /etc/init.d
    chmod a+x /etc/init.d/tomcat-public
    cp "$DIR_RESOURCES/tomcat-public/services/tomcat-public-debug" /etc/init.d
    chmod a+x /etc/init.d/tomcat-public-debug
    chkconfig --add tomcat-public
    chkconfig tomcat-public on
    /etc/init.d/tomcat-public start >> $FILELOG
  else
    cp "$DIR_RESOURCES/tomcat-public/services/tomcat-public.service" /etc/systemd/system
    cp "$DIR_RESOURCES/tomcat-public/services/tomcat-public-debug.service" /etc/systemd/system
    systemctl daemon-reload
    systemctl stop tomcat-public
    systemctl enable tomcat-public &>> $FILELOG
    systemctl start tomcat-public
  fi

  show_status_ok
}

function uninstall_tomcat-public {
  show_info " Uninstall tomcat-public... "

  show_status "  Remove service... "
  if [ "$OS_VERSION" = "6" ]; then
    /etc/init.d/tomcat-public kill &> /dev/null
    chkconfig --del tomcat-public &>> $FILELOG
    rm -f /etc/init.d/tomcat-public &>> $FILELOG
    rm -f /etc/init.d/tomcat-public-debug &>> $FILELOG
  else
    systemctl stop tomcat-public
    systemctl disable tomcat-public &>> $FILELOG
    rm -f /etc/systemd/system/tomcat-public.service &>> $FILELOG
    rm -f /etc/systemd/system/tomcat-public-debug.service &>> $FILELOG
  fi
  show_status_ok

  show_status "  Delete user tomcat-public... "
  userdel -r -f tomcat-public &>> $FILELOG
  show_status_ok

  show_status "  Remove tomcat-public app directory... "
  rm -rf "$DIR_APP_TOMCATPUBLIC"

  if [ -d "$APPS_DIR" ]; then
    rm -rf "$APPS_DIR"/tomcat-public
  fi
  show_status_ok
}
