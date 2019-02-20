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
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.monet.space.explorer.control.dialogs.Dialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.explorer.control.exceptions.WriteEntityPermissionException;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.utils.MimeTypes;

import java.io.IOException;

public class DownloadNodeAction extends Action<Dialog, Display> {
    private LayerProvider layerProvider;

    @Inject
    public void inject(LayerProvider layerProvider) {
        this.layerProvider = layerProvider;
    }

    public void execute() throws IOException {
        NodeLayer nodeLayer = layerProvider.getNodeLayer();
        String nodeId = dialog.getEntityId();
        ComponentDocuments componentDocuments = componentProvider.getComponentDocuments();
        HttpClient httpClient = new HttpClient();

        Node node = nodeLayer.loadNode(nodeId);
        if (!componentProvider.getComponentSecurity().canWrite(node, getAccount()))
            throw new WriteEntityPermissionException();

        if (!node.isDocument()) {
            display.writeError(String.format(org.monet.space.explorer.model.Error.NODE_DOCUMENT_REQUIRED, node.getId()));
            return;
        }

        Boolean download = componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.DOWNLOAD);
        if (download == null || !download)
            throw new SystemException(org.monet.space.explorer.model.Error.DOWNLOAD_NOT_SUPPORTED, nodeId);

        try {
            GetMethod method = new GetMethod(componentDocuments.getDownloadUrl(dialog.getParameters()));
            int status = httpClient.executeMethod(method);

            if (status == HttpStatus.SC_NOT_FOUND)
                throw new SystemException(org.monet.space.explorer.model.Error.DOWNLOAD_NODE, nodeId, null);

            byte[] output = method.getResponseBody();
            String contentType = method.getResponseHeader("Content-Type").getValue();
            String extension = MimeTypes.getInstance().getExtension(contentType);
            String fileName = LibraryString.replaceAll(node.getLabel(), Strings.SPACE, Strings.UNDERLINED).replaceAll(Strings.COMMA, "") + Strings.DOT + extension;

            display.setContentLength(output.length);
            display.setContentType(contentType);
            display.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            display.getOutputStream().write(output);
            display.getOutputStream().flush();

        } catch (HttpException oException) {
            display.writeError(org.monet.space.explorer.model.Error.PREVIEW_NODE);
        } catch (IOException oException) {
            display.writeError(org.monet.space.explorer.model.Error.PREVIEW_NODE);
        }
    }

}