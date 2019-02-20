package org.monet.federation.setupservice.control.actions;

import org.monet.federation.setupservice.core.constants.MessageCode;

public class ActionStop extends ActionSetupService {

  @Override
  public String execute() {
    federationStatus.setRunningDate(null);
    return MessageCode.FEDERATION_STOPPED;
  }

}
