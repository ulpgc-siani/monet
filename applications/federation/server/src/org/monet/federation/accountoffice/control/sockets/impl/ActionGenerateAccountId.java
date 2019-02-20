package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import com.google.inject.Inject;

public class ActionGenerateAccountId implements ActionAccountSocket {
  private DataRepository dataRepository;

  @Inject
  public ActionGenerateAccountId(DataRepository dataRepository) {
    this.dataRepository = dataRepository;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {

    SocketResponseMessage response = new SocketResponseMessage();
    String username = socketMessage.getUsername();
    String fullname = socketMessage.getFullname();
    String email = socketMessage.getEmail();

    User user = new User();
    user.setUsername(username);
    user.setFullname(fullname);
    user.setEmail(email);
    user.setLang("es");
    
    String id = this.dataRepository.generateIdForUser(user);
    response.setResponse(id);

    return response;
  }

}
