#-------------------------------------------------------------------------------
function add_federation_db {
  federation_db_name=$(get_federation_db_name $1)
  show_status " Update federation '$1' database ($federation_db_name)... "
  if [ "$INSTALL_MYSQL_SERVER" = "true" ]; then
    echo "create database if not exists $federation_db_name" | mysql -u $DB_FEDERATION_USERNAME
  fi

  cp "$DIR_APPS"/federation.war "/tmp"
  cd /tmp

  if [ "$DB_FEDERATION_TYPE" == "mysql" -o "$DB_FEDERATION_TYPE" == "" ]; then
    unzip federation.war WEB-INF/classes/accountoffice/database/mysql.clean.sql >> $FILELOG
    unzip federation.war WEB-INF/classes/accountoffice/database/mysql.sql >> $FILELOG

    IFS=':' read -a array <<< "$DB_FEDERATION_DOMAIN"
    host="${array[0]}"
    port="${array[1]}"
    password_text=""
    if [ "$DB_FEDERATION_PASSWORD" != "" ]; then
      password_text=" --password=$DB_FEDERATION_PASSWORD ";
    fi

    mysql --default-character-set=utf8 --host=$host --port=$port --database=$federation_db_name --user=$DB_FEDERATION_USERNAME $password_text <  WEB-INF/classes/accountoffice/database/mysql.clean.sql &>> $FILELOG
    mysql --default-character-set=utf8 --host=$host --port=$port --database=$federation_db_name --user=$DB_FEDERATION_USERNAME $password_text <  WEB-INF/classes/accountoffice/database/mysql.sql &>> $FILELOG
  fi

  if [ "$DB_FEDERATION_TYPE" == "oracle" ]; then
    unzip federation.war WEB-INF/classes/accountoffice/database/oracle.clean.sql >> $FILELOG
    unzip federation.war WEB-INF/classes/accountoffice/database/oracle.sql >> $FILELOG

    java -jar $ORACLE_CLIENT "$DB_FEDERATION_USERNAME/$DB_FEDERATION_PASSWORD@$DB_FEDERATION_DOMAIN" -f WEB-INF/classes/accountoffice/database/oracle.clean.sql &>> $FILELOG
    java -jar $ORACLE_CLIENT "$DB_FEDERATION_USERNAME/$DB_FEDERATION_PASSWORD@$DB_FEDERATION_DOMAIN" -f WEB-INF/classes/accountoffice/database/oracle.sql &>> $FILELOG
  fi

  cd $DIR_CURRENT

  show_status_ok
}

function delete_federation_db {
  federation_db_name=$(get_federation_db_name $1)
  if [ "$INSTALL_MYSQL_SERVER" = "true" ]; then
    echo "drop database $federation_db_name" | mysql -u $DB_SPACES_USERNAME &>> $FILELOG
  fi
}

function add_federation {
  check_applications
  show_info "Add federation '$1'..."

  if [ ! -d "$DIR_HOME_TOMCATPUBLIC" ]; then
    show_error "tomcat-public home directory not exists. Try install first."
    exit
  fi

  if [ -d "$DIR_HOME_TOMCATPUBLIC/.$1" ]; then
    show_error "federation home directory exists. Try delete first."
    exit
  fi

  add_federation_db $1

  # Create config directory
  mkdir "$DIR_HOME_TOMCATPUBLIC/.$1"
  mkdir "$DIR_HOME_TOMCATPUBLIC/.$1/images"
  mkdir "$DIR_HOME_TOMCATPUBLIC/.$1/logs"
  chmod g+w "$DIR_HOME_TOMCATPUBLIC/.$1/images"
  cp "$DIR_RESOURCES"/federation/federation.xml "$DIR_HOME_TOMCATPUBLIC"/".$1"
  cp "$DIR_RESOURCES"/federation/log4j.federation.config "$DIR_HOME_TOMCATPUBLIC"/".$1"
  cp "$DIR_RESOURCES"/federation/$MONET_VERSION/federation.config "$DIR_HOME_TOMCATPUBLIC"/".$1"

  sed -i "s/#federation#/$1/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/log4j.federation.config
  sed -i "s/#logginhub#/$LOGGINGHUB/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/log4j.federation.config
  sed -i "s/#federation#/$1/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  sed -i "s/#mail_admin_host#/$MAIL_ADMIN_HOST/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  sed -i "s/#mail_admin_port#/$MAIL_ADMIN_PORT/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  sed -i "s/#mail_admin_username#/$MAIL_ADMIN_USERNAME/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  sed -i "s/#mail_admin_password#/$MAIL_ADMIN_PASSWORD/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  sed -i "s/#mail_admin_from#/$MAIL_ADMIN_FROM/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  sed -i "s/#mail_admin_to#/$MAIL_ADMIN_TO/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  sed -i "s/#server_domain#/$SERVER_DOMAIN/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  sed -i "s/#federation_port#/$FEDERATION_PORT/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  if [ "$FEDERATION_PORT" = "443" ]; then
    sed -i "s/#secure#/true/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  else
    sed -i "s/#secure#/false/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config
  fi
  sed -i "s/#db_type#/$DB_FEDERATION_TYPE/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config

  sed -i "s/#label#/$2/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.xml

  if [ $FEDERATION_FULL = "true" ]; then
    sed -i "s/#auth_type#/<database-auth read-only=\"false\" \/>/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.xml
  else
    sed -i "s/#auth_type#/<mock-auth \/>/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.xml
  fi

  # Add certificates
  show_status " Generating certificates... "

  certificates_dir="$DIR_HOME_TOMCATPUBLIC"
  certificate_dir="$certificates_dir"/.$1
  if [ "$FEDERATIONS_DIR" != "" ]; then
    certificates_dir="$FEDERATIONS_DIR"
    certificate_dir="$certificates_dir"/$1
    mkdir "$certificate_dir"
  fi

  openssl genrsa -des3 -passout pass:12345678 -out "$certificate_dir"/ca-$1.key 4096 &>> $FILELOG
  if [ ! -f "$DIR_HOME_TOMCATPUBLIC"/.$1/ca-$1.key ]; then
    ln -s "$certificate_dir"/ca-$1.key "$DIR_HOME_TOMCATPUBLIC"/.$1/ca-$1.key
  fi

  openssl req -new -x509 -passin pass:12345678 -days 3650 -key "$certificate_dir"/ca-$1.key -out "$certificate_dir"/ca-$1.crt -subj "/C=ES/ST=Las Palmas de Gran Canaria/L=Tafira/O=$2 SL/OU=Autoridad\ certificadora/CN=federation|$1/emailAddress=info@monentia.es/" &>> $FILELOG

  if [ ! -f "$DIR_HOME_TOMCATPUBLIC"/.$1/ca-$1.crt ]; then
    ln -s "$certificate_dir"/ca-$1.crt "$DIR_HOME_TOMCATPUBLIC"/.$1/ca-$1.crt
  fi

  show_status_ok

  federations=`cat "$DIR_CONFIG_DEPLOYSERVICE/servers.xml" | grep -Po '<federation id="\K[^"]*'`
  federations_certificates=" -certfile $certificate_dir/ca-$1.crt "

  for federation in $federations
  do
    federation_certificates_dir=$DIR_HOME_TOMCATPUBLIC/.$federation
    if [ "$FEDERATIONS_DIR" != "" ]; then
      federation_certificates_dir=$certificates_dir/$federation
    fi

    federations_certificates="$federations_certificates -certfile $federation_certificates_dir/ca-$federation.crt "
  done

  openssl crl2pkcs7 -nocrl $federations_certificates -out "$certificate_dir"/$1.p7b
  if [ ! -f "$DIR_HOME_TOMCATPUBLIC"/.$1/$1.p7b ]; then
    ln -s "$certificate_dir"/$1.p7b "$DIR_HOME_TOMCATPUBLIC"/.$1/$1.p7b
  fi
  if [ ! -f "$DIR_HOME_TOMCATPUBLIC"/.$1/$1.p12 ]; then
    ln -s "$certificate_dir"/federation.p12 "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.p12
  fi
  for federation in $federations
  do
    federation_certificates_dir=$DIR_HOME_TOMCATPUBLIC/.$federation
    if [ "$FEDERATIONS_DIR" != "" ]; then
      federation_certificates_dir=$certificates_dir/$federation
    fi

    openssl pkcs7 -print_certs -in $federation_certificates_dir/$federation.p7b -out /tmp/pem.cer
    openssl crl2pkcs7 -nocrl -certfile "$certificate_dir"/ca-$1.crt -certfile /tmp/pem.cer -out $federation_certificates_dir/$federation.p7b
    rm -f /tmp/pem.cer
  done


  # Personal certificate
  openssl genrsa -des3 -passout pass:1234 -out "$certificate_dir"/federation.key 4096 &>> $FILELOG
  openssl req -new -passin pass:1234 -key "$certificate_dir"/federation.key -out "$certificate_dir"/federation.csr -subj "/C=ES/ST=Las Palmas de Gran Canaria/L=Tafira/O=Gisc/OU=Grupo de ingenieria del software y el conocimiento/CN=business_unit|federation-$1/emailAddress=info@openmonet.com/" &>> $FILELOG
  openssl x509 -req -passin pass:12345678 -days 36500 -CA "$certificate_dir"/ca-$1.crt -CAkey "$certificate_dir"/ca-$1.key -set_serial 01 -in "$certificate_dir"/federation.csr -out "$certificate_dir"/federation.crt &>> $FILELOG
  openssl pkcs12 -export -passin pass:1234 -passout pass:1234 -out "$certificate_dir"/federation.p12 -inkey "$certificate_dir"/federation.key -in "$certificate_dir"/federation.crt -certfile "$certificate_dir"/ca-$1.crt &>> $FILELOG

  # Federation port
  x=5345
  for federation in $federations
  do
    if [ -d "$DIR_HOME_TOMCATPUBLIC"/.$federation ]; then
      if [ "$MONET_VERSION" = "3.1" ]; then
        new_port=`cat "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config | grep -Po '"SocketPort">\K[^<]*'`
      else
        new_port=`cat "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config | grep -Po '"Socket.Port">\K[^<]*'`
      fi

      if [ "$new_port" != "#socket_port#" ]; then
        if [ $new_port -gt $x ]; then
          x=$new_port
        fi
      fi
    fi
  done
  x=$(($x + 1))
  sed -i "s/#socket_port#/$x/g" "$DIR_HOME_TOMCATPUBLIC"/.$1/federation.config

  chown tomcat-public.monet "$DIR_HOME_TOMCATPUBLIC"/."$1"
  chown tomcat-public.monet "$DIR_HOME_TOMCATPUBLIC"/."$1"/* -R

  # Deploy federation in tomcat
  cp "$DIR_APPS"/federation.war "/tmp"
  cd /tmp

  unzip federation.war WEB-INF/web.dist.xml >> $FILELOG
  sed -i "s/#federation#/$1/g" WEB-INF/web.dist.xml
  mv WEB-INF/web.dist.xml WEB-INF/web.xml
  zip federation.war WEB-INF/web.xml >> $FILELOG
  rm -rf WEB-INF

  unzip federation.war META-INF/context.dist.xml >> $FILELOG

  federation_db_name=$(get_federation_db_name $1)
  local url="jdbc:mysql:\/\/$DB_FEDERATION_DOMAIN\/$federation_db_name?autoReconnect=true"
  local driver="com.mysql.jdbc.Driver"
  local validation_query="SELECT 1"
  if [ "$DB_FEDERATION_TYPE" == "oracle" ]; then
    local url="jdbc:oracle:thin:$DB_FEDERATION_USERNAME\/$DB_FEDERATION_PASSWORD\@$DB_FEDERATION_DOMAIN"
    local driver="oracle.jdbc.OracleDriver"
    local validation_query="select 1 from dual"
  fi

  sed -i "s/#resource#/<Resource auth=\"Container\" driverClassName=\"$driver\" global=\"jdbc\/FederationDatabase-$1\" maxActive=\"15\" maxIdle=\"2\" maxWait=\"30000\" name=\"jdbc\/FederationDatabase-$1\" password=\"$DB_FEDERATION_PASSWORD\" type=\"javax.sql.DataSource\" url=\"$url\" username=\"$DB_FEDERATION_USERNAME\" removeAbandoned=\"true\" removeAbandonedTimeout=\"5\" logAbandoned=\"true\" accessToUnderlyingConnectionAllowed=\"true\" validationQuery=\"$validation_query\" validationQueryTimeout=\"10\" validationInterval=\"10000\" testOnBorrow=\"true\" \/>/g" META-INF/context.dist.xml
  mv META-INF/context.dist.xml META-INF/context.xml
  zip federation.war META-INF/context.xml >> $FILELOG
  rm -rf META-INF

  mv federation.war "$DIR_APP_TOMCATPUBLIC"/webapps/$1.war

  if [ "$new_port" == "#socket_port#" ]; then
    return 1
  fi

  # Config deployservice
  if [ "$FEDERATION_DEFAULT_NAME" = "$1" -a "$FEDERATION_LOCAL" != "" ]; then
    federation_local_escaped=$(sed 's/\//\\\//g' <<< "$FEDERATION_LOCAL")
    sed -i "s/<!--federation-->/$federation_local_escaped\n      <!--federation-->/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"
  else
    file_key="$(echo "$DIR_HOME_TOMCATPUBLIC/.$1/ca-$1.key" | sed -e 's/\//\\\//g')"
    file_certificate="$(echo "$DIR_HOME_TOMCATPUBLIC/.$1/ca-$1.crt" | sed -e 's/\//\\\//g')"
    sed -i "s/<!--federation-->/<federation id=\"$1\" password-key=\"1234\" password-ca=\"12345678\" key=\"$file_key\" certificate=\"$file_certificate\" \/>\n      <!--federation-->/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"
  fi

  # Restart services
  restart_deployservice
}

function delete_federation {
  show_status " Delete federation '$1' "

  delete_federation_db $1

  sed -i "/<federation id=\"$1\"/d" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"

  certificates_dir="$DIR_HOME_TOMCATPUBLIC"
  certificate_dir="$certificates_dir"/.$1
  if [ "$FEDERATIONS_DIR" != "" ]; then
    certificates_dir="$FEDERATIONS_DIR"
    certificate_dir="$certificates_dir"/$1
  fi

  if [ -d $certificate_dir ]; then
    federations=`cat "$DIR_CONFIG_DEPLOYSERVICE/servers.xml" | grep -Po '<federation id="\K[^"]*'`
    certificate=`cat "$certificate_dir"/ca-$1.crt`
    for federation in $federations
    do
      federation_certificates_dir=$DIR_HOME_TOMCATPUBLIC/.$federation
      if [ "$FEDERATIONS_DIR" != "" ]; then
        federation_certificates_dir=$certificates_dir/$federation
      fi

      openssl pkcs7 -print_certs -in $federation_certificates_dir/$federation.p7b -out /tmp/pem.cer
      diff /tmp/pem.cer "$certificate_dir"/ca-$1.crt > /tmp/pem.diff.cer
      sed -i 's/< //g' /tmp/pem.diff.cer
      openssl crl2pkcs7 -nocrl -certfile /tmp/pem.diff.cer -out $federation_certificates_dir/$federation.p7b
      rm -f /tmp/pem.cer
      rm -f /tmp/pem.diff.cer
    done

    if [ -d "$DIR_HOME_TOMCATPUBLIC"/.$1 ]; then
      rm -rf "$certificate_dir"
    fi

    rm -f "$DIR_APP_TOMCATPUBLIC"/webapps/$1.war
    rm -rf "$DIR_HOME_TOMCATPUBLIC"/."$1"

  fi

  restart_deployservice

  show_status_ok
}

function add_federations_remote {
  show_info "Add federations remote..."

  if [ "$FEDERATIONS_REMOTE" != "" ]; then
    IFS='<' read -ra federations <<< "$FEDERATIONS_REMOTE"
    for federation in "${federations[@]}"; do
     federation=$(sed 's/\//\\\//g' <<< "$federation")
     if [ "$federation" != "" ]; then
       sed -i "s/<!--federation-->/<$federation\n      <!--federation-->/g" "$DIR_CONFIG_DEPLOYSERVICE/servers.xml"
     fi
    done
  fi
}

function delete_all_federations {
  if [ -f "$DIR_CONFIG_DEPLOYSERVICE/servers.xml" ]; then
    federations=`cat "$DIR_CONFIG_DEPLOYSERVICE/servers.xml" | grep -Po '<federation id="\K[^"]*'`
    for federation in $federations
    do
      delete_federation $federation
    done
  fi
}
