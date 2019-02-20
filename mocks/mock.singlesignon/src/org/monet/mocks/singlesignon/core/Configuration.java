package org.monet.mocks.singlesignon.core;

public interface Configuration {

	public static interface Context {
		public String getUserPath();
		public String getAppPath();
	}

	public String getFederationUrl();
	public String getCertificateFilename();
	public String getCertificatePassword();
	public String getUnitName();
	public String getUnitUrl();
	public String getUnitCallbackUrl();
	public String getUnitKey();
	public String getUnitSecret();

}
