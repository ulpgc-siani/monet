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

package org.monet.space.frontservice;

import org.monet.space.frontservice.control.constants.Actions;
import org.monet.space.frontservice.control.constants.Parameter;
import org.monet.space.frontservice.configuration.Configuration;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.applications.Application;

public class ApplicationFrontService extends Application {
	public static final String NAME = "frontservice";

	private ApplicationFrontService() {
		super();
	}

	public synchronized static Application getInstance() {
		if (instance == null) instance = new ApplicationFrontService();
		return instance;
	}

	public static Configuration getConfiguration() {
		return Configuration.getInstance();
	}

	public static String getCallbackUrl() {
		Configuration configuration = getConfiguration();
		return configuration.getUrl() + "?" + Parameter.OPERATION + "=" + Actions.SHOW_SERVICES;
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