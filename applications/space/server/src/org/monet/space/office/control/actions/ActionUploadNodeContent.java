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
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.constants.Common;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ActionUploadNodeContent extends Action {

	public ActionUploadNodeContent() {
		super();
	}

	private Boolean doUploadContent(FileItem oFileItem) {
		String sFileDir = Configuration.getInstance().getImportsDir();
		String sFilename;
		File oDirectory, oFile;

		try {
			oDirectory = new File(sFileDir);
			if (!oDirectory.exists()) {
				oDirectory.mkdirs();
			}

			sFilename = this.getAccount().getId() + Common.FileExtensions.XML;
			oFile = new File(oDirectory.getAbsolutePath(), sFilename);
			oFileItem.write(oFile);
		} catch (Exception ex) {
			this.agentException.error(ex);
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public String execute() {
		String sHandler = LibraryRequest.getParameter(Parameter.DATA, this.request);
		FileItemFactory oFactory = new DiskFileItemFactory();
		ServletFileUpload oUploadsManager = new ServletFileUpload(oFactory);
		List<FileItem> oList = new LinkedList<FileItem>();
		Iterator<FileItem> oIterator;
		String sResult;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		try {
			oList = oUploadsManager.parseRequest(this.request);
			oIterator = oList.listIterator();

			while (oIterator.hasNext()) {
				FileItem oFileItem = oIterator.next();

				if (oFileItem.isFormField()) {
					continue;
				}
				if (oFileItem.getString().trim().equals(Strings.EMPTY)) {
					continue;
				}

				this.doUploadContent(oFileItem);
			}

		} catch (Exception oException) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.UPLOAD_NODE_CONTENT, oException);
		}

		sResult = Language.getInstance().getMessage(MessageCode.CONTENT_UPLOADED);
		if (sHandler != null) sResult = "<body onload='" + sHandler + "'></body>";

		return sResult;
	}

}