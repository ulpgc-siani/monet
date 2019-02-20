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

import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

public class ActionRedirect extends Action {

	public ActionRedirect() {
		super();
	}

	public Boolean initialize() {
		return true;
	}

	public String execute() {
		String page = (String) this.request.getAttribute(Parameter.PAGE);

		try {

			if (page == null)
				throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.REDIRECT);

			String filename = Configuration.getInstance().getPageDir(page);
			File file = new File(filename);

			if (!file.exists())
				throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.REDIRECT);

			request.getRequestDispatcher(Configuration.getInstance().getPageUrl(page)).include(request, response);

		} catch (ServletException exception) {
			AgentLogger.getInstance().error(exception);
		} catch (IOException exception) {
			AgentLogger.getInstance().error(exception);
		}

		return null;
	}

}