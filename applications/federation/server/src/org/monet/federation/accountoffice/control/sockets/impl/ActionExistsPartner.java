package org.monet.federation.accountoffice.control.sockets.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

public class ActionExistsPartner implements ActionAccountSocket {
  private Logger         logger;
  private DataRepository dataRepository;

  @Inject
  public ActionExistsPartner(Logger logger, DataRepository dataRepository) {
    this.logger = logger;
    this.dataRepository = dataRepository;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {
    String partnerId = socketMessage.getPartnerId();
    Boolean result = this.dataRepository.existsPartnerBusinessUnit(partnerId);
    SocketResponseMessage response = new SocketResponseMessage();

    try {
      response.setResponse(result ? SocketResponseMessage.RESPONSE_OK : SocketResponseMessage.RESPONSE_FALSE);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return response;
  }

}
