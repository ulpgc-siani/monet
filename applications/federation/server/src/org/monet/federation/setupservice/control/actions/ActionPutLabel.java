package org.monet.federation.setupservice.control.actions;

import org.monet.federation.setupservice.control.constants.Parameter;
import org.monet.federation.setupservice.core.constants.MessageCode;


public class ActionPutLabel extends ActionSetupService {


  @Override
  public String execute() throws Exception{
    String label = (String)this.parameters.get(Parameter.LABEL);
    this.configuration.setLabel(decode(label));
    return MessageCode.FEDERATION_OPERATION_OK;
  }

}
