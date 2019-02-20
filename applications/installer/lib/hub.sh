#-------------------------------------------------------------------------------
function install_hub {
  if [ "$SERVER_TYPE" = "dev" ]; then
    show_info " Add hub ..."

    if [ -d "$DIR_APP_HUB" ]; then
      show_error "hub directory exists. Try uninstall first."
      exit
    fi

    show_status "  Copy... "
    mkdir "$DIR_APP_HUB"
    mkdir "$DIR_APP_HUB"/var
    cp -a "$DIR_RESOURCES"/hub/* "$DIR_APP_HUB"
    show_status_ok

    show_status "  Configure... "
    ln -s /opt/hub/bin/hub.sh /etc/init.d/hub
    chkconfig --add hub
    show_status_ok

    show_status "  Start service... "
    /etc/init.d/hub start &>> $FILELOG
    show_status_ok
  fi
}

function uninstall_hub {
  show_status " Delete hub"

  if [ -f /etc/init.d/hub ]; then
    /etc/init.d/hub stop &>> $FILELOG
    chkconfig --del hub &>> $FILELOG
    rm -f /etc/init.d/hub
  fi
  rm -rf "$DIR_APP_HUB"

  show_status_ok
}

