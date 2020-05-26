#-------------------------------------------------------------------------------
function load_configuration_relative {
  DB_FORCE_DELETE=false
  if [ "$INSTALL_MYSQL_SERVER" = "true" ]; then
    DB_SPACES_DOMAIN=localhost:3306
    DB_SPACES_USERNAME=root
    DB_SPACES_PASSWORD=
    DB_FORCE_DELETE=true
  fi

  MONET_APP_SPACE_FILE="$DIR_APPS/$MONET_APP_SPACE"
  MONET_APP_DOCSERVICE_FILE="$DIR_APPS/$MONET_APP_DOCSERVICE"
  MONET_APP_FEDERATION_FILE="$DIR_APPS/$MONET_APP_FEDERATION"
  MONET_APP_DEPLOYSERVICE_FILE="$DIR_APPS/$MONET_APP_DEPLOYSERVICE"
  TOMCAT_APP_FILE="$DIR_APPS/$TOMCAT_APP"
  SERVER_NAME="$PROJECT_NAME"-"$SERVER_TYPE"
  DB_SPACES_PREFIX=monet_"$SERVER_TYPE"_"$PROJECT_NAME"_space_
  DB_FEDERATION_PREFIX=monet_"$SERVER_TYPE"_"$PROJECT_NAME"_federation_
  URL_APPS="$URL_MONET_BASE/$MONET_VERSION"
  FEDERATION_FULL=false
  if [ "$SERVER_TYPE" != "dev" -a "$SERVER_TYPE" != "demo" ]; then
    FEDERATION_FULL=true
    URL_APPS="$URL_MONET_BASE/$MONET_VERSION/full"
  else
    DEPLOYSERVICE_PASSWORD="1234"
  fi

  if [ "$DB_SPACES_NAME" != "" ]; then
    DB_SPACES_PREFIX=""
  fi
  if [ "$DB_FEDERATION_NAME" != "" ]; then
    DB_FEDERATION_PREFIX=""
  fi

  if [ "$DB_SPACES_TYPE" == "" ]; then
    DB_SPACES_TYPE="mysql"
  fi
  if [ "$DB_FEDERATION_TYPE" == "" ]; then
    DB_FEDERATION_TYPE=$DB_SPACES_TYPE
  fi
  if [ "$DB_FEDERATION_DOMAIN" == "" ]; then
    DB_FEDERATION_DOMAIN=$DB_SPACES_DOMAIN
  fi
  if [ "$DB_FEDERATION_USERNAME" == "" ]; then
    DB_FEDERATION_USERNAME=$DB_SPACES_USERNAME
  fi
  if [ "$DB_FEDERATION_PASSWORD" == "" ]; then
    DB_FEDERATION_PASSWORD=$DB_SPACES_PASSWORD
  fi
  if [ "$MAIL_ADMIN_HOST" == "" ]; then
    MAIL_ADMIN_HOST="smtp.gmail.com"
  fi
  if [ "$MAIL_ADMIN_PORT" == "" ]; then
    MAIL_ADMIN_HOST="587"
  fi

  LOGGINGHUB=""
#  if [ "$SERVER_TYPE" = "dev" ]; then
#    LOGGINGHUB=", logginghub"
#  fi

}

function load_configuration {
  if [ "$1" != "install-nfs-server" -a "$1" != "install-nfs-client" -a "$1" != "install-only-database" ]; then
    URL_MONET_BASE=`cat $FILECONFIG |grep url_monet_base= | sed "s/.*=//g"`
    MONET_VERSION=`cat $FILECONFIG |grep monet_version= | sed "s/.*=//g"`
    MONET_APP_SPACE=`cat $FILECONFIG |grep monet_app_space= | sed "s/.*=//g"`
    MONET_APP_DOCSERVICE=`cat $FILECONFIG |grep monet_app_docservice= | sed "s/.*=//g"`
    MONET_APP_FEDERATION=`cat $FILECONFIG |grep monet_app_federation= | sed "s/.*=//g"`
    MONET_APP_DEPLOYSERVICE=`cat $FILECONFIG |grep monet_app_deployservice= | sed "s/.*=//g"`
    URL_TOMCAT_BASE=`cat $FILECONFIG |grep url_tomcat_base= | sed "s/.*=//g"`
    TOMCAT_APP=`cat $FILECONFIG |grep tomcat_app= | sed "s/.*=//g"`

    MAIL_ADMIN_HOST=`cat $FILECONFIG |grep mail_admin_host= | sed "s/.*=//g"`
    MAIL_ADMIN_PORT=`cat $FILECONFIG |grep mail_admin_port= | sed "s/.*=//g"`
    MAIL_ADMIN_FROM=`cat $FILECONFIG |grep mail_admin_from= | sed "s/.*=//g"`
    MAIL_ADMIN_TO=`cat $FILECONFIG |grep mail_admin_to= | sed "s/.*=//g"`
    MAIL_ADMIN_USERNAME=`cat $FILECONFIG |grep mail_admin_username= | sed "s/.*=//g"`
    MAIL_ADMIN_PASSWORD=`cat $FILECONFIG |grep mail_admin_password= | sed "s/.*=//g"`

    SLACK_TOKEN=`cat $FILECONFIG |grep slack_token= | sed "s/.*=//g"`
    SLACK_CHANNEL=`cat $FILECONFIG |grep slack_channel= | sed "s/.*=//g"`

    PROJECT_NAME=`cat $FILECONFIG |grep project_name= | sed "s/.*=//g"`
    SERVER_TYPE=`cat $FILECONFIG |grep server_type= | sed "s/.*=//g"`

    SERVER_DOMAIN=`cat $FILECONFIG |grep server_domain= | sed "s/.*=//g"`
    PROXY_IP=`cat $FILECONFIG |grep proxy_ip= | sed "s/.*=//g"`
    FEDERATION_DEFAULT_NAME=`cat $FILECONFIG |grep federation_default_name= | sed "s/.*=//g"`
    FEDERATION_DEFAULT_LABEL=`cat $FILECONFIG |grep federation_default_label= | sed "s/.*=//g"`
    FEDERATION_PORT=`cat $FILECONFIG |grep federation_port= | sed "s/.*=//g"`
    FEDERATION_LOCAL=`cat $FILECONFIG |grep federation_local= | sed "s/federation_local=//g"`
    FEDERATIONS_REMOTE=`cat $FILECONFIG |grep federations_remote= | sed "s/federations_remote=//g"`

    INSTALL_MYSQL_SERVER=`cat $FILECONFIG |grep install_mysql_server= | sed "s/.*=//g"`

    DB_SPACES_TYPE=`cat $FILECONFIG |grep db_spaces_type= | sed "s/.*=//g"`
    DB_SPACES_NAME=`cat $FILECONFIG |grep db_spaces_name= | sed "s/.*=//g"`
    DB_SPACES_DOMAIN=`cat $FILECONFIG |grep db_spaces_domain=`; DB_SPACES_DOMAIN=${DB_SPACES_DOMAIN#*=};

    DB_SPACES_USERNAME=`cat $FILECONFIG |grep db_spaces_username= | sed "s/.*=//g"`
    DB_SPACES_PASSWORD=`cat $FILECONFIG |grep db_spaces_password= | sed "s/.*=//g"`

    DB_FEDERATION_TYPE=`cat $FILECONFIG |grep db_federation_type= | sed "s/.*=//g"`
    DB_FEDERATION_NAME=`cat $FILECONFIG |grep db_federation_name= | sed "s/.*=//g"`
    DB_FEDERATION_DOMAIN=`cat $FILECONFIG |grep db_federation_domain=`; DB_FEDERATION_DOMAIN=${DB_FEDERATION_DOMAIN#*=};
    DB_FEDERATION_USERNAME=`cat $FILECONFIG |grep db_federation_username= | sed "s/.*=//g"`
    DB_FEDERATION_PASSWORD=`cat $FILECONFIG |grep db_federation_password= | sed "s/.*=//g"`

    DOCUMENTS_DIR=`cat $FILECONFIG |grep documents_dir= | sed "s/.*=//g"`
    FEDERATIONS_DIR=`cat $FILECONFIG |grep federations_dir= | sed "s/.*=//g"`
    SPACES_DIR=`cat $FILECONFIG |grep spaces_dir= | sed "s/.*=//g"`
    APPS_DIR=`cat $FILECONFIG |grep apps_dir= | sed "s/.*=//g"`

#    MONITOR_DATA_SIZE_CHECK=`cat $FILECONFIG |grep monitor_data_size_check= | sed "s/.*=//g"`
#    MONITOR_DATA_DIR=`cat $FILECONFIG |grep monitor_data_dir= | sed "s/.*=//g"`

    MONITOR_DISKS=`cat $FILECONFIG |grep monitor_disks= | sed "s/.*=//g"`
    MONITOR_DISKS=$(sed 's/\//\\\//g' <<< "$MONITOR_DISKS")
    MONITOR_MAX_SIZE_GB=`cat $FILECONFIG |grep monitor_max_size_gb= | sed "s/.*=//g"`

    OPERATORS_SSH_KEYS=`cat $FILECONFIG |grep operators_ssh_keys=`; OPERATORS_SSH_KEYS=${OPERATORS_SSH_KEYS#*=};

    load_configuration_relative
  fi
}

function get_federation_db_name {
  local result=$DB_FEDERATION_PREFIX$1
  if [ ! -z $DB_FEDERATION_NAME ]; then
    local result=$DB_FEDERATION_NAME
  fi
  result=`echo $result | tr '[:upper:]' '[:lower:]'`
  echo "$result";
}

#-------------------------------------------------------------------------------

function check_requisites {
  if [ "$OS_VERSION" != "6" -a "$OS_VERSION" != "7" ]; then
    show_error "Sorry, this operating system is not compatible."
    exit 1
  fi

  if [ "$(id -u)" != "0" ]; then
    show_error "Sorry, you are not root."
    exit 1
  fi

  if [ "$1" != "install" -a "$1" != "uninstall" -a "$1" != "clean-install" -a "$1" != "update" -a "$1" != "add-federation" -a "$1" != "delete-federation" -a "$1" != "install-only-database" -a "$1" != "install-nfs-server" -a "$1" != "install-nfs-client" -a "$1" != "upgrade-federation" -a "$1" != "upgrade-tomcat" ]; then
    echo "Use: $0 [install | uninstall | clean-install | update | add-federation <name> '<label>'| delete-federation <name> | install-only-database | install-nfs-server | install-nfs-client | upgrade-federation | upgrade-tomcat]"
    echo ""
    exit
  fi

  if [ "$1" = "add-federation" ]; then
    DIALOG_ENABLED=false
  fi

  if [ "$1" = "delete-federation" ]; then
    DIALOG_ENABLED=false
  fi

  if [ ! -f $FILECONFIG -a "$1" != "install-nfs-server" -a "$1" != "install-nfs-client" -a "$1" != "install-only-database" ]; then
    show_error "File '$FILECONFIG' not exists."
    exit
  fi

  if [ ! -d $DIR_APPS ]; then
    mkdir $DIR_APPS
  fi
}

function check_configuration {
  if [ "$URL_MONET_BASE" = "" ]; then
    show_error "Installation must be a monet base url"
    exit
  fi
}

function check_applications {
  show_info "Checking applications... "

  PROGRESS=5
  if [ ! -f "$MONET_APP_SPACE_FILE" ]; then
    show_info "Downloading $MONET_APP_SPACE_FILE..."
    wget --no-check-certificate "$URL_APPS/$MONET_APP_SPACE" -O "$MONET_APP_SPACE_FILE" &>> $FILELOG|| rm "$MONET_APP_SPACE_FILE"
  fi

  if [ ! -f "$MONET_APP_SPACE_FILE" ]; then
    show_error "File '$MONET_APP_SPACE_FILE' not exists."
    exit
  fi

  PROGRESS=20
  if [ ! -f "$MONET_APP_DOCSERVICE_FILE" ]; then
    show_info "Downloading $MONET_APP_DOCSERVICE_FILE..."
    wget --no-check-certificate "$URL_APPS/$MONET_APP_DOCSERVICE" -O "$MONET_APP_DOCSERVICE_FILE" &>> $FILELOG|| rm "$MONET_APP_DOCSERVICE_FILE"
  fi

  if [ ! -f "$MONET_APP_DOCSERVICE_FILE" ]; then
    show_error "File '$MONET_APP_DOCSERVICE_FILE' not exists."
    exit
  fi

  PROGRESS=40
  if [ ! -f "$MONET_APP_FEDERATION_FILE" ]; then
    show_info "Downloading $MONET_APP_FEDERATION_FILE..."
    wget --no-check-certificate "$URL_APPS/$MONET_APP_FEDERATION" -O "$MONET_APP_FEDERATION_FILE" &>> $FILELOG|| rm "$MONET_APP_FEDERATION_FILE" &>> $FILELOG
  fi

  if [ ! -f "$MONET_APP_FEDERATION_FILE" ]; then
    show_error "File '$MONET_APP_FEDERATION_FILE' not exists."
    exit
  fi

  PROGRESS=70
  if [ ! -f "$MONET_APP_DEPLOYSERVICE_FILE" ]; then
    show_info "Downloading $MONET_APP_DEPLOYSERVICE_FILE..."
    wget --no-check-certificate "$URL_MONET_BASE/$MONET_APP_DEPLOYSERVICE" -O "$MONET_APP_DEPLOYSERVICE_FILE" &>> $FILELOG|| rm "$MONET_APP_DEPLOYSERVICE_FILE"
  fi

  if [ ! -f "$MONET_APP_DEPLOYSERVICE_FILE" ]; then
    show_error "File '$MONET_APP_DEPLOYSERVICE_FILE' not exists."
    exit
  fi

  PROGRESS=90
  if [ ! -f "$TOMCAT_APP_FILE" ]; then
    show_info "Downloading $TOMCAT_APP_FILE..."
    wget --no-check-certificate "$URL_TOMCAT_BASE/$TOMCAT_APP" -O "$TOMCAT_APP_FILE" &>> $FILELOG || rm "$TOMCAT_APP_FILE"
  fi

  if [ ! -f "$TOMCAT_APP_FILE" ]; then
    show_error "File '$TOMCAT_APP_FILE' not exists."
    exit
  fi
}

#-------------------------------------------------------------------------------
function get_backup_dir {
  FILENAME=$1
  FILENAME_BACKUP=$FILENAME.backup

  FILENAME_NEW_BACKUP=$FILENAME_BACKUP

  x=1
  while [ -d $FILENAME_NEW_BACKUP ]; do
    FILENAME_NEW_BACKUP=$FILENAME_BACKUP.$x
    x=$((x+1))
  done
  echo $FILENAME_NEW_BACKUP
}
