package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import com.google.inject.Inject;

public class ActionExistsAccount implements ActionAccountSocket {
  private SessionComponent sessionComponent;

  @Inject
  public ActionExistsAccount(Logger logger, SessionComponent sessionComponent, AccountLayer accountLayer) {
    this.sessionComponent = sessionComponent;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {

    SocketResponseMessage response = new SocketResponseMessage();

    String username = this.sessionComponent.getUsername(socketMessage.getAccesToken());
    if(username == null)
      response.setResponse(SocketResponseMessage.RESPONSE_FALSE);
    else
      response.setResponse(SocketResponseMessage.RESPONSE_OK);

    return response;
  }

}
