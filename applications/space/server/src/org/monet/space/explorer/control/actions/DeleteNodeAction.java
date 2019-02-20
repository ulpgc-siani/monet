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
import org.monet.space.explorer.control.dialogs.DeleteNodeDialog;
import org.monet.space.explorer.control.displays.HttpDisplay;
import org.monet.space.explorer.control.exceptions.DeleteEntityPermissionException;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

import java.io.IOException;

public class DeleteNodeAction extends Action<DeleteNodeDialog, HttpDisplay> {
    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
	    NodeLayer nodeLayer = layerProvider.getNodeLayer();
        Node node = dialog.getNode();

	    if (!componentProvider.getComponentSecurity().canDelete(node, getAccount()))
		    throw new DeleteEntityPermissionException();

	    nodeLayer.deleteNode(node);

        display.write("OK");
    }

}