#-------------------------------------------------------------------------------
function install_tomcat-local {
  show_info " Install tomcat-local..."

  show_status "  Configure... "
  adduser tomcat-local -g monet
  chmod g+rwx /home/tomcat-local
  if [ -d "$DIR_APP_TOMCATLOCAL" ]; then
    show_error "tomcat-local app directory exists. Try uninstalling first."
    exit
  fi

  mkdir "$DIR_APP_TOMCATLOCAL"
  tar xzf "$TOMCAT_APP_FILE" -C "$DIR_APP_TOMCATLOCAL"
  dir_into_tar=`dir $DIR_APP_TOMCATLOCAL | tr -d "\n"`
  mv "$DIR_APP_TOMCATLOCAL"/"$dir_into_tar"/* "$DIR_APP_TOMCATLOCAL"
  rm -rf "$DIR_APP_TOMCATLOCAL"/"$dir_into_tar"/
  rm -rf "$DIR_APP_TOMCATLOCAL"/webapps/*

#  cp "$DIR_RESOURCES"/tomcat/bin/* "$DIR_APP_TOMCATLOCAL"/bin
  cp "$DIR_RESOURCES"/tomcat/lib/* "$DIR_APP_TOMCATLOCAL"/lib
  cp "$DIR_RESOURCES"/tomcat-local/bin/* "$DIR_APP_TOMCATLOCAL"/bin
  chown tomcat-local.monet "$DIR_APP_TOMCATLOCAL"/* -R

  sed -i "s/8005/8006/g" "$DIR_APP_TOMCATLOCAL"/conf/server.xml
  sed -i "s/8080/8081/g" "$DIR_APP_TOMCATLOCAL"/conf/server.xml
  sed -i "s/8443/8444/g" "$DIR_APP_TOMCATLOCAL"/conf/server.xml
  sed -i "s/8009/8010/g" "$DIR_APP_TOMCATLOCAL"/conf/server.xml

  sed -i "s/#logginhub#/$LOGGINGHUB/g" "$DIR_APP_TOMCATLOCAL"/lib/log4j.properties
  sed -i "s/#source_application#/tomcat-local/g" "$DIR_APP_TOMCATLOCAL"/lib/log4j.properties

  if [ -d "$APPS_DIR" ]; then
    mkdir -p "$APPS_DIR"/tomcat-local/webapps
    chown tomcat-local.monet "$APPS_DIR"/tomcat-local/webapps
    rmdir "$DIR_APP_TOMCATLOCAL"/webapps
    ln -s "$APPS_DIR"/tomcat-local/webapps "$DIR_APP_TOMCATLOCAL"/webapps
  fi

  show_status_ok

  show_status "  Install service... "
  if [ "$OS_VERSION" = "6" ]; then
    cp "$DIR_RESOURCES/tomcat-local/services/tomcat-local" /etc/init.d
    chmod a+x /etc/init.d/tomcat-local
    cp "$DIR_RESOURCES/tomcat-local/services/tomcat-local-debug" /etc/init.d
    chmod a+x /etc/init.d/tomcat-local-debug
    chkconfig --add tomcat-local
    chkconfig tomcat-local on
    /etc/init.d/tomcat-local start >> $FILELOG
  else
    cp "$DIR_RESOURCES/tomcat-local/services/tomcat-local.service" /etc/systemd/system
    cp "$DIR_RESOURCES/tomcat-local/services/tomcat-local-debug.service" /etc/systemd/system
    systemctl daemon-reload
    systemctl stop tomcat-local
    systemctl enable tomcat-local &>> $FILELOG
    systemctl start tomcat-local
  fi
  show_status_ok
}

function uninstall_tomcat-local {
  show_info " Uninstall tomcat-local..."

  show_status "  Remove service... "
  if [ "$OS_VERSION" = "6" ]; then
    /etc/init.d/tomcat-local kill&> /dev/null
    chkconfig --del tomcat-local &>> $FILELOG
    rm -f /etc/init.d/tomcat-local
    rm -f /etc/init.d/tomcat-local-debug
  else
    systemctl stop tomcat-local
    systemctl disable tomcat-local &>> $FILELOG
    rm -f /etc/systemd/system/tomcat-local.service &>> $FILELOG
    rm -f /etc/systemd/system/tomcat-local-debug.service &>> $FILELOG
  fi
  show_status_ok

  show_status "  Delete user tomcat-local... "
  userdel -r -f tomcat-local &>> $FILELOG
  show_status_ok

  show_status "  Remove tomcat-local app directory... "
  rm -rf "$DIR_APP_TOMCATLOCAL"
  if [ -d "$APPS_DIR" ]; then
    rm -rf "$APPS_DIR"/tomcat-local
  fi
  show_status_ok
}
