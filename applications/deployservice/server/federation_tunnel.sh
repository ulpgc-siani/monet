#!/bin/bash

FEDERATIONPORT=`cat /etc/monet/federation_port`

iptables -F
iptables -X
iptables -t nat -F
iptables -t nat -X
iptables -t mangle -F
iptables -t mangle -X
iptables -P INPUT ACCEPT
iptables -P FORWARD ACCEPT
iptables -P OUTPUT ACCEPT

iptables -t nat -I PREROUTING -p tcp --dport $FEDERATIONPORT -j REDIRECT --to-ports 8080
iptables -t nat -I OUTPUT -p tcp -d 127.0.0.1 --dport $FEDERATIONPORT -j REDIRECT --to-ports 8080