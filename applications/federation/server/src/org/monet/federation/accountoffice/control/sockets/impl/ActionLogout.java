package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import com.google.inject.Inject;

public class ActionLogout implements ActionAccountSocket {

  private SessionComponent sessionComponent;
  
  @Inject
  public ActionLogout(SessionComponent sessionComponent) {
    this.sessionComponent = sessionComponent;
  }
  
  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {

    SocketResponseMessage response = new SocketResponseMessage();
    
    String status = null;
    if(this.sessionComponent.deleteUserFromToken(socketMessage.getAccesToken())) {
      status = SocketResponseMessage.RESPONSE_OK;
    } else {
      status = SocketResponseMessage.RESPONSE_USER_NOT_FOUND;
      response.setIsError(true);
    }
    response.setResponse(status);

    return response; 
  }
}
