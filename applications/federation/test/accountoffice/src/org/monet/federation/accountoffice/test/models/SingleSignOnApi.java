package org.monet.federation.accountoffice.test.models;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class SingleSignOnApi extends DefaultApi10a {

  private static  String AUTHORIZE_URL = "http://localhost:8081/monet.federationservice/accounts/authorization/?oauth_token=%s";
  private static  String REQUEST_TOKEN_RESOURCE = "localhost:8081/monet.federationservice/accounts/tokens/request/";
  private static  String ACCESS_TOKEN_RESOURCE  = "localhost:8081/monet.federationservice/accounts/tokens/access/";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return "http://" + ACCESS_TOKEN_RESOURCE;
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "http://" + REQUEST_TOKEN_RESOURCE;
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }
  
  public static void getAccessTokenEndpoint(String accessTokenEndPoint) {
    ACCESS_TOKEN_RESOURCE  = accessTokenEndPoint;
  }

  public static void getRequestTokenEndpoint(String requestTokenEndPoint) {
    REQUEST_TOKEN_RESOURCE = requestTokenEndPoint;
  }

  public static void getAuthorizationUrl(String authorizeURL) {
    AUTHORIZE_URL = authorizeURL;
  }


  public static class SSLSingleSignOnApi extends SingleSignOnApi
  {
    @Override
    public String getAccessTokenEndpoint()
    {
      return "https://" + ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint()
    {
      return "https://" + REQUEST_TOKEN_RESOURCE;
    }
  }
}
