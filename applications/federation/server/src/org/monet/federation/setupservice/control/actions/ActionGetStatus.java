package org.monet.federation.setupservice.control.actions;

import org.monet.federation.setupservice.core.model.Status;

public class ActionGetStatus extends ActionSetupService {

  @Override
  public String execute() throws Exception {
    Status status = new Status();
    status.setRunningDate(federationStatus.getRunningDate());
    return status.serializeToXML();
  }

}
