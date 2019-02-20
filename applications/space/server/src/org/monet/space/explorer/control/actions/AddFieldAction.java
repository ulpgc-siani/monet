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
import org.monet.metamodel.CompositeFieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.space.explorer.control.dialogs.AddFieldDialog;
import org.monet.space.explorer.control.displays.DefaultDisplay;
import org.monet.space.explorer.control.exceptions.WriteEntityPermissionException;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.explorer.model.PathResolver;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.AttributeList;
import org.monet.space.kernel.model.Node;

import java.io.IOException;

public class AddFieldAction extends Action<AddFieldDialog, DefaultDisplay> {
    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
        Node node = dialog.getNode();

        checkWritePermissions();

        PathResolver resolver = new PathResolver();
        String parentPath = dialog.getParentPath();
        Attribute attribute = dialog.getField();

        Attribute parentAttribute = resolver.resolveAttribute(node, parentPath);
        if (attribute.getAttributeList() == null) {
            FormDefinition definition = (FormDefinition) node.getDefinition();
            CompositeFieldProperty fieldDefinition = (CompositeFieldProperty) definition.getField(resolver.getLeafAttributeCode(parentPath));
            attribute.setAttributeList(new AttributeList(fieldDefinition.getAllFieldPropertyList()));
        }

        AttributeList parentAttributeList = (parentAttribute == null) ? node.getAttributeList() : parentAttribute.getAttributeList();
        parentAttributeList.insert(dialog.getPos(), attribute);
        layerProvider.getNodeLayer().saveNode(node);

        display.write("OK");
    }

    private void checkWritePermissions() {
        if (!componentProvider.getComponentSecurity().canWrite(dialog.getNode(), getAccount()))
            throw new WriteEntityPermissionException();
    }
}