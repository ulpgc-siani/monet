package org.monet.mocks.businessunit.core;

public interface Configuration {

	public static interface Context {
		public String getUserPath();
		public String getAppPath();
	}

	public String getCertificateFilename();
	public String getCertificatePassword();
	public String getClientUnitUrl();
	public String getUnitName();
	public String getUnitUrl();
	public String getUnitCallbackUrl();

}
