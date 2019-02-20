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

package org.monet.space.analytics.configuration;

import org.monet.space.analytics.ApplicationAnalytics;
import org.monet.space.analytics.model.Format;
import org.monet.space.analytics.model.ChartType;
import org.monet.space.kernel.configuration.ApplicationConfiguration;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.utils.Resources;

public class Configuration extends ApplicationConfiguration {
	private static Configuration instance;

	private static final String PROVIDER_PREFIX = "Provider.";

	private Configuration() {
		super(ApplicationAnalytics.NAME);
	}

	public synchronized static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public String getPushApiUrl() {
		return this.getServletUrl() + ".push";
	}

	public String getUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getUrl() + Strings.BAR45 + ApplicationAnalytics.NAME;
	}

	public String getServletUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getServletUrl() + Strings.BAR45 + ApplicationAnalytics.NAME;
	}

	public String getPageUrl(String page) {
		return Strings.BAR45 + ApplicationAnalytics.NAME + Strings.BAR45 + page + ".jsp";
	}

	public String getPageDir(String page) {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getFrameworkDir() + Strings.BAR45 + ApplicationAnalytics.NAME + Strings.BAR45 + page + ".jsp";
	}

	public String getImagesUrl() {
		return this.getUrl() + "/" + ApplicationAnalytics.NAME + "/images";
	}

	public String getImagesPath() {
		return this.getBasePath() + "/" + ApplicationAnalytics.NAME + "/images";
	}

	public String getTempDir() {
		return this.getTempDir(ApplicationAnalytics.NAME);
	}

	public String getImportsDir() {
		return this.getTempDir() + "/imports";
	}

	public String getDocumentsDir() {
		return this.getTempDir() + "/documents";
	}

	public String getLogsDir() {
		return org.monet.space.kernel.configuration.Configuration.getInstance().getLogsDir();
	}

	public String getProvider(int type) {
		String typeSuffix = "";

		if (type == ChartType.LINE) typeSuffix = "LineChart";
		else if (type == ChartType.BAR) typeSuffix = "BarChart";
		else if (type == ChartType.MAP) typeSuffix = "MapChart";
		else if (type == ChartType.BUBBLE) typeSuffix = "BubbleChart";
		else if (type == ChartType.TABLE) typeSuffix = "TableChart";
		else if (type == ChartType.TABLE) typeSuffix = "TableChart";
		else if (type == Format.XLS) typeSuffix = "XlsFormat";

		String key = PROVIDER_PREFIX + typeSuffix;

		return this.getValue(key);
	}

	public String getTemplatesDir(String language) {
		return Resources.getFullPath(String.format("/analytics/templates/%s", language));
	}

	public String getTemplatesWebDir(String language) {
		return this.getTemplatesDir(language) + "/web";
	}

	public String getTemplatesProvidersDir(String language) {
		return this.getTemplatesDir(language) + "/providers";
	}

	public String getEnterpriseLoginUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getEnterpriseLoginUrl();
	}

	public String getFederationLogoUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return this.getServletUrl() + "?op=loadbusinessmodelfile&path=images/" + monetConfiguration.getFederationLogoFilename();
	}

	public String getOfficeUrl() {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();
		return monetConfiguration.getUrl() + Strings.BAR45 + "office";
	}

}