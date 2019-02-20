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
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.MasterLayer;
import org.monet.space.setup.ApplicationSetup;
import org.monet.space.setupservice.control.constants.Parameter;

import java.io.IOException;

public class ActionDeleteMaster extends Action {

	public ActionDeleteMaster() {
		super();
	}

	public String execute() {

		if (!this.kernel.isLogged()) {
			this.launchAuthenticateAction();
			return null;
		}

		MasterLayer masterLayer = ComponentFederation.getMasterLayer();
		String id = (String) this.request.getParameter(Parameter.ID);

		try {
			if (id == null) {
				response.setStatus(403);
				response.getWriter().println("No id on request");
				return null;
			}
			masterLayer.deleteMaster(id);
		} catch (IOException exception) {
			AgentLogger.getInstance().error(exception);
		}

		LibraryResponse.sendRedirect(this.response, ApplicationSetup.getConfiguration().getUrl() + "?tab=masters");

		return null;
	}

}