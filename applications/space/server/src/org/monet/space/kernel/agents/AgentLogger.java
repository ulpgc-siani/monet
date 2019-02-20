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

package org.monet.space.kernel.agents;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AgentLogger {
	private static AgentLogger instance;
	private static Logger applicationLogger;
	private static Logger modelLogger;

	protected AgentLogger() {
		super();
	}

	public synchronized static AgentLogger getInstance() {
		if (instance == null) instance = new AgentLogger();
		return instance;
	}

	private void createLogger() {
		Configuration configuration = Configuration.getInstance();
		Properties properties = new Properties();
		InputStream configurationStream = null;
		try {
			configurationStream = new FileInputStream(configuration.getConfigurationDir() + "/log4j.config");
			properties.loadFromXML(configurationStream);
			PropertyConfigurator.configure(properties);
		} catch (Exception e) {
			//No logger...
			e.printStackTrace();
		} finally {
			StreamHelper.close(configurationStream);
		}
		AgentLogger.applicationLogger = Logger.getLogger("application");
		AgentLogger.modelLogger = Logger.getLogger("model");
	}

	public void error(String message, Throwable exception) {
		if (AgentLogger.applicationLogger == null) {
			this.createLogger();
		}
		AgentLogger.applicationLogger.error(message, exception);
	}

	public void errorInModel(String message, Throwable exception) {
		if (AgentLogger.modelLogger == null) {
			this.createLogger();
		}
		AgentLogger.modelLogger.error(message, exception);
	}

	public void error(Throwable exception) {
		if (AgentLogger.applicationLogger == null) {
			this.createLogger();
		}
		AgentLogger.applicationLogger.error(exception.getMessage(), exception);
	}

	public void error(String error) {
		if (AgentLogger.applicationLogger == null) {
			this.createLogger();
		}
		AgentLogger.applicationLogger.error(error);
	}

	public void errorInModel(Throwable exception) {
		if (AgentLogger.modelLogger == null) {
			this.createLogger();
		}
		AgentLogger.modelLogger.error(exception.getMessage(), exception);
	}

	public void info(String message, Object... args) {
		if (AgentLogger.applicationLogger == null) {
			this.createLogger();
		}
		AgentLogger.applicationLogger.info(String.format(message, args));
	}

	public void infoInModel(String message, Object... args) {
		if (AgentLogger.modelLogger == null) {
			this.createLogger();
		}
		AgentLogger.modelLogger.info(String.format(message, args));
	}

	public void debug(String message, Object... args) {
		if (AgentLogger.applicationLogger == null) {
			this.createLogger();
		}
		AgentLogger.applicationLogger.debug(String.format(message, args));
	}

	public void debugInModel(String message, Object... args) {
		if (AgentLogger.modelLogger == null) {
			this.createLogger();
		}
		AgentLogger.modelLogger.debug(String.format(message, args));
	}

	public void trace(String message, Object... args) {
		if (AgentLogger.applicationLogger == null) {
			this.createLogger();
		}
		AgentLogger.applicationLogger.trace(String.format(message, args));
	}
}