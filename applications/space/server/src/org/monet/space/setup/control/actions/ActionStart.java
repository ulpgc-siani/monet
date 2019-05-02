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

package org.monet.space.setup.control.actions;

import org.monet.space.applications.library.LibraryResponse;
import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.exceptions.BaseException;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.ComponentInfo;
import org.monet.space.kernel.model.Context;
import org.monet.space.setup.ApplicationSetup;
import org.monet.space.setup.control.constants.Parameter;
import org.monet.space.setup.control.constants.SessionVariable;

import java.util.Enumeration;
import java.util.Map;

public class ActionStart extends Action {

	public ActionStart() {
		super();
	}

	private Boolean setBusinessUnitComponents() {
		Enumeration<String> enumeration = request.getParameterNames();
        Map<String, ComponentInfo> hmComponents = Kernel.getInstance().getComponents();

		hmComponents.clear();

		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();

			if (!name.contains(Parameter.COMPONENT_PREFIX)) continue;

            String value = request.getParameter(name);
			hmComponents.put(value, new ComponentInfo(value));
		}

		return true;
	}

	public String execute() {

		if (!kernel.isLogged()) {
			launchAuthenticateAction();
			return null;
		}

		String codeError = "";

		try {
			if (!BusinessUnit.isRunning()) {
				setBusinessUnitComponents();
				Kernel.getInstance().run(null);
			}
		} catch (BaseException exception) {
			AgentLogger.getInstance().error(exception);
			codeError = exception.getError().getCode();
		}

		Context.getInstance().getCurrentSession().setVariable(SessionVariable.ERROR, codeError);
		LibraryResponse.sendRedirect(response, ApplicationSetup.getConfiguration().getUrl());

		return null;
	}

}