package org.monet.monitor.utils;

import org.monet.monitor.configuration.Configuration;

public class Message {
  private final org.siani.jmessages.utils.messages.Message message;

  public Message() {
    message = new org.siani.jmessages.utils.messages.Message(null, null, null, Configuration.getRocketChatUrl());
  }
  public void send(String value) throws Exception {
    message.enableProxySocks(Configuration.getSocksHost(), Configuration.getSocksPort());
    message.send(value);
    message.disableProxySocks();
  }
}
