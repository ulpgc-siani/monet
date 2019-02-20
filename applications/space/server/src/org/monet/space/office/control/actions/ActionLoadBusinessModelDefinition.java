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

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.presentation.user.renders.OfficeRender;

import java.util.HashMap;

public class ActionLoadBusinessModelDefinition extends Action {

	public ActionLoadBusinessModelDefinition() {
		super();
	}

	public String execute() {
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String language = LibraryRequest.getParameter(Parameter.LANGUAGE, this.request);
		MimeTypes mimeTypes = MimeTypes.getInstance();
		BusinessModel businessModel = BusinessUnit.getInstance().getBusinessModel();
		Object renderObject = businessModel;
		OfficeRender render;
		String definition = "";

		if (code != null)
			renderObject = businessModel.getDictionary().getDefinition(code);
		else code = "dictionary";

		if (!BusinessModel.definitionsMap.containsKey(language))
			BusinessModel.definitionsMap.put(language, new HashMap<String, String>());

		HashMap<String, String> languageDefinitionsMap = BusinessModel.definitionsMap.get(language);

		if (languageDefinitionsMap.containsKey(code))
			definition = languageDefinitionsMap.get(code);
		else {
			render = this.rendersFactory.get(renderObject, "definition.js?mode=definition", this.getRenderLink(), getAccount());
			definition = render.getOutput();
			languageDefinitionsMap.put(code, definition);
		}

		try {
			this.response.setContentType(mimeTypes.getFromFilename("js"));
			this.response.setHeader("Content-Disposition", "inline;");
			this.response.getOutputStream().write(definition.getBytes());
			this.response.getOutputStream().flush();
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_BUSINESS_MODEL_DEFINITION, code, oException);
		}

		return null; // Avoid controller getting response writer
	}

}