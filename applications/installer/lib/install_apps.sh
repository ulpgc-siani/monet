#-------------------------------------------------------------------------------
function install_dialog {
  dialog --version &> /dev/null
  if [ $? != 0 ]; then
    show_info "Install dialog... "
    yum -y install dialog >> $FILELOG
  fi
}

function install_needed_apps {
  show_info_text "Install needed apps... "
#  yum clean all >> $FILELOG
  yum -y update >> $FILELOG
  yum -y install unzip wget zip setuptool nfs-utils >> $FILELOG
}

function install_java {
  show_info "Install java... "
  yum -y install java-1.8.0-openjdk >> $FILELOG
}

function configure_java {
  certificate_files=`ls $DIR_RESOURCES/cacerts`
  answer=yes
  language=`locale | grep LANG | cut -d= -f2 | cut -d_ -f1`
  if [ "$language" = 'es' ]; then
    answer=si
  fi

  for certificate_file in $certificate_files
  do
    certificate_alias=`basename $certificate_file .crt`
    echo "$answer" | keytool -import -storepass changeit -alias $certificate_alias -keystore /usr/lib/jvm/jre/lib/security/cacerts -file "$DIR_RESOURCES"/cacerts/"$certificate_file" &>> $FILELOG
  done
}

function unconfigure_java {
  certificate_files=`ls $DIR_RESOURCES/cacerts`
  for certificate_file in $certificate_files
  do
    certificate_alias=`basename $certificate_file .crt`
    keytool -delete -storepass changeit -alias $certificate_alias -keystore /usr/lib/jvm/jre/lib/security/cacerts &>> $FILELOG
  done
}

function install_mysql {
  DATABASE_NAME="mysql"
  DATABASE_SERVICE="mysqld"
  DATABASE_SERVICE_FILE="/etc/init.d/mysqld"

  if [ "$OS_VERSION" = "7" ]; then
    DATABASE_NAME="mariadb"
    DATABASE_SERVICE="mariadb"
    DATABASE_SERVICE_FILE="/usr/lib/systemd/system/mariadb.service"
  fi

  if [ "$INSTALL_MYSQL_SERVER" = "true" ]; then
    show_info " Install mysql server... "
    if [ ! -f $DATABASE_SERVICE_FILE ]; then
      show_status "  Downloading and install... "
      yum -y install $DATABASE_NAME-server &>> $FILELOG
      show_status_ok

      show_status "  Downloading and install nfs... "
      yum -y install $DATABASE_NAME-server &>> $FILELOG
      show_status_ok

      show_status "  Configure service... "
      chkconfig $DATABASE_SERVICE on &>> $FILELOG
      show_status_ok
    else
      show_info "  Mysql server is installed."
    fi

    # Configure mysql
    cmp -s "$DIR_RESOURCES"/mysql/my.cnf /etc/my.cnf > /dev/null
    if [ $? -eq 1 ]; then
      show_status "  Configure... "
      mv /etc/my.cnf /etc/my.cnf`date +'%d-%m-%Y_%H%M%S'`
      cp "$DIR_RESOURCES"/mysql/my.cnf /etc/my.cnf
      show_status_ok
    fi
    show_status "  Restart... "

    service $DATABASE_SERVICE restart &>> $FILELOG

#    if [ "$OS_VERSION" = "6" ]; then
#      execute "lokkit -p 3306:tcp"
#    fi
    if [ "$OS_VERSION" = "7" -a "$FIREWALL" = "0" ]; then
      execute "firewall-cmd --permanent --add-port=3306/tcp" &>> $FILELOG
      execute "firewall-cmd --reload" &>> $FILELOG
    fi

    show_status_ok
  else
    show_info " Install mysql client... "
    yum -y install $DATABASE_NAME &>> $FILELOG
  fi

}

function install_nfs_server {
  show_info " Install nfs server... "
  show_status "  Downloading and install... "
  execute "yum -y install nfs-utils nfs-utils-lib"

#   if [ "$OS_VERSION" = "6" ]; then
#     execute "lokkit -s nfs"
#   fi
   if [ "$OS_VERSION" = "7" -a "$FIREWALL" = "0" ]; then
     execute "firewall-cmd --permanent --add-service=nfs"
     execute "firewall-cmd --reload"

     systemctl enable rpcbind &>> $FILELOG
     systemctl enable nfs-server &>> $FILELOG
   fi

  cp "$DIR_RESOURCES"/nfs/restart_nfs.sh $HOME/restart_nfs.sh &>> $FILELOG
  chmod a+x $HOME/restart_nfs.sh &>> $FILELOG

  show_status_ok
}

function install_nfs_client {
  show_info " Install nfs client... "
  show_status "  Downloading and install... "
  execute "yum -y install nfs-utils"

  show_status_ok
}

function install_mmonitor {
  show_info " Install Monet Monitor... "

  mkdir /opt/mmonitor
  cp "$DIR_RESOURCES"/mmonitor/mmonitor.jar /opt/mmonitor/mmonitor.jar
  cp "$DIR_RESOURCES"/mmonitor/log4j2.xml /opt/mmonitor/log4j2.xml
  cp "$DIR_RESOURCES"/mmonitor/mmonitor.sh /opt/mmonitor/mmonitor.sh

#  mkdir $HOME/.mmonitor
#  mkdir $HOME/.mmonitor/logs


#  mmonitor_log_dir="$HOME/.mmonitor/logs/monitor.log"
#  mmonitor_log_dir_escaped=$(sed 's/\//\\\//g' <<< "$mmonitor_log_dir")

#  sed -i "s/#file_log#/$mmonitor_log_dir_escaped/g" $HOME/.mmonitor/log4j.properties

#  cp "$DIR_RESOURCES"/mmonitor/mmonitor.dist.config $HOME/.mmonitor/mmonitor.config
#  sed -i "s/#smtphost#/smtp.gmail.com/g" $HOME/.mmonitor/mmonitor.config
#  sed -i "s/#smtpuser#/$MAIL_ADMIN_USERNAME/g" $HOME/.mmonitor/mmonitor.config
#  sed -i "s/#smtppassword#/$MAIL_ADMIN_PASSWORD/g" $HOME/.mmonitor/mmonitor.config
#  sed -i "s/#emailto#/$MAIL_ADMIN_TO/g" $HOME/.mmonitor/mmonitor.config
#  sed -i "s/#emailfrom#/$MAIL_ADMIN_FROM/g" $HOME/.mmonitor/mmonitor.config
#  sed -i "s/#checkdisk#/$MONITOR_DATA_SIZE_CHECK/g" $HOME/.mmonitor/mmonitor.config
#  sed -i "s/#disk#/$MONITOR_DATA_DIR/g" $HOME/.mmonitor/mmonitor.config

  sed -i "s/#max_size_gb#/$MONITOR_MAX_SIZE_GB/g" /opt/mmonitor/mmonitor.sh
  sed -i "s/#disks#/$MONITOR_DISKS/g" /opt/mmonitor/mmonitor.sh

#  sed -i "s/#project_name#/$PROJECT_NAME/g" $HOME/.mmonitor/mmonitor.config
#  sed -i "s/#server_type#/$SERVER_TYPE/g" $HOME/.mmonitor/mmonitor.config
#  sed -i "s/#server_domain#/$SERVER_DOMAIN/g" $HOME/.mmonitor/mmonitor.config

  sed -i "s/#project_name#/$PROJECT_NAME/g" /opt/mmonitor/mmonitor.sh
  sed -i "s/#server_type#/$SERVER_TYPE/g" /opt/mmonitor/mmonitor.sh
  sed -i "s/#server_domain#/$SERVER_DOMAIN/g" /opt/mmonitor/mmonitor.sh

  sed -i "s/#slack_token#/$SLACK_TOKEN/g" /opt/mmonitor/mmonitor.sh
  sed -i "s/#slack_channel#/$SLACK_CHANNEL/g" /opt/mmonitor/mmonitor.sh

  mmonitor_file="/opt/mmonitor/mmonitor.sh"
  mmonitor_file_escaped=$(sed 's/\//\\\//g' <<< "$mmonitor_file")
  sed -i "/00 6 \* \* \* root $mmonitor_file_escaped/d" /etc/crontab
  echo "00 6 * * * root /opt/mmonitor/mmonitor.sh" >> /etc/crontab
}

function uninstall_mmonitor {
  show_info " Uninstall Monet Monitor... "

  mmonitor_file="/opt/mmonitor/mmonitor.sh"
  mmonitor_file_escaped=$(sed 's/\//\\\//g' <<< "$mmonitor_file")
  sed -i "/00 6 \* \* \* root $mmonitor_file_escaped/d" /etc/crontab

  rm -rf /opt/mmonitor
#  rm -rf $HOME/.mmonitor
}
