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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.monet.space.explorer.control.dialogs.Dialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.explorer.control.exceptions.WriteEntityPermissionException;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.model.Node;

import java.io.IOException;

public class PreviewNodeAction extends Action<Dialog, Display> {
    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
        try {
            if (canSendPreview(layerProvider.getNodeLayer().loadNode(dialog.getEntityId())))
                sendPreview(componentProvider.getComponentDocuments());
        } catch (IOException exception) {
            display.writeError(org.monet.space.explorer.model.Error.PREVIEW_NODE);
        }
    }

    private boolean canSendPreview(Node node) {
        checkWritePermission(node);
        return nodeIsDocument(node) && previewIsSupported(componentProvider.getComponentDocuments());
    }

    private void sendPreview(ComponentDocuments componentDocuments) throws IOException {
        GetMethod method = new GetMethod(componentDocuments.getPreviewUrl(dialog.getParameters()));

        if (new HttpClient().executeMethod(method) == HttpStatus.SC_NOT_FOUND)
            display.writeError(org.monet.space.explorer.model.Error.PREVIEW_NODE);
        else
            display.getOutputStream().write(method.getResponseBody());
    }

    private void checkWritePermission(Node node) {
        if (!componentProvider.getComponentSecurity().canWrite(node, getAccount()))
            throw new WriteEntityPermissionException();
    }

    private boolean nodeIsDocument(Node node) {
        if (!node.isDocument()) {
            display.writeError(org.monet.space.explorer.model.Error.NODE_DOCUMENT_REQUIRED);
            return false;
        }
        return true;
    }

    private boolean previewIsSupported(ComponentDocuments componentDocuments) {
        Boolean preview = componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.PREVIEW);
        if (preview == null || !preview) {
            display.writeError(org.monet.space.explorer.model.Error.PREVIEW_NOT_SUPPORTED);
            return false;
        }
        return true;
    }

}