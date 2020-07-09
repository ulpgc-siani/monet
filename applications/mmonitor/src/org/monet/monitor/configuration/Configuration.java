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

	private static String getValue(String sName) {
		return System.getenv(sName);
	}

	public static String getSlackToken() {
		return getValue("SLACK_TOKEN");
	}

	public static String getSlackChannel() {
		return getValue("SLACK_CHANNEL");
	}

	public static String getTeamsURL() {
		return getValue("TEAMS_URL");
	}

	public static String getSocksHost() {
	  String value = getValue("SOCKS_HOST");
	  if (value != null)
	    return value;
		return "";
	}

	public static String getSocksPort() {
    String value = getValue("SOCKS_PORT");
    if (value != null)
      return value;
    return "";
	}

  public static long getMinSizeGB() {
    return Long.parseLong(getValue("MIN_DISK_SIZE_GB"));
  }

  public static String getDisks() {
    return getValue("DISKS");
  }

  public static String getProject() {
    return getValue("PROJECT");
  }
}