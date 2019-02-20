package org.monet.federation.setupservice.control.actions;

import org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationInfo;

public class ActionGetInfo extends ActionSetupService {

    @Override
    public String execute() throws Exception {
        FederationInfo info = new FederationInfo();
        info.setName(this.configuration.getName());
        info.setLabel(this.configuration.getLabel());
        info.setLogoPath(this.configuration.getLogoPath());
        return info.serializeToXML();
    }

}
