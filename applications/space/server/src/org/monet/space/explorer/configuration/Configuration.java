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

package org.monet.space.explorer.configuration;

import org.monet.space.explorer.ApplicationExplorer;
import org.monet.space.kernel.configuration.ApplicationConfiguration;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.utils.Resources;

import java.io.InputStream;

public class Configuration extends ApplicationConfiguration {
	private static Configuration instance;

	private static final String THEME = "Theme";

	private Configuration() {
		super(ApplicationExplorer.NAME);
	}

	public synchronized static Configuration getInstance() {
		if (instance == null)
			instance = new Configuration();
		return instance;
	}

	public String getUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getUrl() + Strings.BAR45 + ApplicationExplorer.NAME;
	}

	@Override
	public String getApiUrl() {
		return getUrl() + "/api";
	}

	public String getPushApiUrl() {
		return getServletUrl() + "/push";
	}

	public String getServletUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getServletUrl() + Strings.BAR45 + ApplicationExplorer.NAME;
	}

	public String getFmsServletUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getServletUrl() + "/fms";
	}

	public String getAnalyticsServletUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getServletUrl() + "/analytics";
	}

	public String getPageUrl(String page) {
		return Strings.BAR45 + page + ".jsp";
	}

	public String getPageDir(String page) {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getFrameworkDir() + Strings.BAR45 + page + ".jsp";
	}

	public String getEnterpriseLoginUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getEnterpriseLoginUrl();
	}

	public String getImagesPath() {
		return this.getBasePath() + "/" + ApplicationExplorer.NAME + "/images";
	}

	public String getTempDir() {
		return this.getTempDir(ApplicationExplorer.NAME);
	}

	public String getImportsDir() {
		return this.getTempDir() + "/imports";
	}

	public String getDocumentsDir() {
		return this.getTempDir() + "/documents";
	}

	public String getLogsDir() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getLogsDir();
	}

	public String getMonetSplashLogoUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return this.getImagesPath() + "/" + monetConfiguration.getMonetSplashLogoFilename();
	}

	public String getBusinessModelLogoUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return this.getApiUrl() + "/model$file/?file=images/" + monetConfiguration.getModelLogoFilename();
	}

	public String getBusinessModelSplashLogoUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return this.getApiUrl() + "/model$file/?file=images/" + monetConfiguration.getModelSplashLogoFilename();
	}

	public String getFederationLogoUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return this.getApiUrl() + "/model$file/?file=images/" + monetConfiguration.getFederationLogoFilename();
	}

	public String getFederationSplashLogoUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return this.getApiUrl() + "/model$file/?file=images/" + monetConfiguration.getFederationSplashLogoFilename();
	}

	public String getAnalyticsUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getUrl() + Strings.BAR45 + "analytics";
	}

	public InputStream getTemplate(String name, String path) {
		return Resources.getAsStream("/" + ApplicationExplorer.NAME + "/templates/" + (path!=null?path + "/":"") + name + ".html");
	}

	public InputStream getTemplate(String name) {
		return getTemplate(name, null);
	}

	public String getTheme() {
		return getValue(THEME);
	}

	public String getDigitalSignatureUrl() {
		return this.getUrl() + Strings.BAR45 + "digitalsignature";
	}

}