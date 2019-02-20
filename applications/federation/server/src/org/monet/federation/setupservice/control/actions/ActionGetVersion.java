package org.monet.federation.setupservice.control.actions;

import org.monet.federation.accountoffice.core.configuration.Configuration;

public class ActionGetVersion extends ActionSetupService {

  @Override
  public String execute() {
    return this.configuration.getProperty(Configuration.VERSION);
  }

}
