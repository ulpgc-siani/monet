#!/bin/bash

if [ "$(id -u)" != "0" ]; then
  printf "Error: Sorry, you are not root (sudo?).\n"
  exit 1
fi

if [ "$1" = "deployservice" ]; then
  rm -f /root/.deployservice/logs/*
  rm -f /home/monet-operator/deployservice-logs/*
  touch /root/.deployservice/logs/deployservice.log
  ln /root/.deployservice/logs/deployservice.log /home/monet-operator/deployservice-logs/deployservice.log
else
  if [ "$1" = "tomcat-public" ]; then
    rm -f /opt/tomcat-public/logs/*
  else
    if [ "$1" = "tomcat-local" ]; then
      rm -f /opt/tomcat-local/logs/*
    else
      rm -f /root/.deployservice/logs/*
      rm -f /home/monet-operator/deployservice-logs/*
      touch /root/.deployservice/logs/deployservice.log
      ln /root/.deployservice/logs/deployservice.log /home/monet-operator/deployservice-logs/deployservice.log

      rm -f /opt/tomcat-public/logs/*
      rm -f /opt/tomcat-local/logs/*
    fi
  fi
fi
