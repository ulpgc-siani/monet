/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2014  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.explorer.control.actions;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.Dialog;
import org.monet.space.explorer.control.displays.BusinessUnitDisplay;
import org.monet.space.explorer.model.ExplorerList;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.FederationUnit;
import org.monet.space.kernel.model.FederationUnitList;

import java.io.IOException;

public class LoadBusinessUnitsAction extends Action<Dialog, BusinessUnitDisplay> {
    private BusinessUnit businessUnit;

    @Inject
    public void inject(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

	public void execute() throws IOException {
        FederationUnitList federationUnitList = this.getFederationLayer().loadMembers(getAccount());
        ExplorerList<org.monet.space.explorer.model.BusinessUnit> result = new ExplorerList<>();
        final String businessUnitName = businessUnit.getName();

        result.setTotalCount(federationUnitList.getTotalCount());

        for (final FederationUnit federationUnit : federationUnitList)
            result.add(new org.monet.space.explorer.model.BusinessUnit() {

                @Override
                public String getName() {
                    return federationUnit.getName();
                }

                @Override
                public Type getType() {
                    return federationUnit.isMember()?Type.MEMBER:Type.PARTNER;
                }

                @Override
                public String getUrl() {
                    return federationUnit.getUrl();
                }

                @Override
                public boolean isActive() {
                    return businessUnitName.equals(federationUnit.getName());
                }

                @Override
                public boolean isDisabled() {
                    return false;
                }
            });

        display.writeList(result);
	}

}