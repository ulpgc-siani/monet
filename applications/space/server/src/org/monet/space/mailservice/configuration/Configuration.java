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

package org.monet.space.mailservice.configuration;

import org.monet.space.kernel.configuration.ApplicationConfiguration;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.mailservice.ApplicationMailService;

public class Configuration extends ApplicationConfiguration {

	private static Configuration oInstance;

	private Configuration() {
		super(ApplicationMailService.NAME);
	}

	public synchronized static Configuration getInstance() {
		if (oInstance == null) {
			oInstance = new Configuration();
		}
		return oInstance;
	}

	public boolean isPushEnable() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.isPushEnable();
	}

	public String getUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getUrl() + Strings.BAR45 + ApplicationMailService.NAME;
	}

	public String getServletUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getServletUrl() + Strings.BAR45 + ApplicationMailService.NAME;
	}

	public String getFmsServletUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getServletUrl() + "/fms";
	}

	public String getPageUrl(String page) {
		return Strings.EMPTY;
	}

	public String getPageDir(String page) {
		return Strings.EMPTY;
	}

	public String getEnterpriseLoginUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getEnterpriseLoginUrl();
	}

	public String getImagesPath() {
		return this.getBasePath() + "/" + ApplicationMailService.NAME + "/images";
	}

	public String getTempDir() {
		return this.getTempDir(ApplicationMailService.NAME);
	}

}