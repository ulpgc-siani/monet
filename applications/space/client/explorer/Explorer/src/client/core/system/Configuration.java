package client.core.system;

import client.core.model.Space;

public class Configuration implements Space.Configuration {
	private String domain;
	private String url;
	private String apiUrl;
	private String pushUrl;
	private String analyticsUrl;
	private String digitalSignatureUrl;
	private String imagesPath;

	@Override
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getApiUrl() {
		return this.apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	@Override
	public String getPushUrl() {
		return this.pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	@Override
	public String getAnalyticsUrl() {
		return this.analyticsUrl;
	}

	public void setAnalyticsUrl(String analyticsUrl) {
		this.analyticsUrl = analyticsUrl;
	}

	@Override
	public String getDigitalSignatureUrl() {
		return this.digitalSignatureUrl;
	}

	public void setDigitalSignatureUrl(String digitalSignatureUrl) {
		this.digitalSignatureUrl = digitalSignatureUrl;
	}

	@Override
	public String getImagesPath() {
		return this.imagesPath;
	}

	public void setImagesPath(String imagesPath) {
		this.imagesPath = imagesPath;
	}

}
