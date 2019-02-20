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

import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.UploadNodeResourceDialog;
import org.monet.space.explorer.control.displays.JsonElementDisplay;
import org.monet.space.explorer.control.exceptions.WriteEntityPermissionException;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.utils.MimeTypes;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public abstract class UploadNodeResourceAction extends Action<UploadNodeResourceDialog, JsonElementDisplay> {
    protected MimeTypes mimeTypes;

    private static final String OUTPUT_FORMAT = "{status: %d,name: \"%s\", mime: \"%s\", size: %d, id: \"%s\"}";
    private static final int STATUS_OK = 0;

    @Inject
    public void inject(MimeTypes mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public void execute() throws IOException {
        Node node = dialog.getNode();
        ComponentDocuments componentDocuments = componentProvider.getComponentDocuments();

        if (!componentProvider.getComponentSecurity().canWrite(node, getAccount()))
            throw new WriteEntityPermissionException();

        Boolean upload = componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.UPLOAD);
        if (upload == null || !upload)
            throw new SystemException(org.monet.space.explorer.model.Error.UPLOAD_NOT_SUPPORTED, node.getId());

        byte[] resource = dialog.getResource();
        if (resource == null) {
            display.writeError(org.monet.space.explorer.model.Error.UPLOAD_NODE);
            return;
        }

        String resourceName = dialog.getResourceName();
        String contentType = getContentType(resourceName, resource);
        int size = resource.length;

        String documentId = upload(resourceName, resource, contentType);
        if (documentId == null) {
            display.writeError(org.monet.space.explorer.model.Error.UPLOAD_NODE);
            return;
        }

        display.write(new JsonParser().parse(String.format(OUTPUT_FORMAT, STATUS_OK, resourceName, contentType, size, documentId)));
    }

    protected abstract String upload(String resourceName, byte[] resource, String contentType);

    private String getContentType(String filename, byte[] file) {
        String contentType = mimeTypes.getFromStream(new ByteArrayInputStream(file));

        if (contentType == null || contentType.equals("application/octet-stream"))
            contentType = mimeTypes.getFromFilename(filename);

        return contentType;
    }

}