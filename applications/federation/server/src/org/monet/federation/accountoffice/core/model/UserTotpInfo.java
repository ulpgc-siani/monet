package org.monet.federation.accountoffice.core.model;

public class UserTotpInfo {
  private String userId;
  private String secret;
  private boolean enabled;
  private Long lastUsedCounter;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Long getLastUsedCounter() {
    return lastUsedCounter;
  }

  public void setLastUsedCounter(Long lastUsedCounter) {
    this.lastUsedCounter = lastUsedCounter;
  }
}
