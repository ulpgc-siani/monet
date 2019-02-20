#-------------------------------------------------------------------------------
function show_welcome {
  message="Welcome to $APP_NAME\nMonet version: $1\n\n"
  printf $message >> $FILELOG

  dialog --version &> /dev/null
  if [ $? = 0 -a "$DIALOG_ENABLED" = "true" ]; then
    dialog --shadow --backtitle "$APP_NAME" \
    --title "Welcome" \
    --msgbox "\n$message" 9 50
    clear
  else
    printf "$message"
  fi
}

function show_error {
  echo "Error: $1" >> $FILELOG

  dialog --version &> /dev/null
  if [ $? = 0 -a "$DIALOG_ENABLED" = "true" ]; then
    dialog --shadow --backtitle "$APP_NAME" \
    --title "Error" \
    --msgbox "\n$1" 8 70
    clear
  else
    printf "Error: $1\n"
  fi
}

function show_info {
#  CURRENT_OP=$1
  CURRENT_OP=`echo $1 | sed 's/^ *//'`
  echo "$1" >> $FILELOG
  dialog --version &> /dev/null
  if [ $? = 0 -a "$DIALOG_ENABLED" = "true" -a "$PROGRESS_ENABLED" = "true" ]; then
    echo "$PROGRESS" | dialog --shadow --backtitle "$APP_NAME" --gauge "Please wait\n\n$1" 10 70 0
  else
    printf "$1\n"
  fi
}

function show_info_text {
  echo "$1" >> $FILELOG
  printf "$1\n"
}

function show_status {
  CURRENT_OP=`echo $1 | sed 's/^ *//'`
  echo "$1" >> $FILELOG
  dialog --version &> /dev/null
  if [ $? != 0 -o "$DIALOG_ENABLED" = "false" ]; then
    echo -n "$1"
  fi
}

function show_status_ok {
  dialog --version &> /dev/null
  if [ $? != 0 -o "$DIALOG_ENABLED" = "false" ]; then
    echo "OK"
  fi
}

function execute {
  LAST_MESSAGE=`$1`
  LAST_EXIT=$?
  echo $LAST_MESSAGE &>> $FILELOG

  if [ $LAST_EXIT != 0 ]; then
    show_error "Current process: $CURRENT_OP\nMessage: $LAST_MESSAGE"
    exit 1
  fi
}

function show_dialog_federation {
  federation_name=$1
  federation_label=$2

  exec 3>&1
  VALUES=$(dialog --backtitle "$APP_NAME" --separator ";"\
    --title "Add federation" \
    --form "Configuration" 10 50 0 \
    "Name:" 1 1	"$federation_name" 	1 10 20 0 \
    "Label:"    2 1	"$federation_label"  	2 10 25 0 \
  2>&1 1>&3)
  exec 3>&-
  DIALOG_RETURN=$VALUES
}

function show_dialog_install {
  server_type=$SERVER_TYPE
  project_name=$PROJECT_NAME
  server_domain=$SERVER_DOMAIN
  proxy_ip=$PROXY_IP

  federation_default_name=$FEDERATION_DEFAULT_NAME
  federation_default_label=$FEDERATION_DEFAULT_LABEL
  federation_port=$FEDERATION_PORT

  install_mysql_server=$INSTALL_MYSQL_SERVER
  db_spaces_domain=$DB_SPACES_DOMAIN
  db_spaces_username=$DB_SPACES_USERNAME
  db_spaces_password=$DB_SPACES_PASSWORD

  mail_admin_from=$MAIL_ADMIN_FROM
  mail_admin_to=$MAIL_ADMIN_TO
  mail_admin_username=$MAIL_ADMIN_USERNAME
  mail_admin_password=$MAIL_ADMIN_PASSWORD

  server_type_dev='off';
  server_type_demo='off';
  server_type_pre='off';
  server_type_exp='off';
  if [ "$server_type" = "exp" ]; then
    server_type_exp='ON'
  else
    if [ "$server_type" = "pre" ]; then
      server_type_pre='ON'
    else
      if [ "$server_type" = "demo" ]; then
        server_type_demo='ON'
      else
        server_type_dev='ON'
      fi
    fi
  fi
  exec 3>&1
  VALUES=$(dialog --backtitle "$APP_NAME" \
    --title "Installation" \
    --radiolist "Please, select you server type " 12 50 5 \
        "dev"  "Developer" $server_type_dev \
        "demo" "Demostration" $server_type_demo \
        "pre"  "Pre-Production" $server_type_pre \
        "exp"  "Production" $server_type_exp \
  2>&1 1>&3)
  exec 3>&-
  SERVER_TYPE=$VALUES

  if [ "$VALUES" = "" ]; then
    echo ""
    exit
  fi
  load_configuration_relative
}

function show_dialog_uninstall {
  dialog --backtitle "$APP_NAME" \
    --title "Uninstall" \
    --defaultno \
    --yesno "Monet all settings will be deleted. Are you sure you want to continue?" 7 60
  if [ "$?" != "0" ]; then
    echo ""
    exit
  fi
}
