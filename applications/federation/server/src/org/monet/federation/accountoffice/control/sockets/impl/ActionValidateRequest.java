package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountoffice.core.components.certificatecomponent.CertificateComponent;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import com.google.inject.Inject;

public class ActionValidateRequest implements ActionAccountSocket {

  private static final String  ERROR_INVALID_USER = "error_invalid_user";

  private Logger               logger;
  private CertificateComponent verificationComponent;
  private DataRepository dataRepository;

  @Inject
  public ActionValidateRequest(Logger logger, CertificateComponent verificationComponent, DataRepository dataRepository) {
    this.logger = logger;
    this.verificationComponent = verificationComponent;
    this.dataRepository = dataRepository;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {
    logger.debug("ValidateRequestAction.execute()");
    
    String signature = socketMessage.getVerifierSign().getSignature().getText();
    String hash = socketMessage.getVerifierSign().getHash().getText();
    String respMessage = null;

    try {
      if (this.verificationComponent.isValidCertificate(hash, signature, false)) {
        User user = this.verificationComponent.getUserFromSignature(hash, signature, false);
        user.setId(this.dataRepository.generateIdForUser(user));
        respMessage = user.serialize();
      }
      else
        respMessage = ERROR_INVALID_USER;
    } catch (Exception e) {
      respMessage = e.getMessage();
      logger.debug(e.getMessage(), e);
    }

    SocketResponseMessage response = new SocketResponseMessage();
    response.setResponse(respMessage);
    
    return response;
  }

}
