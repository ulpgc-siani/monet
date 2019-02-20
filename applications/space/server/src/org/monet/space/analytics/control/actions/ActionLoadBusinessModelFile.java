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

package org.monet.space.analytics.control.actions;

import org.monet.metamodel.Definition;
import org.monet.metamodel.interfaces.HasClientBehaviour;
import org.monet.space.analytics.constants.Parameter;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.utils.MimeTypes;

import javax.servlet.http.HttpServletResponse;

public class ActionLoadBusinessModelFile extends Action {

	public ActionLoadBusinessModelFile() {
		super();
	}

	public String execute() {
		BusinessModel businessModel;
		String filename = this.getParameterAsString(Parameter.PATH);
		MimeTypes mimeTypes = MimeTypes.getInstance();
		String codeFormat, absoluteFilename = null, content = null;

		if (filename == null)
			throw new DataException("incorrect parameters", Actions.LOAD_BUSINESS_MODEL_FILE);

		if (filename.startsWith("scripts/")) {
			Definition definition = Dictionary.getInstance().getDefinition(filename.substring(filename.indexOf("/") + 1, filename.indexOf(".")));
			if (definition instanceof HasClientBehaviour) {
				content = ((HasClientBehaviour) definition).getClientBehaviour();
			}
		} else {
			businessModel = BusinessUnit.getInstance().getBusinessModel();
			absoluteFilename = businessModel.getAbsoluteFilename(filename);
		}
		codeFormat = mimeTypes.getFromFilename(filename);

		if (codeFormat == null) {
			throw new DataException("incorrect parameters", Actions.LOAD_BUSINESS_MODEL_FILE);
		}

		filename = filename.replace(Strings.BAR45, Strings.UNDERLINED);

		try {
			HttpServletResponse response = this.getResponse();
			response.setContentType(codeFormat);
			response.setHeader("Content-Disposition", "inline; filename=" + filename);
			if (absoluteFilename != null)
				response.getOutputStream().write(AgentFilesystem.getBytesFromFile(absoluteFilename));
			else
				response.getOutputStream().write(content.getBytes("UTF-8"));
			response.getOutputStream().flush();
		} catch (Exception oException) {
			throw new DataException("load business model file", filename, oException);
		}

		return null; // Avoid controller getting response writer
	}

}