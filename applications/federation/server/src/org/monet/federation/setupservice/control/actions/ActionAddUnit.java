package org.monet.federation.setupservice.control.actions;

import org.monet.federation.accountoffice.core.model.BusinessUnit;

import java.net.URI;

public class ActionAddUnit extends ActionSetupService {

    @Override
    public String execute() throws Exception {
        BusinessUnit unit = new BusinessUnit();

        unit.setName("unit");
        unit.setUri(new URI("http://localhost:9000"));
        unit.setLabel("Unit");
        unit.setSecret("1234");
        unit.setEnable(true);
        unit.setVisible(true);

        this.dataRepository.createBusinessUnit(unit);
        return null;
    }
}
