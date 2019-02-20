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
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.setup.ApplicationSetup;

import java.io.IOException;

public class ActionDownloadDistribution extends Action {

	public ActionDownloadDistribution() {
		super();
	}

	public String execute() {

		if (!this.kernel.isLogged()) {
			this.launchAuthenticateAction();
			return null;
		}

		String businessModelZipLocation = Configuration.getInstance().getBusinessModelZipLocation();
		byte[] output = AgentFilesystem.getBytesFromFile(businessModelZipLocation);
		String contentType = "application/zip";

		try {
			this.response.setContentLength(output.length);
			this.response.setContentType(contentType);
			this.response.setHeader("Content-Disposition", "attachment; filename=distribution.zip");
			this.response.getOutputStream().write(output);
			this.response.getOutputStream().flush();
		} catch (IOException exception) {
			this.agentException.error(exception);
		}

		LibraryResponse.sendRedirect(this.response, ApplicationSetup.getConfiguration().getUrl() + "?tab=distribution");

		return null;
	}

}