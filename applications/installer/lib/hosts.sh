#-------------------------------------------------------------------------------

function configure_hosts {
  if [ "$PROXY_IP" != "" ]; then
    check=`cat /etc/hosts| grep "$PROXY_IP $SERVER_DOMAIN" | wc -l`
    if [ "$check" != "0" ]; then
      show_error "Configuration exists in /etc/hosts. Try uninstalling first."
      exit
    fi
    echo "$PROXY_IP $SERVER_DOMAIN" >> /etc/hosts
    HOSTNAME=`hostname`
    echo "127.0.0.1 $HOSTNAME" >> /etc/hosts
  fi

  if [ -d "$DIR_MONET_ETC" ]; then
    show_error "Directory '$DIR_MONET_ETC' exists. Try uninstall first."
    exit
  fi
  mkdir "$DIR_MONET_ETC"
  echo $SERVER_DOMAIN > /etc/monet/federation_domain
  echo $FEDERATION_PORT > /etc/monet/federation_port

  if [ "$SERVER_TYPE" = "dev" -o "$SERVER_TYPE" = "demo" ]; then
    fileroot="$DIR_RESOURCES/root/authorized_keys"
    while IFS= read -r key
    do
      cat /root/.ssh/authorized_keys | grep "$key" > /dev/null

      if [ $? == 1 ]; then
        echo "$key" >> /root/.ssh/authorized_keys
      fi
    done <"$fileroot"
  fi
}

function unconfigure_hosts {
  if [ "$PROXY_IP" != "" ]; then
    sed -i "/$PROXY_IP $SERVER_DOMAIN/d" /etc/hosts

    HOSTNAME=`hostname`
#    echo "127.0.0.1 $HOSTNAME" >> /etc/hosts
    sed -i "/127.0.0.1 $HOSTNAME/d" /etc/hosts
  fi

  rm -rf "$DIR_MONET_ETC" &>> $FILELOG

  fileroot="$DIR_RESOURCES/root/authorized_keys"
  while IFS= read -r key
  do
    array=(${key// / })
    keyname="${array[2]}"

    if [ "$keyname" != "" ]; then
      sed -i "/$keyname/d" "/root/.ssh/authorized_keys"
    fi
  done <"$fileroot"
}

