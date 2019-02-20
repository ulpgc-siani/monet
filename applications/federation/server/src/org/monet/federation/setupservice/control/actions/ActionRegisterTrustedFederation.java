package org.monet.federation.setupservice.control.actions;

import org.monet.federation.accountoffice.core.model.Federation;
import org.monet.federation.accountoffice.utils.PersisterHelper;
import org.monet.federation.setupservice.control.constants.Parameter;
import org.monet.federation.setupservice.core.constants.MessageCode;

public class ActionRegisterTrustedFederation extends ActionSetupService {

  @Override
  public String execute() throws Exception{
    String trustedFederation = (String)this.parameters.get(Parameter.FEDERATION);
    Federation federation = PersisterHelper.load(decode(trustedFederation), Federation.class);
    
    federation.setTrusted(true);
    if (this.dataRepository.existsFederation(federation.getName())) {
      federation = this.dataRepository.loadFederation(federation.getName());
      this.dataRepository.saveFederation(federation);
    }
    else
      federation = this.dataRepository.createFederation(federation);
      
    return MessageCode.FEDERATION_OPERATION_OK;
  }

}
