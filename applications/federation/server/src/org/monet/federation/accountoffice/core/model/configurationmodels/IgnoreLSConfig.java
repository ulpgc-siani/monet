package org.monet.federation.accountoffice.core.model.configurationmodels;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="login-service", strict=false)
public class IgnoreLSConfig {

  @Element (name="setup")
  SetupModel setup;

  public IgnoreLSConfig(){}

  public SetupModel getSetup() {
    return setup;
  }

  public void setSetup(SetupModel setup) {
    this.setup = setup;
  }

}
