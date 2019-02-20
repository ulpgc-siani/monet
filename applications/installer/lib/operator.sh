function install_group_monet {
  groupadd monet
}

function uninstall_group_monet {
  groupdel monet &>> $FILELOG
}

function install_operator_user {
  show_info " Install user operator ..."
  adduser monet-operator -g monet &>> $FILELOG
  chown monet-operator.monet /home/monet-operator
  mkdir /home/monet-operator/.ssh
  cp "$DIR_RESOURCES"/monet-operator/authorized_keys /home/monet-operator/.ssh

  operators_ssh_keys=`echo $OPERATORS_SSH_KEYS | sed -e 's/ /#/g'`  
  keys=`echo $operators_ssh_keys | sed -e 's/;/\n/g'`
  
  for key in $keys; do
    key=`echo $key | sed -e 's/#/ /g'`
    echo $key >> /home/monet-operator/.ssh/authorized_keys
  done
  
  restorecon -R /home/monet-operator/.ssh
  cp "$DIR_RESOURCES"/monet-operator/service-tomcat-* /home/monet-operator
  chmod a+x /home/monet-operator/service-tomcat-*.sh
  cp "$DIR_RESOURCES"/monet-operator/remove-logs.sh /opt
  chmod a+x /opt/remove-logs.sh  
  ln -s /opt/tomcat-public/logs /home/monet-operator/tomcat-public-logs
  ln -s /opt/tomcat-local/logs /home/monet-operator/tomcat-local-logs
  mkdir /home/monet-operator/deployservice-logs &>> $FILELOG
  chown monet-operator.monet /home/monet-operator/* -R
  if [ ! -d /etc/sudoers.d ]; then
    mkdir /etc/sudoers.d
    echo "## Read drop-in files from /etc/sudoers.d (the # here does not mean a comment)" >> /etc/sudoers
    echo "#includedir /etc/sudoers.d" >> /etc/sudoers.d
  fi
  cp "$DIR_RESOURCES"/monet-operator/monet-operator-permissions /etc/sudoers.d/
  chmod u-w /etc/sudoers.d/monet-operator-permissions
  chmod o-r /etc/sudoers.d/monet-operator-permissions
  sed -i "s/Defaults    requiretty/#Defaults    requiretty/g" /etc/sudoers
}

function uninstall_operator_user {
  show_status "  Delete user operator... "
  rm -f -r /home/monet-operator
  userdel -f -r monet-operator &>> $FILELOG
  rm -f /etc/sudoers.d/monet-operator-permissions
  sed -i "s/#Defaults    requiretty/Defaults    requiretty/g" /etc/sudoers
  rm -f /opt/remove-logs.sh
  show_status_ok
}

