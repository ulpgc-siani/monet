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

package org.monet.space.office;

import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.applications.Application;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;

public class ApplicationOffice extends Application {
	public static final String NAME = "office";
	public static final String SERVLET_NAME = "servletoffice";
	public static final String LOG_AJAX = "office_ajax.log";

	private ApplicationOffice() {
		super();
	}

	public synchronized static Application getInstance() {
		if (instance == null) instance = new ApplicationOffice();
		return instance;
	}

	public static Configuration getConfiguration() {
		return Configuration.getInstance();
	}

	public static String getCallbackUrl() {
		Configuration configuration = getConfiguration();
		return configuration.getUrl() + "?" + Parameter.ACTION + "=" + Actions.LOGIN;
	}

	public void run() {
		this.isRunning = true;
	}

	public void stop() {
		this.isRunning = false;
	}

	public void clean() {
		String tempDir = Configuration.getInstance().getTempDir();
		AgentFilesystem.removeDir(tempDir);
	}

}