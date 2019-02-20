#-------------------------------------------------------------------------------
function install_deployservice {
  show_info " Install deployservice..."
  if [ -d "$DIR_APP_DEPLOYSERVICE" ]; then
    show_error "Deployservice directory exists. Try uninstalling first."
    exit
  fi

  mkdir "$DIR_APP_DEPLOYSERVICE"
  cp "$MONET_APP_DEPLOYSERVICE_FILE" "$DIR_APP_DEPLOYSERVICE"
  cp "$DIR_RESOURCES/deployservice/deployservice.sh" "$DIR_APP_DEPLOYSERVICE"

  show_status "  Configure... "

  if [ -d "$DIR_CONFIG_DEPLOYSERVICE" ]; then
    show_error "Deployservice config directory exists. Try uninstalling first."
    exit
  fi

  mkdir "$DIR_CONFIG_DEPLOYSERVICE"
  mkdir "$DIR_CONFIG_DEPLOYSERVICE/logs"
  mkdir "$DIR_CONFIG_DEPLOYSERVICE/war"
  touch "$DIR_CONFIG_DEPLOYSERVICE/logs/deployservice.log"
  cp "$DIR_RESOURCES/deployservice/deployservice.config" "$DIR_CONFIG_DEPLOYSERVICE"
  cp "$DIR_RESOURCES/deployservice/deployservice.jks" "$DIR_CONFIG_DEPLOYSERVICE"
  cp "$DIR_RESOURCES/deployservice/servers.xml" "$DIR_CONFIG_DEPLOYSERVICE"
  cp "$DIR_RESOURCES/deployservice/docservers.xml" "$DIR_CONFIG_DEPLOYSERVICE"
  cp "$DIR_RESOURCES/deployservice/log4j.properties" "$DIR_CONFIG_DEPLOYSERVICE"
  cp "$DIR_APPS/docservice.war" "$DIR_CONFIG_DEPLOYSERVICE/war"
  cp "$DIR_APPS/federation.war" "$DIR_CONFIG_DEPLOYSERVICE/war"
  cp "$DIR_APPS/monet.war" "$DIR_CONFIG_DEPLOYSERVICE/war"

  PASSWORD=$DEPLOYSERVICE_PASSWORD
  if [ "$PASSWORD" = "" ]; then
    PASSWORD=$GENERATE_PASSWORD
  fi

  PASSWORD_MD5=`echo -n $PASSWORD | md5sum | tr -d ' ' | tr -d '-'`
  sed -i "s/<Password>/$PASSWORD_MD5/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  echo "Deploy service password: $PASSWORD" >> $FILERESULT

  sed -i "s/<MailAdminSpaceHost>/$MAIL_ADMIN_HOST/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  sed -i "s/<MailAdminSpacePort>/$MAIL_ADMIN_PORT/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  sed -i "s/<MailAdminSpaceFrom>/$MAIL_ADMIN_FROM/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  sed -i "s/<MailAdminSpaceTo>/$MAIL_ADMIN_TO/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  sed -i "s/<MailAdminSpaceUsername>/$MAIL_ADMIN_USERNAME/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  sed -i "s/<MailAdminSpacePassword>/$MAIL_ADMIN_PASSWORD/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  sed -i "s/<FederationDefault>/$FEDERATION_DEFAULT_NAME/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  sed -i "s/<ForceDeleteDB>/$DB_FORCE_DELETE/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  sed -i "s/<MonetVersion>/$MONET_VERSION/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"

#  datawarehouse_dir_escaped=$(sed 's/\//\\\//g' <<< "$SPACES_DIR")
#  sed -i "s/<DataWareHouseDir>/$datawarehouse_dir_escaped/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
#  documents_dir_escaped=$(sed 's/\//\\\//g' <<< "$DOCUMENTS_DIR")
#  sed -i "s/<DocumentDisksDir>/$documents_dir_escaped/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"

  spaces_dir_escaped=$(sed 's/\//\\\//g' <<< "$SPACES_DIR")
  sed -i "s/<SpacesDir>/$spaces_dir_escaped/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  documents_dir_escaped=$(sed 's/\//\\\//g' <<< "$DOCUMENTS_DIR")
  sed -i "s/<DocumentsDir>/$documents_dir_escaped/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"

  sed -i "s/#server_name#/$SERVER_NAME/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"

  local url="jdbc:mysql:\/\/$DB_SPACES_DOMAIN\/#dbname#?autoReconnect=true"
  if [ "$DB_SPACES_TYPE" == "oracle" ]; then
    local url="jdbc:oracle:thin:$DB_SPACES_USERNAME\/$DB_SPACES_PASSWORD\@$DB_SPACES_DOMAIN"
  fi

  sed -i "s/#db_spaces_prefix#/$DB_SPACES_PREFIX/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"
  sed -i "s/#db_spaces_username#/$DB_SPACES_USERNAME/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"
  sed -i "s/#db_spaces_password#/$DB_SPACES_PASSWORD/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"
  sed -i "s/#db_federation_prefix#/$DB_FEDERATION_PREFIX/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"
  sed -i "s/#db_create#/$DB_FORCE_DELETE/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"
  sed -i "s/#url#/$url/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"

  sed -i "s/#db_spaces_prefix#/$DB_SPACES_PREFIX/g" "$DIR_CONFIG_DEPLOYSERVICE/docservers.xml"
  sed -i "s/#db_spaces_username#/$DB_SPACES_USERNAME/g" "$DIR_CONFIG_DEPLOYSERVICE/docservers.xml"
  sed -i "s/#db_spaces_password#/$DB_SPACES_PASSWORD/g" "$DIR_CONFIG_DEPLOYSERVICE/docservers.xml"
  sed -i "s/#db_federation_prefix#/$DB_FEDERATION_PREFIX/g" "$DIR_CONFIG_DEPLOYSERVICE/docservers.xml"
  sed -i "s/#url#/$url/g" "$DIR_CONFIG_DEPLOYSERVICE/docservers.xml"

  logginhub_config="false"
  if [ "$LOGGINGHUB" != "" ]; then
    logginhub_config="true"
  fi
  sed -i "s/#logginhub#/$LOGGINGHUB/g" "$DIR_CONFIG_DEPLOYSERVICE/log4j.properties"
  sed -i "s/<AddSocketLog>/$logginghub_config/g" "$DIR_CONFIG_DEPLOYSERVICE/deployservice.config"
  show_status_ok

  show_status "  Install service... "
  if [ "$OS_VERSION" = "6" ]; then
    cp "$DIR_RESOURCES/deployservice/deployservice" /etc/init.d
    chmod a+x /etc/init.d/deployservice
    chkconfig --add deployservice
    chkconfig deployservice on
    /etc/init.d/deployservice start > /dev/null
  else
    cp "$DIR_RESOURCES/deployservice/deployservice.service" /etc/systemd/system
    systemctl daemon-reload
    systemctl stop deployservice
    systemctl enable deployservice &>> $FILELOG
    systemctl start deployservice
  fi
  show_status_ok
}

function uninstall_deployservice {
  show_info " Uninstall deployservice..."

  show_status "  Remove service... "
  if [ "$OS_VERSION" = "6" ]; then
    /etc/init.d/deployservice stop &> $FILELOG
    chkconfig --del deployservice &>> $FILELOG
    rm -f /etc/init.d/deployservice &>> $FILELOG
  else
    systemctl stop deployservice
    systemctl disable deployservice &>> $FILELOG
    rm -f /etc/systemd/system/deployservice.service &>> $FILELOG
  fi
  show_status_ok

  show_status "  Remove directory... "
  rm -rf "$DIR_APP_DEPLOYSERVICE" &>> $FILELOG
  show_status_ok

  show_status "  Remove config... "
  rm -rf "$DIR_CONFIG_DEPLOYSERVICE" &>> $FILELOG
  show_status_ok
}

function restart_deployservice {
  if [ "$OS_VERSION" = "6" ]; then
    /etc/init.d/deployservice restart &> $FILELOG
  else
    systemctl restart deployservice &>> $FILELOG
  fi
}
