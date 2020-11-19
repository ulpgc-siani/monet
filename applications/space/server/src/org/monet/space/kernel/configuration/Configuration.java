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
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.SiteFiles;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.utils.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	private Properties properties;
	private static Configuration instance;

	public static final String CERTIFICATE_FILENAME = "MONET_CERTIFICATE_FILENAME";
	public static final String CERTIFICATE_PASSWORD = "MONET_CERTIFICATE_PASSWORD";
	public static final String JDBC_TYPE = "Jdbc.Type";
	public static final String JDBC_DATASOURCE = "Jdbc.DataSource";
	public static final String USER_DATA_DIR = "MONET_USER_DATA_DIR";
	public static final String COMPONENT_DOCUMENTS_MONET_URL = "MONET_COMPONENT_DOCUMENTS_MONET_URL";
	public static final String APPLICATION_DEFINITION_FILENAME = "MONET_APPLICATION_DEFINITION_FILENAME";
	public static final String COMPONENT_DEFINITION_FILENAME = "MONET_COMPONENT_DEFINITION_FILENAME";
	public static final String TRANSLATOR_DEFINITION_FILENAME = "MONET_TRANSLATOR_DEFINITION_FILENAME";
	public static final String BUSINESS_MODEL_PACKAGES_PATH = "MONET_BUSINESS_MODEL_PACKAGES_PATH";
	public static final String SIMPLE_FIELD_PATTERN = "MONET_SIMPLE_FIELD_PATTERN";
	public static final String MULTIPLE_FIELD_PATTERN = "MONET_MULTIPLE_FIELD_PATTERN";
	public static final String INDICATOR_NODE_LINK = "MONET_INDICATOR_NODE_LINK";
	public static final String ENCRIPT_PARAMETERS = "MONET_ENCRIPT_PARAMETERS";
	public static final String RUN_BUSINESS_UNIT = "MONET_RUN_BUSINESS_UNIT";
	public static final String MAIL_ADMIN_HOST = "MONET_MAIL_ADMIN_HOST";
	public static final String MAIL_ADMIN_FROM = "MONET_MAIL_ADMIN_FROM";
	public static final String MAIL_ADMIN_TO = "MONET_MAIL_ADMIN_TO";
	public static final String MAIL_ADMIN_USERNAME = "MONET_MAIL_ADMIN_USERNAME";
	public static final String MAIL_ADMIN_PASSWORD = "MONET_MAIL_ADMIN_PASSWORD";
	public static final String MAIL_ADMIN_PORT = "MONET_MAIL_ADMIN_PORT";
	public static final String MAIL_ADMIN_SECURE = "MONET_MAIL_ADMIN_SECURE";
	public static final String MAIL_ADMIN_TLS = "MONET_MAIL_ADMIN_TLS";
	public static final String USE_SSL = "MONET_USE_SSL";
	public static final String EXCLUDE_URL_PATH = "MONET_EXCLUDE_URL_PATH";
	public static final String ENTERPRISE_LOGIN_URL = "MONET_ENTERPRISE_LOGIN_URL";
	public static final String IS_PUSH_ENABLED = "MONET_IS_PUSH_ENABLED";
	public static final String MOBILE_FCM_SETTINGS_FILENAME = "MONET_MOBILE_FCM_SETTINGS_FILENAME";
	public static final String MOBILE_FCM_PROJECT_ID = "MONET_MOBILE_FCM_PROJECT_ID";
	public static final String GOOGLE_API_KEY = "MONET_GOOGLE_API_KEY";
	public static final String IS_DEBUG_MODE = "MONET_IS_DEBUG_MODE";
	public static final String DEBUG_PARAM = "MONET_DEBUG_PARAM";
	public static final String DATABASE_QUERY_EXECUTION_TIME_WARNING = "MONET_DATABASE_QUERY_EXECUTION_TIME_WARNING";
	public static final String SERVICES_REQUEST_MAX_ELAPSED_TIME = "MONET_SERVICES_REQUEST_MAX_ELAPSED_TIME";
	public static final String WORKQUEUE_PERIOD = "MONET_WORKQUEUE_PERIOD";
	public static final String SOURCE_UPGRADE_HOUR = "MONET_SOURCE_UPGRADE_HOUR";
    public static final String SOURCE_UPGRADE_LAUNCH_ON_START = "MONET_SOURCE_UPGRADE_LAUNCH_ON_START";
	public static final String DATAWAREHOUSE_DIR = "MONET_DATAWAREHOUSE_DIR";
	public static final String DATAWAREHOUSE_UPGRADE_HOUR = "MONET_DATAWAREHOUSE_UPGRADE_HOUR";
	public static final String DATAWAREHOUSE_UPGRADE_LAUNCH_ON_START = "MONET_DATAWAREHOUSE_UPGRADE_LAUNCH_ON_START";
	public static final String DATAWAREHOUSE_UPGRADE_DISABLED = "MONET_DATAWAREHOUSE_DISABLED";
	public static final String DEFAULT_APPLICATION = "MONET_DEFAULT_APPLICATION";

	private Configuration() {
		this.initialize();
	}

	private void initialize() {
		String sFilename = Configuration.getConfigurationFile();
		InputStream isConfigFileContent;

		if (!AgentFilesystem.existFile(sFilename)) {
			throw new SystemException("Configuration file not found", sFilename);
		}

		isConfigFileContent = AgentFilesystem.getInputStream(sFilename);

		this.properties = new Properties();
		try {
			this.properties.loadFromXML(isConfigFileContent);
			isConfigFileContent.close();
		} catch (IOException exception) {
			AgentLogger.getInstance().error(new Exception("Exception in 'Configuration' (" + sFilename + ") : " + exception.getMessage(), exception));
		}
	}

	public static String getConfigurationFile() {
		Context oContext = Context.getInstance();
		String sFrameworkName = oContext.getFrameworkName();
		String sFilename = System.getProperty("user.home") + "/." + sFrameworkName + "/configuration/monet" + SiteFiles.CONFIG;

		if (!AgentFilesystem.existFile(sFilename))
			sFilename = oContext.getFrameworkDir() + "/WEB-INF/" + SiteFiles.CONFIG;

		return sFilename;
	}

	public synchronized static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public void reload() {
		this.initialize();
	}

	public String getValue(String sName) {
		if (!this.properties.containsKey(sName))
			return Strings.EMPTY;
		return this.properties.get(sName).toString();
	}

	public String getDatabaseSource() {
		return this.getValue(JDBC_DATASOURCE);
	}

	public Boolean autoRun() {
		if (!this.properties.containsKey(RUN_BUSINESS_UNIT))
			return false;
		return (this.properties.get(RUN_BUSINESS_UNIT).toString().toLowerCase().equals("yes") || this.properties.get(RUN_BUSINESS_UNIT).toString().toLowerCase().equals("true"));
	}

	public String getMonetSplashLogoFilename() {
		return "monet.splash.png";
	}

	public String getModelLogoFilename() {
		return "model.logo.png";
	}

	public String getModelSplashLogoFilename() {
		return "model.splash.png";
	}

	public String getFederationLogoFilename() {
		return "organization.logo.png";
	}

	public String getFederationSplashLogoFilename() {
		return "organization.splash.png";
	}

	public String getUrl() {
		Long idThread = Thread.currentThread().getId();
		String sUseSSL = this.getValue(Configuration.USE_SSL);
		String sExcludePath = this.getValue(Configuration.EXCLUDE_URL_PATH);
		return Context.getInstance().getUrl(idThread, (sUseSSL.equals(Strings.TRUE)) ? true : false, (sExcludePath.equals(Strings.TRUE)) ? true : false);
	}

	public boolean useSSL() {
		String sUseSSL = this.getValue(Configuration.USE_SSL);
		return sUseSSL.equals(Strings.TRUE);
	}

	public String getPath() {
		Long idThread = Thread.currentThread().getId();
		String sExcludePath = this.getValue(Configuration.EXCLUDE_URL_PATH);
		if (sExcludePath.equals(Strings.TRUE))
			return "";
		return Context.getInstance().getPath(idThread);
	}

	public String getServletUrl() {
		return this.getUrl() + "/servlet";
	}

	public String getBackserviceServletPath() {
		return "/servlet/backservice";
	}

	public String getEnterpriseLoginUrl() {
		return this.getValue(Configuration.ENTERPRISE_LOGIN_URL);
	}

	public String getDomain() {
		Long idThread = Thread.currentThread().getId();
		return Context.getInstance().getDomain(idThread);
	}

	public String getFrameworkDir() {
		return Context.getInstance().getFrameworkDir();
	}

	public String getUserDataDir() {
		return this.getValue(Configuration.USER_DATA_DIR);
	}

	public String getConfigurationDir() {
		return this.getUserDataDir() + "/configuration";
	}

	public String getUserDataApplicationsDir() {
		return this.getUserDataDir() + "/applications";
	}

	public String getBusinessSpaceDir() {
		return this.getUserDataDir();
	}

	public String getBusinessModelZipLocation() {
		return this.getUserDataDir() + "/resources/businessmodel.zip";
	}

	public String getBusinessModelDir() {
		return this.getUserDataDir() + "/businessmodel";
	}

	public String getMonetSplashFile() {
		return this.getBusinessModelDir() + "/res/images/" + this.getMonetSplashLogoFilename();
	}

	public String getModelLogoFile() {
		return this.getBusinessModelDir() + "/res/images/" + this.getModelLogoFilename();
	}

	public String getModelSplashLogoFile() {
		return this.getBusinessModelDir() + "/res/images/" + this.getModelSplashLogoFilename();
	}

	public String getFederationLogoFile() {
		return this.getBusinessModelDir() + "/res/images/" + this.getFederationLogoFilename();
	}

	public String getFederationSplashLogoFile() {
		return this.getBusinessModelDir() + "/res/images/" + this.getFederationSplashLogoFilename();
	}

	public String getBusinessModelResourcesDir() {
		return this.getBusinessModelDir() + "/res";
	}

	public String getBusinessModelResourcesDataDir() {
		return this.getBusinessModelResourcesDir() + "/data";
	}

	public String getBusinessModelResourcesImagesDir() {
		return this.getBusinessModelResourcesDir() + "/images";
	}

	public String getBusinessModelResourcesLayoutsDir() {
		return this.getBusinessModelResourcesDir() + "/layouts";
	}

	public String getBusinessModelResourcesHelpDir() {
		return this.getBusinessModelResourcesDir() + "/help";
	}

	public String getBusinessModelResourcesHelpDirname() {
		return "res/help";
	}

	public String getBusinessModelDefinitionsDirname() {
		return "definitions";
	}

	public String getBusinessModelDefinitionsTemplatesDir() {
		String directory = this.getBusinessModelDir() + "/definitions.templates";
		if (!AgentFilesystem.existFile(directory))
			AgentFilesystem.createDir(directory);
		return directory;
	}

	public String getBusinessModelNodeExportersDir() {
		return this.getBusinessModelDir() + "/node.exporters";
	}

	public String getBusinessModelNodeImportersDir() {
		return this.getBusinessModelDir() + "/node.importers";
	}

	public String getBusinessModelCompilationDir() {
		return this.getBusinessModelDir();
	}

	public String getBusinessModelClassesDir() {
		return this.getBusinessModelCompilationDir() + "/classes";
	}

	public String getBusinessModelClassesDir(String businessModel) {
		return businessModel + "/classes";
	}

	public String getBusinessModelLibrariesClassesDir() {
		return this.getBusinessModelCompilationDir() + "/classes_libraries";
	}

	public String getBusinessModelLibrariesDir() {
		return this.getBusinessModelCompilationDir() + "/libs";
	}

	public String getBusinessModelPackagesPath() {
		return this.getValue(Configuration.BUSINESS_MODEL_PACKAGES_PATH);
	}

	public String getDatawareHouseDir() {
		String datawareHouseDir = this.getValue(DATAWAREHOUSE_DIR);
		return datawareHouseDir.isEmpty() ? this.getUserDataDir() + "/datawarehouse" : datawareHouseDir;
	}

	public String getThemeDir() {
		return this.getUserDataDir() + "/theme";
	}

	public String getThemeTemplatesDir(String language) {
		return this.getThemeDir() + "/templates/" + language;
	}

	public String getAppDataDir() {
		return this.getFrameworkDir() + "/WEB-INF/app_data";
	}

	public String getAppDataComponentsDir() {
		return this.getAppDataDir() + "/components";
	}

	public String getAppDataTranslatorsDir() {
		return this.getAppDataDir() + "/translators";
	}

	public Integer getPort() {
		Long idThread = Thread.currentThread().getId();
		return Context.getInstance().getPort(idThread);
	}

	public String getLogsDir() {
		return this.getUserDataDir() + "/logs";
	}

	public String getTempDir() {
		return this.getUserDataDir() + "/core/temp";
	}

	public Boolean encriptParameters() {
		if (!this.properties.containsKey(ENCRIPT_PARAMETERS))
			return false;
		return this.properties.get(ENCRIPT_PARAMETERS).equals(Strings.TRUE);
	}

	public boolean isPushEnable() {
		if (this.properties.containsKey(IS_PUSH_ENABLED)) {
			String pushEnableValue = this.properties.getProperty(IS_PUSH_ENABLED);
			return Boolean.parseBoolean(pushEnableValue);
		} else {
			return false;
		}
	}

	public String getMobileFCMSettingsFile() {
		return this.getValue(MOBILE_FCM_SETTINGS_FILENAME);
	}

	public String getMobileFCMProjectId() {
		return this.getValue(MOBILE_FCM_PROJECT_ID);
	}

	public String getGoogleApiKey() {
		return this.getValue(GOOGLE_API_KEY);
	}

	public boolean isDebugMode() {
		if (this.properties.containsKey(IS_DEBUG_MODE)) {
			String debugModeValue = this.properties.getProperty(IS_DEBUG_MODE);
			return Boolean.parseBoolean(debugModeValue);
		} else {
			return false;
		}
	}

	public String debugParam() {
		return this.properties.containsKey(DEBUG_PARAM) ? this.properties.getProperty(DEBUG_PARAM) : null;
	}

	public int getDatabaseQueryExecutionTimeWarning() {
		if (!this.properties.containsKey(DATABASE_QUERY_EXECUTION_TIME_WARNING))
			return 0;
		return Integer.valueOf((String) this.properties.get(DATABASE_QUERY_EXECUTION_TIME_WARNING));
	}

    public String getDatabaseDir() {
        return Resources.getFullPath("/kernel/database");
    }

    public String getUpgradesScriptsDir() {
        return Resources.getFullPath("/kernel/database/upgrades");
    }

    public int getServicesRequestMaxElapsedTime() {
		String configValue = this.getValue(Configuration.SERVICES_REQUEST_MAX_ELAPSED_TIME);
		if (configValue.isEmpty())
			return 30000;
		return Integer.valueOf(configValue);
	}

	public String getCertificateFilename() {
		return this.getUserDataDir() + "/certificates/" + this.getValue(CERTIFICATE_FILENAME);
	}

	public String getCertificatePassword() {
		return this.getValue(CERTIFICATE_PASSWORD);
	}

	public int getWorkQueuePeriod() {
		return Integer.parseInt(this.properties.getProperty(WORKQUEUE_PERIOD));
	}

	public int getSourceUpgradeHour() {
        String hourValue = this.properties.getProperty(SOURCE_UPGRADE_HOUR);

        if (hourValue == null)
            hourValue = "5";

		return Integer.parseInt(hourValue);
	}

    public boolean launchSourceUpgradeOnStart() {
        String launchOnStart = this.properties.getProperty(SOURCE_UPGRADE_LAUNCH_ON_START);

        if (launchOnStart == null)
            launchOnStart = "false";

        return launchOnStart.indexOf("true") != -1;
    }

    public int getDatawareHouseUpgradeHour() {
        String hourValue = this.properties.getProperty(DATAWAREHOUSE_UPGRADE_HOUR);

        if (hourValue == null)
            hourValue = "5";

        return Integer.parseInt(hourValue);
	}

	public boolean launchDatawareHouseUpgradeOnStart() {
        String launchOnStart = this.properties.getProperty(DATAWAREHOUSE_UPGRADE_LAUNCH_ON_START);

        if (launchOnStart == null)
            launchOnStart = "false";

        return launchOnStart.indexOf("true") != -1;
	}

	public boolean isDatawareHouseUpgradeDisabled() {
		String disabled = this.properties.getProperty(DATAWAREHOUSE_UPGRADE_DISABLED);

		if (disabled == null)
			disabled = "false";

		return disabled.indexOf("true") != -1;
	}

	public String getAssetName() {
		return "asset";
	}

	public String getDefaultApplication() {
		return getValue(DEFAULT_APPLICATION);
	}
}