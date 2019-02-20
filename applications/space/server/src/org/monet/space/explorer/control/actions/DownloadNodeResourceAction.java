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
import org.monet.space.explorer.control.dialogs.DownloadNodeResourceDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.explorer.control.exceptions.ReadEntityPermissionException;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.library.LibraryFile;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.utils.MimeTypes;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public abstract class DownloadNodeResourceAction extends Action<DownloadNodeResourceDialog, Display> {
    protected MimeTypes mimeTypes;
    protected AgentFilesystem agentFilesystem;

    @Inject
    public void inject(MimeTypes mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    @Inject
    public void inject(AgentFilesystem agentFilesystem) {
        this.agentFilesystem = agentFilesystem;
    }

    public void execute() throws IOException {
        Node node = dialog.getNode();
        ComponentDocuments componentDocuments = componentProvider.getComponentDocuments();

        if (!componentProvider.getComponentSecurity().canRead(node, getAccount()))
            throw new ReadEntityPermissionException();

        Boolean download = componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.DOWNLOAD);
        if (download == null || !download)
            throw new SystemException(org.monet.space.explorer.model.Error.DOWNLOAD_NOT_SUPPORTED, node.getId());

        Map<String, String> parameters = new HashMap<>();
        String resourceId = dialog.getResourceName();

        parameters.put("id", URLEncoder.encode(resourceId, "UTF-8"));
        if (dialog.downloadThumb())
            parameters.put("thumb", "1");

        HttpClient httpClient = new HttpClient();
        GetMethod method = new GetMethod(componentDocuments.getDownloadUrl(parameters));
        int status = httpClient.executeMethod(method);
        byte[] output;
        String contentType;

        if (status == HttpStatus.SC_NOT_FOUND || method.getResponseHeader("Content-Type") == null) {
            output = getNotFoundResource();
            contentType = getNotFoundResourceContentType();
        } else {
            output = method.getResponseBody();
            contentType = method.getResponseHeader("Content-Type").getValue();
        }

        if (output == null) {
            display.writeError(org.monet.space.explorer.model.Error.DOWNLOAD_NODE);
            return;
        }

        display.setContentLength(output.length);
        display.setContentType(contentType);
        display.addHeader("Content-Disposition", "attachment; filename=\"" + LibraryFile.getFilename(resourceId) + "\"");
        display.getOutputStream().write(output);
        display.getOutputStream().flush();
    }

    protected abstract byte[] getNotFoundResource();
    protected abstract String getNotFoundResourceContentType();

}