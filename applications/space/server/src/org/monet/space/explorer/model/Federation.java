package org.monet.space.explorer.model;

public class Federation {
	private String label;
	private String logoUrl;
	private String url;

	public Federation(String label, String logoUrl, String url) {
		this.label = label;
		this.logoUrl = logoUrl;
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public String getUrl() {
		return url;
	}
}
