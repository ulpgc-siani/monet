package org.monet.space.mobile.federation;

public class OAuthAPI {

    private final String accessTokenEndpoint;
    private final String authorizationEndpoint;
    private final String requestTokenEndpoint;

    public OAuthAPI(String baseUrl) {
        this.accessTokenEndpoint = baseUrl + "/accounts/tokens/access/";
        this.authorizationEndpoint = baseUrl + "/accounts/autoauthorization/";
        this.requestTokenEndpoint = baseUrl + "/accounts/tokens/request/";
    }

    public String getAccessTokenEndpoint() {
        return this.accessTokenEndpoint;
    }

    public String getAuthorizationUrl() {
        return this.authorizationEndpoint;
    }

    public String getRequestTokenEndpoint() {
        return this.requestTokenEndpoint;
    }

}
