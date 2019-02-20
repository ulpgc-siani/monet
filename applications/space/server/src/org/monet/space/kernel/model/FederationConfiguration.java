package org.monet.space.kernel.model;

public class FederationConfiguration {
	private String name;
	private String label;
	private String logoUrl;
	private String server;
	private String basePath;
	private String port;
	private String socketPort;
	private String secret;
	private String key;
	private boolean ssl;

	public FederationConfiguration() {
		this.name = "";
		this.label = "";
		this.logoUrl = "";
		this.server = "";
		this.basePath = "";
		this.port = "";
		this.socketPort = "";
		this.secret = "";
		this.key = "";
		this.ssl = false;
	}

	public String getName() {
		return this.name;
	}

	public String getLabel() {
		return this.label;
	}

	public String getLogoUrl() {
		return this.logoUrl;
	}

	public String getServer() {
		return this.server;
	}

	public String getBasePath() {
		return this.basePath;
	}

	public String getPort() {
		return this.port;
	}

	public String getSocketPort() {
		return this.socketPort;
	}

	public String getSecret() {
		return this.secret;
	}

	public String getKey() {
		return this.key;
	}

	public boolean isSecure() {
		return this.ssl;
	}

	public boolean equals(FederationConfiguration federation) {
		if (!this.name.equals(federation.getName())) return false;
		if (!this.label.equals(federation.getLabel())) return false;
		if (!this.logoUrl.equals(federation.getLogoUrl())) return false;
		if (!this.server.equals(federation.getServer())) return false;
		if (!this.basePath.equals(federation.getBasePath())) return false;
		if (!this.port.equals(federation.getPort())) return false;
		if (!this.socketPort.equals(federation.getSocketPort())) return false;
		if (!this.secret.equals(federation.getSecret())) return false;
		if (!this.key.equals(federation.getKey())) return false;
		if (this.ssl != federation.isSecure()) return false;
		return true;
	}
}