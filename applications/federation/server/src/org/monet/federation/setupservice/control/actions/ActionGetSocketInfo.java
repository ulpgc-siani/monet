package org.monet.federation.setupservice.control.actions;

import org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationSocketInfo;

public class ActionGetSocketInfo extends ActionSetupService {

  @Override
  public String execute() throws Exception {
    FederationSocketInfo info = new FederationSocketInfo();
    info.setHost(this.configuration.getSocketHost());
    info.setPort(this.configuration.getSocketPort());
    return info.serializeToXML();
  }

}
