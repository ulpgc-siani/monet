package org.monet.mocks.singlesignon.service.federation;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class FederationApi extends DefaultApi10a {
	private String url;

	public final static String AUTHORIZATION_PATH = "/accounts/authorization/";
	public final static String REQUEST_TOKEN_PATH = "/accounts/tokens/request/";
	public final static String ACCESS_TOKEN_PATH = "/accounts/tokens/access/";

	public String getUrl() {
		return this.url;
	}

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
	public String getAccessTokenEndpoint() {
		return this.getUrl() + ACCESS_TOKEN_PATH;
	}

	@Override
	public String getRequestTokenEndpoint() {
		return this.getUrl() + REQUEST_TOKEN_PATH;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return this.url + AUTHORIZATION_PATH + "?oauth_token=" + requestToken.getToken();
	}

}
