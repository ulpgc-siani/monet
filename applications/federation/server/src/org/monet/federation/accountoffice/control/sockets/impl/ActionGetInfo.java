package org.monet.federation.accountoffice.control.sockets.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationInfo;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

public class ActionGetInfo implements ActionAccountSocket {

  private Logger        logger;
  private Configuration configuration;

  @Inject
  public ActionGetInfo(Logger logger, Configuration configuration) {
    this.logger = logger;
    this.configuration = configuration;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {

    FederationInfo info = new FederationInfo();
    info.setName(configuration.getName());
    info.setLabel(configuration.getLabel());
    info.setLogoPath(configuration.getLogoPath());

    SocketResponseMessage rep = new SocketResponseMessage();
    try {
      rep.setResponse(info.serializeToXML());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return rep;
  }

}
