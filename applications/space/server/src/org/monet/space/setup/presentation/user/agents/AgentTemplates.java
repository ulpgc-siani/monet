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

package org.monet.space.setup.presentation.user.agents;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.monet.space.setup.core.constants.ErrorCode;
import org.monet.space.setup.presentation.user.util.Context;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.setup.configuration.Configuration;
import org.monet.space.setup.core.constants.SiteDirectories;

import java.io.Writer;


public class AgentTemplates {
	private VelocityEngine oVelocityEngine;
	private Configuration oConfiguration;
	private static AgentTemplates oInstance;

	private AgentTemplates() {
		try {
			this.oVelocityEngine = new VelocityEngine();
			this.oConfiguration = Configuration.getInstance();
			this.init();
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.AGENT_TEMPLATES, null, oException);
		}
	}

	private Boolean init() throws Exception {

		this.oVelocityEngine.addProperty(Velocity.RESOURCE_LOADER, "classpath");
		this.oVelocityEngine.addProperty("classpath." + Velocity.RESOURCE_LOADER + ".class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		this.oVelocityEngine.addProperty(Velocity.RUNTIME_LOG, this.oConfiguration.getTempDir() + Strings.BAR45 + SiteDirectories.LOGS + "/velocity.log");
		this.oVelocityEngine.addProperty(Velocity.INPUT_ENCODING, "UTF-8");
		this.oVelocityEngine.addProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		this.oVelocityEngine.init();

		return true;
	}

	public synchronized static AgentTemplates getInstance() {
		if (oInstance == null) oInstance = new AgentTemplates();
		return oInstance;
	}

	public void merge(String sFilename, Context oContext, Writer writer) {
		VelocityContext oVelocityContext;
		Template oTemplate;

		oVelocityContext = new VelocityContext(oContext.get());

		try {
			oTemplate = this.oVelocityEngine.getTemplate(sFilename);
			oTemplate.merge(oVelocityContext, writer);
		} catch (Exception ex) {
			AgentLogger.getInstance().error(ex);
		}
	}

}
