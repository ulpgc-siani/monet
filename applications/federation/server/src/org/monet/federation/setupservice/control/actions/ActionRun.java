package org.monet.federation.setupservice.control.actions;

import java.util.Date;

import org.monet.federation.setupservice.core.constants.MessageCode;

public class ActionRun extends ActionSetupService {

  @Override
  public String execute() {
     this.federationStatus.setRunningDate(new Date());
     return MessageCode.FEDERATION_OPERATION_OK;
  }

}
