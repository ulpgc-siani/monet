package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.FederationUnitList;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import com.google.inject.Inject;

public class ActionLoadPartners implements ActionAccountSocket {
  private Logger         logger;
  private DataRepository dataRepository;

  @Inject
  public ActionLoadPartners(Logger logger, DataRepository dataRepository) {
    this.logger = logger;
    this.dataRepository = dataRepository;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {
    FederationUnitList partnerList = this.dataRepository.loadPartnerBusinessUnits();
    SocketResponseMessage rep = new SocketResponseMessage();

    try {
      rep.setResponse(partnerList.serialize());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return rep;
  }

}
