package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.UserList;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import com.google.inject.Inject;

public class ActionSearchUsers implements ActionAccountSocket {
  private Logger         logger;
  private DataRepository dataRepository;

  @Inject
  public ActionSearchUsers(Logger logger, DataRepository dataRepository) {
    this.logger = logger;
    this.dataRepository = dataRepository;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {
    String condition = socketMessage.getCondition();
    int startPos = socketMessage.getStartPos();
    int limit = socketMessage.getLimit();

    UserList userList = this.dataRepository.searchUsers(condition, startPos, limit);
    SocketResponseMessage rep = new SocketResponseMessage();
    try {
      rep.setResponse(userList.serialize());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return rep;
  }

}
