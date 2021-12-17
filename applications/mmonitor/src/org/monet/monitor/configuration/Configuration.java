package org.monet.monitor.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	public static class ConfigurationException extends Exception {
		ConfigurationException(String cause) {
			super(cause);
		}
	}

	@SuppressWarnings("unused")
	public static String getCurrentDir() throws IOException {
		return new java.io.File(".").getCanonicalPath();
	}

  public static String getFileLog2() throws IOException { return getCurrentDir() +  "/log4j2.xml"; }

	public static String version() {
		String version = "";
		try {
			version = getInfo("version");
		} catch (IOException e) {
			System.err.println("I can't get version number.");
		}
		return version;
	}

	@SuppressWarnings("unused")
	public static String appName() {
		String appName = "";
		try {
			appName = getInfo("artifactId");
		} catch (IOException e) {
			System.err.println("I can't get application name.");
		}
		return appName;
	}

	public static String appCaption() {
		String appCaption = "";
		try {
			appCaption = getInfo("name");
		} catch (IOException e) {
			System.err.println("I can't get application caption.");
		}
		return appCaption;
	}

	private static String getInfo(String parameter) throws IOException {
		InputStream resourceAsStream = Configuration.class.getResourceAsStream("/version.properties");
		Properties prop = new Properties();
		prop.load(resourceAsStream);
		return prop.getProperty(parameter);
	}

	private static String getValue(String sName) throws ConfigurationException {
		String result;
		if (System.getenv(sName) == null)
			throw new ConfigurationException("ERROR: Environment parameter '" + sName + "' is null.");
		else
			result = System.getenv(sName);
		return result;
	}

	public static String getSocksHost() {
		try {
	  return getValue("SOCKS_HOST");
		} catch (ConfigurationException e) {
			return "";
		}
	}

	public static String getSocksPort() {
		try {
     return getValue("SOCKS_PORT");
		} catch (ConfigurationException e) {
			return "";
		}
	}

  public static long getMinSizeGB() {
    try {
		return Long.parseLong(getValue("MIN_DISK_SIZE_GB"));
		} catch (ConfigurationException e) {
			return 2;
		}
  }

  public static String getDisks() {
    try {
		return getValue("DISKS");
		} catch (ConfigurationException e) {
			return null;
		}
  }

  public static String getProject() {
    try {
  		return getValue("PROJECT");
		} catch (ConfigurationException e) {
			return null;
		}
  }

	public static String getRocketChatUrl() {
		try {
			return getValue("ROCKETCHAT_URL");
		} catch (ConfigurationException e) {
			return null;
		}
	}

}