package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import com.google.inject.Inject;

public class ActionGetAccount implements ActionAccountSocket {

  private Logger           logger;
  private SessionComponent sessionComponent;
  private AccountLayer     accountLayer;

  @Inject
  public ActionGetAccount(Logger logger, SessionComponent sessionComponent, AccountLayer accountLayer) {
    this.logger = logger;
    this.sessionComponent = sessionComponent;
    this.accountLayer = accountLayer;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {

    SocketResponseMessage response = new SocketResponseMessage();

    String username = this.sessionComponent.getUsername(socketMessage.getAccesToken());
    User user = null;
    if(username != null)
      user = this.accountLayer.loadFromUsername(username);

    String userString = null;
    try {
      userString = (user != null) ? user.serialize() : null;
    } catch (Exception e) {
      this.logger.debug(e.getMessage(), e);
    }
    if(userString == null) {
      userString = SocketResponseMessage.RESPONSE_USER_NOT_FOUND;
      response.setIsError(true);
    }

    response.setResponse(userString);

    return response;
  }

}
