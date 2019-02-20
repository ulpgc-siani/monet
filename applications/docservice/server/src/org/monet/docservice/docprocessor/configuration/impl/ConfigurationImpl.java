package org.monet.docservice.docprocessor.configuration.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.Resources;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.configuration.ServerConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationImpl implements Configuration {

	private Logger logger;
	private ServerConfigurator serverConfigurator;
	private Properties properties;

	private static final long MONTH = 30 * 24 * 60 * 60 * 60;

	@Inject
	public ConfigurationImpl(Logger logger, ServerConfigurator serverConfigurator) {
		FileInputStream configurationFileStream = null;
		this.logger = logger;
		this.serverConfigurator = serverConfigurator;
		this.properties = new Properties();
		try {
			String configurationPath = this.serverConfigurator.getUserPath() + File.separator + "docservice.config";
			configurationFileStream = new FileInputStream(configurationPath);
			this.properties.loadFromXML(configurationFileStream);
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			throw new ApplicationException("Error loading configuration", e);
		} finally {
			StreamHelper.close(configurationFileStream);
		}
	}

	public String getPath(String key) {
		logger.debug("getPath(%s)", key);

		String value = this.properties.getProperty("Path." + key);
		if (value != null)
			return value;
		else
			throw new ApplicationException(String.format("Configuration error, key '%s' not found", key));
	}

	public String getJDBCDataSource() {
		logger.debug("getJDBCDatasource()");

		return this.getString(Configuration.JDBC_DATASOURCE);
	}

	public int getInt(String key) {
		logger.debug("getInt(%s)", key);

		String value = this.properties.getProperty(key);
		if (value != null)
			return Integer.parseInt(value);
		else
			throw new ApplicationException(String.format("Configuration error, key '%s' not found", key));
	}

	public String getString(String key) {
		logger.debug("getString(%s)", key);

		String value = this.properties.getProperty(key);
		if (value != null)
			return value;
		else
			throw new ApplicationException(String.format("Configuration error, key '%s' not found", key));
	}

	public boolean getBoolean(String key) {
		logger.debug("getBoolean(%s)", key);

		String value = this.properties.getProperty(key);
		if (value != null)
			return Boolean.parseBoolean(value);
		else
			throw new ApplicationException(String.format("Configuration error, key '%s' not found", key));
	}

	public int getSignConfig(String key) {
		logger.debug("getSignConfig(%s)", key);

		String value = this.properties.getProperty("Sign." + key);
		if (value != null)
			return Integer.parseInt(value);
		else
			throw new ApplicationException(String.format("Configuration error, key '%s' not found", key));
	}

	public String getApplicationDir() {
		return Resources.getFullPath("/");
	}

	public String getUpgradesDir() {
		return getApplicationDir() + "/upgrades";
	}

	@Override
	public int getDocumentPreviewsCacheSize() {

		String value = this.properties.getProperty(Configuration.DOCUMENT_PREVIEWS_CACHE_SIZE);
		if (value != null)
			return Integer.parseInt(value);

		return 30;
	}

	@Override
	public String getUserDataDir() {
		return serverConfigurator.getUserPath();
	}

	@Override
	public String getLogsDir() {
		return getUserDataDir() + "/logs";
	}

	@Override
	public String[] getDocumentDisks() {

		String value = this.properties.getProperty(Configuration.DOCUMENT_DISKS);
		if (value == null || value.isEmpty())
			return null;

		return value.split(",");
	}

	@Override
	public void check() {
		if (this.getDocumentDisks() == null)
			throw new ApplicationException(String.format("Configuration error, key '%s' not found", Configuration.DOCUMENT_DISKS));
	}

	private void createDocumentDirectory() {
		try {
			File file = new File(serverConfigurator.getUserPath() + "/documents");
			if (!file.exists())
				file.createNewFile();
		}
		catch (IOException exception) {
			logger.error("Could not create documents directory", exception);
		}
	}

}
