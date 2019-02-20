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
import org.monet.space.explorer.control.dialogs.SaveFieldDialog;
import org.monet.space.explorer.control.displays.DefaultDisplay;
import org.monet.space.explorer.control.exceptions.WriteEntityPermissionException;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.explorer.model.PathResolver;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.AttributeList;
import org.monet.space.kernel.model.Node;

import java.io.IOException;

public class SaveFieldAction extends Action<SaveFieldDialog, DefaultDisplay> {

    private final PathResolver resolver = new PathResolver();
    private LayerProvider layerProvider;
    private AgentLogger logger;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    @Inject
    public void inject(AgentLogger logger) {
        this.logger = logger;
    }

    public void execute() throws IOException {
        checkWritePermissions();
        try {
            saveField(dialog.getNode(), dialog.getFieldAttribute());
        }
        catch (RuntimeException exception) {
            display.writeError(exception.getMessage());
        }
        display.write("OK");
    }

    private void checkWritePermissions() {
        if (!componentProvider.getComponentSecurity().canWrite(dialog.getNode(), getAccount()))
            throw new WriteEntityPermissionException();
    }

    private void saveField(Node node, Attribute fieldAttribute) {
        Attribute attribute = null;

        try {
            attribute = resolver.resolveAttribute(node, dialog.getPath());
        } catch (Throwable e) {
            createAttribute(node, resolver.parentCode(dialog.getPath()));
            try {
                attribute = resolver.resolveAttribute(node, dialog.getPath());
            } catch (Throwable e1) {
                this.logger.error(e1);
            }
        }

        if (attribute == null)
            attribute = createAttribute(node, resolver.lastFieldCode(dialog.getPath()));

        attribute.setIndicatorList(fieldAttribute.getIndicatorList());

        AttributeList attributeList = fieldAttribute.getAttributeList();
        if (attributeList != null)
            attribute.setAttributeList(attributeList);

        layerProvider.getNodeLayer().saveNode(node);
    }

    private Attribute createAttribute(Node node, String code) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);

        Attribute parent = resolver.getParent(node, dialog.getPath());
        AttributeList parentAttributeList = parent == null ? node.getAttributeList() : parent.getAttributeList();
        parentAttributeList.add(attribute);

        return attribute;
    }

}