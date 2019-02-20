package org.monet.deployservicemanager.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

	private static Configuration instance;
	private static Properties properties;
	
	public static final String CONST_AppName = "deployservice_manager";
	public static final String CONST_AppCaption = "Deploy Service Manager";	
	public static final String CONST_CONFIG_FILE = CONST_AppName + ".config";

	public static final String VAR_ShowTerminal = "ShowTerminal";
	public static final String VAR_Domain = "Domain";
	public static final String VAR_Port = "Port";
	
	private static final String CONST_HomeDir = "." + CONST_AppName;
	
	private Configuration() {

		properties = new Properties();
		properties = loadProperties();
	}

	public void dispose() {
		properties.clear();
		properties = null;
	}

	public static String getHomePath() {
		return System.getProperty("user.home") + "/" + CONST_HomeDir;
	}
	
	public static String getConfigurationFile() {
		return getHomePath() + "/" + CONST_CONFIG_FILE;
	}
	
	private Properties loadProperties() {
		System.out.println("Loading config: " + getConfigurationFile());
		try {
			FileInputStream is = new FileInputStream(getConfigurationFile());
			properties.load(is);
			System.out.println("The configuration is loaded successfully.");
		} catch (IOException exception) {
			System.out.println("Unable to read configuration.");
		}
		return properties;
	}

	public void reload() {
		properties = loadProperties();
	}

	public synchronized static Configuration getInstance() {
		if (instance == null) {instance = new Configuration();}
		return instance;
	}

	public static String getValue(String sName, String sDefault) {
		if (instance == null)	getInstance();
		if (!properties.containsKey(sName))	return sDefault;
		return properties.getProperty(sName).trim();
	}
	
	public static String getDomain() {
		return getValue(VAR_Domain, "127.0.0.1");
	}
	
	public static String getPort() {
		return getValue(VAR_Port, "4323");		
	}
}
