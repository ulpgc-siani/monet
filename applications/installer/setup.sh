#!/bin/bash

APP_NAME="Monet installer"
APP_VERSION="1.0.83"
FILE_CURRENT=`readlink -e $0`
DIR_CURRENT=`dirname $FILE_CURRENT`
DIR_APPS="$DIR_CURRENT/apps"
DIR_RESOURCES="$DIR_CURRENT/resources"
FILECONFIG="$DIR_CURRENT/config.cfg"
FILERESULT="$DIR_CURRENT/result.txt"
FILELOG="$DIR_CURRENT/install.log"
GENERATE_PASSWORD=`< /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c8`
PROGRESS=0
PROGRESS_ENABLED=true
DIALOG_ENABLED=true
DIALOG_RETURN=""
OS_VERSION=`rpm -qa \*-release | grep -Ei "oracle|redhat|centos" | cut -d"-" -f3`
CURRENT_OP=""
ORACLE_CLIENT="$DIR_CURRENT/utils/oclient.jar"
firewall-cmd --state &> /dev/null
FIREWALL=`echo $?`

DIR_APP_DEPLOYSERVICE="/opt/deployservice"
DIR_CONFIG_DEPLOYSERVICE="/root/.deployservice"
DIR_APP_TOMCATPUBLIC="/opt/tomcat-public"
DIR_APP_TOMCATLOCAL="/opt/tomcat-local"
DIR_APP_HUB="/opt/hub"
DIR_HOME_TOMCATPUBLIC="/home/tomcat-public"
DIR_MONET_ETC="/etc/monet"

source ./lib/display.sh
source ./lib/install_apps.sh
source ./lib/configuration.sh
source ./lib/deployservice.sh
source ./lib/tomcat-public.sh
source ./lib/tomcat-local.sh
source ./lib/hosts.sh
source ./lib/federation.sh
source ./lib/hub.sh
source ./lib/operator.sh
source ./lib/upgrade-federation.sh
source ./lib/upgrade-tomcat.sh

#-------------------------------------------------------------------------------
function install {
  rm -f $FILERESULT
  show_welcome $MONET_VERSION
  show_dialog_install
  check_configuration
  check_applications
  PROGRESS=5
  show_info "Starting install... "

  # Enable firewall
  show_status " Configure firewall... "
#  if [ "$OS_VERSION" = "6" ]; then
#    execute "lokkit --default=server"
#    execute "lokkit -p 8080:tcp"
#  fi
  if [ "$OS_VERSION" = "7" -a "$FIREWALL" = "0" ]; then
    execute "firewall-cmd --permanent --add-port=8080/tcp"
    execute "firewall-cmd --permanent --add-port=4323/tcp"
    execute "firewall-cmd --reload"
  fi
  show_status_ok

  configure_hosts; PROGRESS=10

  # Java
  install_java; PROGRESS=20
  configure_java

  # Mysql
  install_mysql; PROGRESS=30

  # Monet Monitor
  install_mmonitor; PROGRESS=35

  PROGRESS=40
#  install_hub; PROGRESS=45
  install_deployservice; PROGRESS=50
  install_group_monet; PROGRESS=55
  install_operator_user; PROGRESS=60
  install_tomcat-public; PROGRESS=70
  install_tomcat-local; PROGRESS=90
  add_federations_remote;
  if [ "$FEDERATION_DEFAULT_NAME" != "" ]; then
    add_federation $FEDERATION_DEFAULT_NAME "$FEDERATION_DEFAULT_LABEL";
  fi
  PROGRESS=100
  show_info "Installation completed successfully."
  echo ""
  echo "Please, review it $FILERESULT file for more information."
}

function uninstall {
  show_dialog_uninstall
  show_info "Starting uninstall... "

#  if [ "$OS_VERSION" = "6" ]; then
#    execute "lokkit --default=server"
#  fi

  if [ "$OS_VERSION" = "7" -a "$FIREWALL" = "0" ]; then
    execute "firewall-cmd --permanent --remove-port=8080/tcp"
    execute "firewall-cmd --permanent --remove-port=4323/tcp"
    execute "firewall-cmd --reload"
  fi
  PROGRESS=5

  unconfigure_hosts; PROGRESS=10
  unconfigure_java; PROGRESS=20
  delete_all_federations; PROGRESS=30
  uninstall_mmonitor; PROGRESS=40
  uninstall_deployservice; PROGRESS=50
  uninstall_tomcat-public; PROGRESS=80
  uninstall_tomcat-local; PROGRESS=90
  uninstall_operator_user; PROGRESS=93
  uninstall_group_monet; PROGRESS=96
  uninstall_hub; PROGRESS=100;
  show_info "Uninstallation completed successfully."
}
#-------------------------------------------------------------------------------

echo "---------------------------------"
echo "$APP_NAME v$APP_VERSION"
echo "---------------------------------"
echo ""

if [ "$1" != "" ]; then
  install_needed_apps
fi

check_requisites $1 $2 $3
load_configuration $1

if [ "$1" != "install-only-database" ]; then
  install_dialog
fi
rm -f $FILELOG

if [ "$1" = "install" ]; then
  install
else
  if [ "$1" = "uninstall" ]; then
    uninstall
    echo ""
  else
    if [ "$1" = "clean-install" ]; then
      uninstall
      install
    else
      if [ "$1" = "update" ]; then
        echo "Update applications..."
        echo ""
        rm -rf $DIR_APPS/*
        check_applications
      else
        if [ "$1" = "add-federation" ]; then
          federation_name=$2
          federation_label=$3
          if [ "$federation_name" = "" -o "$federation_label" = "" ]; then
            show_dialog_federation $2 $3
            echo ""

            array=(${DIALOG_RETURN//;/ })
            federation_name="${array[0]}"
            federation_label="${array[@]:1}"
          fi

          if [ "$federation_name" = "" ]; then
            show_error "Federation must be a name"
            exit
          fi

          if [ "$federation_label" = "" ]; then
            show_error "Federation must be a label"
            exit
          fi

          add_federation $federation_name "$federation_label"
          echo ""
        else
          if [ "$1" = "delete-federation" ]; then
            delete_federation $2
            echo ""
          else
            if [ "$1" = "install-only-database" ]; then
              INSTALL_MYSQL_SERVER=true
              DIALOG_ENABLED=false
              install_mysql
            else
              if [ "$1" = "install-nfs-server" ]; then
                DIALOG_ENABLED=false
                install_nfs_server
              else
                if [ "$1" = "install-nfs-client" ]; then
                  DIALOG_ENABLED=false
                  install_nfs_client
                else
                  if [ "$1" = "upgrade-federation" ]; then
                    DIALOG_ENABLED=false
                    upgrade_federations
                  else
                    if [ "$1" = "upgrade-tomcat" ]; then
                      DIALOG_ENABLED=false
                      upgrade_tomcat
                    fi
                  fi
                fi
              fi
            fi
          fi
        fi
      fi
    fi
  fi
fi
