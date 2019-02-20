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
import org.monet.space.explorer.control.dialogs.ChangeFieldOrderDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.explorer.control.exceptions.WriteEntityPermissionException;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.explorer.model.PathResolver;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.AttributeList;
import org.monet.space.kernel.model.Node;

import java.io.IOException;

public class ChangeFieldOrderAction extends Action<ChangeFieldOrderDialog, Display> {
    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
        NodeLayer nodeLayer = layerProvider.getNodeLayer();
        Node node = dialog.getNode();

	    if (!componentProvider.getComponentSecurity().canWrite(node, getAccount()))
		    throw new WriteEntityPermissionException();

	    if (dialog.getOldPos() == dialog.getNewPos()) {
		    display.write("OK");
		    return;
	    }

	    PathResolver resolver = new PathResolver();
        String parentPath = dialog.getParentPath();

        Attribute parentAttribute = resolver.resolveAttribute(node, parentPath);
        if (parentAttribute == null) {
            display.writeError(String.format(org.monet.space.explorer.model.Error.NODE_PARENT_FIELD_NOT_FOUND, parentPath));
            return;
        }

        reorder(parentAttribute);
        nodeLayer.saveNode(node);

        display.write("OK");
    }

    private synchronized void reorder(Attribute parentAttribute) {
        AttributeList attributeList = parentAttribute.getAttributeList();
        int oldPos = dialog.getOldPos();
        int newPos = dialog.getNewPos();
        int currentPos = 0;
        Attribute attribute = attributeList.get(oldPos);
        AttributeList newAttributeList = new AttributeList();

        for (Attribute childAttribute : attributeList) {

            if (currentPos == oldPos) {
	            currentPos++;
	            continue;
            }

	        if (oldPos <= newPos)
		        newAttributeList.add(childAttribute);

            if (currentPos == newPos)
	            newAttributeList.add(attribute);

	        if (oldPos > newPos)
                newAttributeList.add(childAttribute);

            currentPos++;
        }

        parentAttribute.setAttributeList(newAttributeList);
    }

}