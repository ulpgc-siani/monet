package org.monet.mocks.singlesignon;

import org.apache.commons.codec.binary.Base64;
import org.monet.api.federation.setupservice.impl.model.FederationSocketInfo;
import org.monet.mocks.singlesignon.core.Configuration;
import org.monet.mocks.singlesignon.service.federation.Federation;
import org.monet.mocks.singlesignon.service.federation.FederationApi;
import org.monet.mocks.singlesignon.service.federation.FederationConnector;
import org.monet.mocks.singlesignon.service.federation.FederationSetupLayer;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.security.MessageDigest;

public class SingleSignOnService implements Service {
	private final Configuration configuration;
	private final FederationSetupLayer federationSetupLayer;
	private FederationConnector connector;
	private OAuthService oauthService;

	private static final String DEFAULT_UNIT_INFO = "<info><label>Label</label><logo-url></logo-url></info>";

	public SingleSignOnService(Configuration configuration, FederationSetupLayer federationSetupLayer) {
		this.configuration = configuration;
		this.federationSetupLayer = federationSetupLayer;
		buildFederationConnector();
		buildOAuthService();
	}

	@Override
	public Token requestToken() {
		return this.oauthService.getRequestToken();
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return this.oauthService.getAuthorizationUrl(requestToken);
	}

	@Override
	public Token getAccessToken(Token requestToken, String verifier) {

		if (requestToken == null)
			return null;

		try {
			return oauthService.getAccessToken(requestToken, new Verifier(verifier));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public boolean isLogged(Token accessToken, String address, String userAgent) {
		Boolean result;
		String verifier = calculateVerifier(address, userAgent);

		try {
			String token = null;

			if (accessToken != null)
				token = accessToken.getToken();

			if (this.connector == null)
				return false;

			result = this.connector.isLogged(token, verifier);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return result;
	}

	@Override
	public void logout(Token accessToken, String address, String userAgent) {
		try {
			this.connector.logout(accessToken.getToken(), calculateVerifier(address, userAgent));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildFederationConnector() {
		Federation federation = new Federation(configuration.getFederationUrl(), configuration.getUnitSecret());
		FederationSocketInfo socketInfo = federationSetupLayer.getSocketInfo(federation);

		try {
			this.connector = new FederationConnector(socketInfo.getHost(), socketInfo.getPort(), configuration.getUnitName(), DEFAULT_UNIT_INFO);
			this.connector.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildOAuthService() {
		String callback = configuration.getUnitCallbackUrl();
		FederationApi federationApi = new FederationApi();
		federationApi.setUrl(configuration.getFederationUrl());

		ServiceBuilder builder = new ServiceBuilder().provider(federationApi).apiKey(configuration.getUnitKey()).apiSecret(configuration.getUnitSecret());
		if (callback != null)
			builder.callback(callback);

		this.oauthService = builder.build();
	}

	private String calculateVerifier(String address, String userAgent) {
		String verifier = address + userAgent;

		MessageDigest digest;
		try {
			digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(verifier.getBytes());
			byte[] hash = digest.digest();
			verifier = Base64.encodeBase64String(hash);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return verifier;
	}

}
