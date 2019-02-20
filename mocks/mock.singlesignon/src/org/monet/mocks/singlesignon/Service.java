package org.monet.mocks.singlesignon;

import org.scribe.model.Token;

public interface Service {
	public Token requestToken();
	String getAuthorizationUrl(Token requestToken);
	public Token getAccessToken(Token requestToken, String verifier);
	public boolean isLogged(Token accessToken, String address, String userAgent);
	public void logout(Token accessToken, String address, String userAgent);
}
