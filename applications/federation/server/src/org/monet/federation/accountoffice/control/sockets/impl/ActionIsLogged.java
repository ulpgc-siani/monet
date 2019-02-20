package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import com.google.inject.Inject;

public class ActionIsLogged implements ActionAccountSocket {

  private SessionComponent sessionComponent;

  @Inject
  public ActionIsLogged(SessionComponent sessionComponent) {
    this.sessionComponent = sessionComponent;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {
    SocketResponseMessage response = new SocketResponseMessage();

    String status = null;
    if(sessionComponent.isUserOnline(socketMessage.getAccesToken())) {
      status = SocketResponseMessage.RESPONSE_OK;
    } else {
      status = SocketResponseMessage.RESPONSE_FALSE;
    }
    response.setResponse(status);

    return response;
  }
}
