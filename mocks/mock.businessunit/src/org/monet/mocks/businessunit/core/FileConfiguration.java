package org.monet.mocks.businessunit.core;

import com.google.inject.Inject;
import org.monet.mocks.businessunit.utils.StreamHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class FileConfiguration implements Configuration {
	private final Context context;
	private final Properties properties;

	private static final String CLIENT_UNIT_URL = "Client.Unit.Url";
	private static final String CERTIFICATE_FILENAME = "Certificate.Filename";
	private static final String CERTIFICATE_PASSWORD = "Certificate.Password";
	private static final String UNIT_NAME = "Unit.Name";
	private static final String UNIT_URL = "Unit.Url";
	private static final String UNIT_MAILBOX_URL = "Unit.MailboxUrl";

	public static interface Context {
		public String getUserPath();
		public String getAppPath();
	}

	@Inject
	public FileConfiguration(Context context) {
		this.context = context;

		FileInputStream configurationFileStream = null;

		this.properties = new Properties();
		try {
			String configurationPath = this.context.getAppPath() + File.separator + "application.config";
			configurationFileStream = new FileInputStream(configurationPath);
			this.properties.loadFromXML(configurationFileStream);
		} catch (Exception e) {
			throw new RuntimeException("Error loading configuration", e);
		} finally {
			StreamHelper.close(configurationFileStream);
		}
	}

	public String getCertificateFilename() {
		return properties.getProperty(CERTIFICATE_FILENAME);
	}

	public String getCertificatePassword() {
		return properties.getProperty(CERTIFICATE_PASSWORD);
	}

	public String getClientUnitUrl() {
		return properties.getProperty(CLIENT_UNIT_URL);
	}

	public String getUnitName() {
		return properties.getProperty(UNIT_NAME);
	}

	public String getUnitUrl() {
		return properties.getProperty(UNIT_URL);
	}

	public String getUnitCallbackUrl() {
		return properties.getProperty(UNIT_MAILBOX_URL);
	}

}
