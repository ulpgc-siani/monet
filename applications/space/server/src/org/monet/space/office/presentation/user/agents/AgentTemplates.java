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

package org.monet.space.office.presentation.user.agents;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.DateTool;
import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.presentation.user.util.Context;

import java.io.Writer;

public class AgentTemplates {
	private VelocityEngine modelVelocityEngine;
	private VelocityEngine applicationVelocityEngine;
	private Configuration configuration;
	private static AgentTemplates instance;

	private AgentTemplates() {
		try {
			this.modelVelocityEngine = new VelocityEngine();
			this.applicationVelocityEngine = new VelocityEngine();
			this.configuration = Configuration.getInstance();
			this.init();
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.AGENT_TEMPLATES, null, oException);
		}
	}

	private Boolean init() throws Exception {
		Kernel monet = Kernel.getInstance();
		String resourceLoaders;

		resourceLoaders = monet.getConfiguration().getBusinessModelDir();

		this.modelVelocityEngine.addProperty(Velocity.FILE_RESOURCE_LOADER_PATH, resourceLoaders);
		this.modelVelocityEngine.addProperty(Velocity.RUNTIME_LOG, this.configuration.getLogsDir() + "/office.velocity.log");
		this.modelVelocityEngine.addProperty(Velocity.INPUT_ENCODING, "UTF-8");
		this.modelVelocityEngine.addProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		this.modelVelocityEngine.init();

		this.applicationVelocityEngine.addProperty(Velocity.RESOURCE_LOADER, "classpath");
		this.applicationVelocityEngine.addProperty("classpath." + Velocity.RESOURCE_LOADER + ".class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		this.applicationVelocityEngine.addProperty(Velocity.RUNTIME_LOG, this.configuration.getLogsDir() + "/office.velocity.log");
		this.applicationVelocityEngine.addProperty(Velocity.INPUT_ENCODING, "UTF-8");
		this.applicationVelocityEngine.addProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		this.applicationVelocityEngine.init();

		return true;
	}

	public synchronized static AgentTemplates getInstance() {
		if (instance == null) instance = new AgentTemplates();
		return instance;
	}

	public void mergeModelTemplate(String sFilename, Context oContext, Writer writer) {
		VelocityContext oVelocityContext;
		Template oTemplate;

		oVelocityContext = new VelocityContext(oContext.get());
		oVelocityContext.put("DateTool", new DateTool());
		oVelocityContext.put("ComparisonDateTool", new ComparisonDateTool());

		try {
			oTemplate = this.modelVelocityEngine.getTemplate(sFilename);
			oTemplate.merge(oVelocityContext, writer);
		} catch (Exception ex) {
			AgentLogger.getInstance().error(ex);
		}
	}

	public void mergeApplicationTemplate(String path, Context oContext, Writer writer) {
		VelocityContext oVelocityContext;
		Template oTemplate;

		oVelocityContext = new VelocityContext(oContext.get());
		oVelocityContext.put("DateTool", new DateTool());
		oVelocityContext.put("ComparisonDateTool", new ComparisonDateTool());

		try {
			oTemplate = this.applicationVelocityEngine.getTemplate(path);
			oTemplate.merge(oVelocityContext, writer);
		} catch (Exception ex) {
			AgentLogger.getInstance().error(ex);
		}
	}
}
