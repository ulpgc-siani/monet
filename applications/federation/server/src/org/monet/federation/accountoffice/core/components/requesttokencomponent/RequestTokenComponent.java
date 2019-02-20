package org.monet.federation.accountoffice.core.components.requesttokencomponent;

public interface RequestTokenComponent {
  String getNewToken(String urlCallback, String unit);
  String getSecret(String token);
  boolean isAllowToken(String token);
  void allowToken(String token);
  String getUrlCallback(String token);
  String generateVerifier(String token);
  boolean checkVerifier(String token, String verifier);
  void setAccessToken(String requestToken, String accessToken);
  String getAccessToken(String requestToken, String verifier);
  void deleteToken(String requestToken);
  public String getVerifier(String requestToken);
  public String getUnit(String token);
}
