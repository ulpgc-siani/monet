/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

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

package org.monet.space.office.control.actions;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.utils.MimeTypes;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ActionReplaceNodeDocument extends Action {
	ComponentDocuments componentDocuments;

	public ActionReplaceNodeDocument() {
		super();
		this.componentDocuments = ComponentDocuments.getInstance();
	}

	@SuppressWarnings("unchecked")
	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String tempDocumentId = UUID.randomUUID().toString();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload uploadsManager = new ServletFileUpload(factory);
		List<FileItem> list = new LinkedList<FileItem>();
		Iterator<FileItem> iterator;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.REPLACE_NODE_DOCUMENT);
		}

		try {
			list = uploadsManager.parseRequest(this.request);
			iterator = list.listIterator();

			while (iterator.hasNext()) {
				FileItem fileItem = iterator.next();

				if (fileItem.isFormField()) {
					continue;
				}
				if (fileItem.getString().trim().equals(Strings.EMPTY)) {
					continue;
				}

				InputStream input = fileItem.getInputStream();
				MimeTypes mimeTypes = MimeTypes.getInstance();
				String contentType = fileItem.getContentType();

				String documentSchema = componentDocuments.getDocumentSchema(id);
				componentDocuments.uploadDocument(tempDocumentId, input, contentType, mimeTypes.isPreviewable(contentType));
				componentDocuments.overwriteDocument(id, tempDocumentId, documentSchema);
			}

		} catch (Exception oException) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.REPLACE_NODE_DOCUMENT, oException);
		}
		finally {
			componentDocuments.removeDocument(tempDocumentId);
		}

		return Language.getInstance().getMessage(MessageCode.NODE_DOCUMENT_REPLACED);
	}

}