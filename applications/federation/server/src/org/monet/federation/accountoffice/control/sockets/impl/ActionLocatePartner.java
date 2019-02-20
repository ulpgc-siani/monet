package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.FederationUnit;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import com.google.inject.Inject;

public class ActionLocatePartner implements ActionAccountSocket {
  private Logger         logger;
  private DataRepository dataRepository;

  @Inject
  public ActionLocatePartner(Logger logger, DataRepository dataRepository) {
    this.logger = logger;
    this.dataRepository = dataRepository;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {
    String partnerName = socketMessage.getPartnerName();
    FederationUnit partner = this.dataRepository.locatePartnerBusinessUnit(partnerName);
    
    SocketResponseMessage rep = new SocketResponseMessage();

    try {
      rep.setResponse(partner.serialize());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return rep;
  }

}
