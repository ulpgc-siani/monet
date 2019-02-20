package org.monet.deployservice.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {

	private static Configuration instance;
	private static Properties properties;
	private static Logger logger;

	public static final String CONST_AppName = "deployservice";
	public static final String CONST_CONFIG_FILE = CONST_AppName + ".config";

	
	private static final String CONST_HomeDir = "." + CONST_AppName;
	
	private Configuration() {
		logger = Logger.getLogger(this.getClass());

		properties = new Properties();
		loadProperties();
	}

	public void dispose() {
		properties.clear();
		properties = null;
	}

	private void loadProperties() {
		logger.info("Loading config: " + getHomePath() + "/" + CONST_CONFIG_FILE);

		FileInputStream is = null;

		try {
			is = new FileInputStream(getHomePath() + "/" + CONST_CONFIG_FILE);
			properties.load(is);
			logger.info("The configuration is loaded successfully.");
		} catch (IOException exception) {
			logger.error("Unable to read configuration.");
		} finally {
			if (is != null)
	      try {
	        is.close();
        } catch (IOException e) {
    			logger.error("Unable to read configuration.");
        }			
		}		
	}

	public void reload() {
		loadProperties();
	}

	public static String getPath() {
		String path = "";
		try {
			path = new java.io.File(".").getCanonicalPath();
		} catch (Exception exception) {
			logger.error("Unable to read current path.");
		}
		return path;
	}

	public static String getHomePath() {
		return System.getProperty("user.home") + "/" + CONST_HomeDir;
	}

	public synchronized static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public static String getValue(String sName) {
		if (instance == null)
			getInstance();
		if (!properties.containsKey(sName))
			return "";
		return properties.getProperty(sName).trim();
	}

}
