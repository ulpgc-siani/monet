package org.monet.federation.setupservice.control.actions;

import org.monet.federation.accountoffice.core.model.BusinessUnit;
import org.monet.federation.accountoffice.core.model.Federation;
import org.monet.federation.accountoffice.utils.PersisterHelper;
import org.monet.federation.setupservice.control.constants.Parameter;
import org.monet.federation.setupservice.core.constants.MessageCode;

public class ActionUnregisterPartner extends ActionSetupService {


  @Override
  public String execute() throws Exception{
    String businessUnitFederation = (String)this.parameters.get(Parameter.FEDERATION);
    String name = (String)this.parameters.get(Parameter.NAME);
    Federation federation = PersisterHelper.load(decode(businessUnitFederation), Federation.class);
    
    if (!this.dataRepository.existsBusinessUnit(federation.getName(), name)) {
      BusinessUnit businessUnit = this.dataRepository.loadBusinessUnit(federation.getName(), name);
      this.dataRepository.removeBusinessUnit(businessUnit.getId());
    }
    
    return MessageCode.FEDERATION_OPERATION_OK;
  }

}
