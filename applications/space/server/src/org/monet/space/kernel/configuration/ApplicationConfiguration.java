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

package org.monet.space.kernel.configuration;

import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.constants.SiteFiles;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.SystemException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public abstract class ApplicationConfiguration {

	private Properties oProperties;

	public static final String PAGE_NOT_INSTALLED = "notinstalled";
	public static final String PAGE_OUT_OF_SERVICE = "outofservice";

	protected ApplicationConfiguration(String sApplication) {
		org.monet.space.kernel.configuration.Configuration oMonetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		InputStream isConfigFileContent;
		String configFile;

		try {
			this.oProperties = new Properties();
			configFile = oMonetConfiguration.getConfigurationDir() + "/" + sApplication + SiteFiles.CONFIG;
			if (!AgentFilesystem.existFile(configFile)) return;
			isConfigFileContent = AgentFilesystem.getInputStream(configFile);
			this.oProperties.loadFromXML(isConfigFileContent);
			this.parse();
			isConfigFileContent.close();
		} catch (IOException oException) {
			throw new SystemException("Load configuration file", sApplication, oException);
		}
	}

	private void parse() {
		Iterator<Object> iterator = this.oProperties.keySet().iterator();
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = (String) this.oProperties.get(key);
			value = value.replaceAll("::MONETDIR::", monetConfiguration.getFrameworkDir());
			value = value.replaceAll("::MONETUSERDATADIR::", monetConfiguration.getUserDataDir());
			value = value.replaceAll("::MONETAPPDATADIR::", monetConfiguration.getAppDataDir());
			value = value.replaceAll("::MONETDOMAIN::", monetConfiguration.getDomain());
			this.oProperties.setProperty(key, value);
		}
	}

	protected String getBaseDir() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getFrameworkDir();
	}

	protected String getBaseUserDataDir() {
		org.monet.space.kernel.configuration.Configuration oMonetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return oMonetConfiguration.getUserDataApplicationsDir();
	}

	protected String getBasePath() {
		org.monet.space.kernel.configuration.Configuration oMonetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return oMonetConfiguration.getPath();
	}

	public String getValue(String sName) {
		if (!this.oProperties.containsKey(sName)) return Strings.EMPTY;
		return this.oProperties.get(sName).toString();
	}

	public String getDomain() {
		org.monet.space.kernel.configuration.Configuration oMonetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return oMonetConfiguration.getDomain();
	}

	public Integer getPort() {
		org.monet.space.kernel.configuration.Configuration oMonetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return oMonetConfiguration.getPort();
	}

	public String getTempDir(String application) {
		org.monet.space.kernel.configuration.Configuration oMonetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return oMonetConfiguration.getTempDir() + "/" + application;
	}

	public String getEnterpriseLoginUrl() {
		org.monet.space.kernel.configuration.Configuration oMonetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return oMonetConfiguration.getEnterpriseLoginUrl();
	}

	public String getFmsServletUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getServletUrl() + "/fms";
	}

	public String getSignatoryServletUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getServletUrl() + "/signatory";
	}

	public String getAnalyticsServletUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getServletUrl() + "/analytics";
	}

	public String getApiUrl() {
		return this.getServletUrl() + ".api";
	}

	public Integer getApiPort() {
		return this.getPort();
	}

	public String getLanguagesUrl() {
		return this.getUrl() + "/languages";
	}

	public String getJavascriptUrl() {
		return this.getUrl() + "/javascript";
	}

	public String getWebComponentsUrl() {
		return this.getUrl() + "/../webcomponents";
	}

	public String getStylesUrl() {
		return this.getUrl() + "/styles";
	}

	public String getGoogleApiKey() {
		org.monet.space.kernel.configuration.Configuration oMonetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return oMonetConfiguration.getGoogleApiKey();
	}

	public String getOutOfServiceUrl() {
		return this.getPageUrl(PAGE_OUT_OF_SERVICE);
	}

	public String getNotInstalledUrl() {
		return this.getPageUrl(PAGE_OUT_OF_SERVICE);
	}

	public abstract String getTempDir();

	public abstract String getUrl();

	public abstract String getServletUrl();

	public abstract String getPageUrl(String page);

	public abstract String getPageDir(String page);

	public abstract String getImagesPath();

}