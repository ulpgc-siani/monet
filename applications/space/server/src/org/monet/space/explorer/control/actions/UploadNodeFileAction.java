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

import org.monet.space.kernel.components.ComponentDocuments;

import java.io.ByteArrayInputStream;

public class UploadNodeFileAction extends UploadNodeResourceAction {

    private static final String ID_DOCUMENT_TEMPLATE = "%s/file/%s";

    @Override
    protected String upload(String resourceName, byte[] resource, String contentType) {
        String documentId = String.format(ID_DOCUMENT_TEMPLATE, dialog.getNode().getId(), resourceName);
        ComponentDocuments componentDocuments = componentProvider.getComponentDocuments();
//        boolean generatePreview = mimeTypes.isPreviewable(contentType);

        componentDocuments.uploadDocument(documentId, new ByteArrayInputStream(resource), contentType, false);

        return documentId;
    }

}