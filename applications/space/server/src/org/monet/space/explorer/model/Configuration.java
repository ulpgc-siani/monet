package org.monet.space.explorer.model;

public class Configuration {
	private String domain;
	private String url;
	private String apiUrl;
	private String pushUrl;
	private String analyticsUrl;
	private String digitalSignatureUrl;
	private String imagesPath;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public String getAnalyticsUrl() {
		return analyticsUrl;
	}

	public void setAnalyticsUrl(String analyticsUrl) {
		this.analyticsUrl = analyticsUrl;
	}

	public String getDigitalSignatureUrl() {
		return digitalSignatureUrl;
	}

	public void setDigitalSignatureUrl(String digitalSignatureUrl) {
		this.digitalSignatureUrl = digitalSignatureUrl;
	}

	public String getImagesPath() {
		return imagesPath;
	}

	public void setImagesPath(String imagesPath) {
		this.imagesPath = imagesPath;
	}
}
