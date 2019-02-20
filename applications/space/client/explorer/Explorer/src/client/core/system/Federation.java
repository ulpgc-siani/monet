package client.core.system;

import client.core.model.Space;

public class Federation implements Space.Federation {
	private String label;
	private String url;
	private String logoUrl;

	public Federation() {
	}

	public Federation(String label, String url, String logoUrl) {
		this.label = label;
		this.url = url;
		this.logoUrl = logoUrl;
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getLogoUrl() {
		return this.logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

}
