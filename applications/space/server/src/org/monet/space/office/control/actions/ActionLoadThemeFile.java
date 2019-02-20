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
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Theme;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionLoadThemeFile extends Action {

	public ActionLoadThemeFile() {
		super();
	}

	public String execute() {
		String filename = LibraryRequest.getParameter(Parameter.PATH, this.request);
		MimeTypes mimeTypes = MimeTypes.getInstance();
		String codeFormat, absoluteFilename;

		if (filename == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_THEME_FILE);
		}

		absoluteFilename = Theme.getAbsoluteFilename(filename);
		codeFormat = mimeTypes.getFromFilename(filename);

		if (codeFormat == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_THEME_FILE);
		}

		filename = filename.replace(Strings.BAR45, Strings.UNDERLINED);

		try {
			this.response.setContentType(codeFormat);
			this.response.setHeader("Content-Disposition", "inline;");
			this.response.getOutputStream().write(AgentFilesystem.getBytesFromFile(absoluteFilename));
			this.response.getOutputStream().flush();
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_THEME_FILE, absoluteFilename, oException);
		}

		return null; // Avoid controller getting response writer
	}

}