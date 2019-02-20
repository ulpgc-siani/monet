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
import org.monet.space.explorer.control.dialogs.NodeHelpDialog;
import org.monet.space.explorer.control.displays.PageDisplay;
import org.monet.space.explorer.control.exceptions.ReadEntityPermissionException;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Page;

import java.io.IOException;

public class LoadNodeHelpPageAction extends Action<NodeHelpDialog, PageDisplay> {
    private BusinessUnit businessUnit;

    @Inject
    public void inject(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public void execute() throws IOException {
        Node node = dialog.getNode();

        if (!componentProvider.getComponentSecurity().canRead(node, getAccount()))
            throw new ReadEntityPermissionException();

        Page page = businessUnit.loadNodeHelperPage(node.getDefinition().getCode());
        String helpDir = Configuration.getInstance().getBusinessModelResourcesHelpDir();
        String helpFilename = helpDir + "/" + page.getFilename();

        if (AgentFilesystem.existFile(helpFilename))
            page.setContent(AgentFilesystem.readFile(helpFilename));

        display.write(page);
    }

}