#-------------------------------------------------------------------------------
function upgrade_federations {

if [ -f "$DIR_CONFIG_DEPLOYSERVICE/servers.xml" ]; then
    federations=`cat "$DIR_CONFIG_DEPLOYSERVICE/servers.xml" | grep -Po '<federation id="\K[^"]*'`
    for federation in $federations
    do
      if [ ! -f "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.p12 ]; then
        show_status " Update federation '$federation' ... "
        
        openssl genrsa -des3 -passout pass:1234 -out "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.key 4096 &>> $FILELOG
        openssl req -new -passin pass:1234 -key "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.key -out "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.csr -subj "/C=ES/ST=Las Palmas de Gran Canaria/L=Tafira/O=Gisc/OU=Grupo de ingenieria del software y el conocimiento/CN=business_unit|federation-$federation/emailAddress=info@openmonet.com/" &>> $FILELOG
        openssl x509 -req -passin pass:12345678 -days 36500 -CA "$DIR_HOME_TOMCATPUBLIC"/.$federation/ca-$federation.crt -CAkey "$DIR_HOME_TOMCATPUBLIC"/.$federation/ca-$federation.key -set_serial 01 -in "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.csr -out "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.crt &>> $FILELOG
        openssl pkcs12 -export -passin pass:1234 -passout pass:1234 -out "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.p12 -inkey "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.key -in "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.crt -certfile "$DIR_HOME_TOMCATPUBLIC"/.$federation/ca-$federation.crt &>> $FILELOG
        

        cat "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config | grep "Certificate.File" > /dev/null
        if [ $? == 1 ]; then
          sed -i ':a;N;$!ba;s/<\/properties>/\t<entry key="Certificate.File">federation.p12<\/entry>\n<\/properties>/g' "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config
        fi

        cat "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config | grep "Certificate.Password" > /dev/null
        if [ $? == 1 ]; then
          sed -i ':a;N;$!ba;s/<\/properties>/\t<entry key="Certificate.Password">1234<\/entry>\n<\/properties>/g' "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config
        fi

        cat "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config | grep "Socket.AllowedAddresses" > /dev/null
        if [ $? == 1 ]; then
          sed -i ':a;N;$!ba;s/<\/properties>/\t<entry key="Socket.AllowedAddresses">127.0.0.1<\/entry> <!-- Ip addresses separated by comma -->\n<\/properties>/g' "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config
        fi

        cat "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config | grep "Mobile.PushApi.Key" > /dev/null
        if [ $? == 1 ]; then
          sed -i ':a;N;$!ba;s/<\/properties>/\t<entry key="Mobile.PushApi.Key">AIzaSyBG6uhpiu7wxOzDH5qH08ZXe9WoMYWVwoE<\/entry>\n<\/properties>/g' "$DIR_HOME_TOMCATPUBLIC"/.$federation/federation.config
        fi       
        
        show_status_ok
      else
        show_info "Federation upgraded yet."
      fi
    done
  fi
}
