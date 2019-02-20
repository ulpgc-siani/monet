package org.monet.federation.accountoffice.core.components.requesttokencomponent.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.codec.binary.Base64;
import org.monet.federation.accountoffice.core.components.requesttokencomponent.RequestTokenComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class RequestTokenImpl implements RequestTokenComponent {

  private Logger logger;
  private Map<String,Value> tokens;
  private static SecureRandom prng = new SecureRandom();
  
  @Inject
  public RequestTokenImpl (Logger logger, Configuration configuration){
    this.logger = logger;
    this.tokens = new HashMap<String,Value>();
  }
  
  @Override
  public String getNewToken(String urlCallback, String unit) {
    String token = String.valueOf(UUID.randomUUID());
    this.logger.debug("getNewToken() = %s", token); 
    Value value = new Value(createSecret(token), false, urlCallback, unit);
    tokens.put(token, value);
    return token;
  }

  public String createSecret(String token) {
    MessageDigest sha;
    try {
      String randomNum = Integer.toString(prng.nextInt());
      sha = MessageDigest.getInstance("SHA-1");
      byte[] result = sha.digest( (randomNum + token).getBytes() );
      
      return Base64.encodeBase64URLSafeString(result);
    } catch (NoSuchAlgorithmException e) {
      logger.error(e.getMessage(),e);
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String getSecret(String token) {
    Value tokenData = tokens.get(token);
    if(tokenData == null) return null;
    return tokens.get(token).getSecret();
  }
  
  @Override
  public void allowToken(String token) {
    Value tokenData = tokens.get(token);
    if(tokenData == null) return;
    tokens.get(token).setAllow(true);
  }
  

  @Override
  public boolean isAllowToken(String token) {
    Value tokenData = tokens.get(token);
    if(tokenData == null) return false;
    return tokens.get(token).isAllow();
  }
  
  @Override
  public String getUrlCallback(String token) {
    Value tokenData = tokens.get(token);
    if(tokenData == null) return null;
    return tokens.get(token).getUrlCallback();
  }
  
  @Override
  public String getUnit(String token) {
    Value tokenData = tokens.get(token);
    if(tokenData == null) return null;
    return tokens.get(token).getUnit();
  }


  @Override
  public String generateVerifier(String token) {
    MessageDigest sha;
    try {
      String randomNum = Integer.toString(prng.nextInt());
      sha = MessageDigest.getInstance("SHA-1");
      byte[] result =  sha.digest( (randomNum + token).getBytes() );
      String verifier = Base64.encodeBase64String(result);
      Value value = this.tokens.get(token);
      value.setVerifier(verifier);
      return verifier;
    } catch (NoSuchAlgorithmException e) {
      logger.error(e.getMessage(),e);
      e.printStackTrace();
    }
    return null;
  }
  
  @Override
  public boolean checkVerifier(String token, String verifier) {
    Value tokenData = tokens.get(token);
    if(tokenData == null) return false;
    return this.tokens.get(token).getVerifier().equals(verifier);
  }
  
  @Override
  public void setAccessToken(String requestToken, String accessToken) {
    Value value = this.tokens.get(requestToken);
    if(value != null) value.setAccessToken(accessToken);
  }
  
  @Override
  public String getAccessToken(String requestToken, String verifier) {
    Value tokenData = tokens.get(requestToken);
    if(tokenData == null) return null;
    return this.tokens.get(requestToken).getAccessToken();
  }
  
  @Override
  public String getVerifier(String requestToken) {
    Value tokenData = tokens.get(requestToken);
    if(tokenData == null) return null;
    return this.tokens.get(requestToken).getVerifier();
  }


  @Override
  public void deleteToken(String requestToken) {
    this.tokens.remove(requestToken);
  }
  
  
  private class Value{
    private String secret;
    private boolean allow;
    private String urlCallback;
    private String verifier;
    private String accessToken;
    private String unit;
    
    public Value(String secret, boolean allow, String urlCallback, String unit) {
      this.secret = secret;
      this.allow = allow;
      this.urlCallback = urlCallback;
      this.unit = unit;
    }
    
    public String getSecret() {
      return secret;
    }
    
    public boolean isAllow() {
      return allow;
    }
    
    public void setAllow(boolean allow) {
      this.allow = allow;
    }

    public String getUrlCallback() {
      return urlCallback;
    }

    public String getVerifier() {
      return verifier;
    }

    public void setVerifier(String verifier) {
      this.verifier = verifier;
    }

    public String getAccessToken() {
      return accessToken;
    }

    public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
    }

    public String getUnit() {
      return unit;
    }
  }
}
