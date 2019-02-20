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

import org.monet.metamodel.Definition;
import org.monet.metamodel.interfaces.HasClientBehaviour;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionLoadBusinessModelFile extends Action {

	public ActionLoadBusinessModelFile() {
		super();
	}

	public String execute() {
		BusinessModel businessModel;
		String filename = LibraryRequest.getParameter(Parameter.PATH, this.request);
		MimeTypes mimeTypes = MimeTypes.getInstance();
		String codeFormat, absoluteFilename = null, content = null;

		if (filename == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_BUSINESS_MODEL_FILE);
		}

		if (filename.startsWith("scripts/")) {
			String definitionCode = filename.contains(".client.js")?filename.replace(".client.js", "").substring(filename.indexOf("/")+1):filename.substring(filename.indexOf("/") + 1, filename.lastIndexOf("."));
			Definition definition = Dictionary.getInstance().getDefinition(definitionCode);
			if (definition instanceof HasClientBehaviour) {
				content = ((HasClientBehaviour) definition).getClientBehaviour();
			}
		} else {
			businessModel = BusinessUnit.getInstance().getBusinessModel();
			absoluteFilename = businessModel.getAbsoluteFilename(filename);
		}
		codeFormat = mimeTypes.getFromFilename(filename);

		if (codeFormat == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_BUSINESS_MODEL_FILE);
		}

		filename = filename.replace(Strings.BAR45, Strings.UNDERLINED);

		try {
			this.response.setContentType(codeFormat);
			this.response.setHeader("Content-Disposition", "inline; filename=" + filename);
			if (absoluteFilename != null)
				this.response.getOutputStream().write(AgentFilesystem.getBytesFromFile(absoluteFilename));
			else
				this.response.getOutputStream().write(content.getBytes("UTF-8"));
			this.response.getOutputStream().flush();
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_BUSINESS_MODEL_FILE, filename, oException);
		}

		return null; // Avoid controller getting response writer
	}

}