package org.monet.federation.accountoffice.core.model;

import java.util.Date;

public class Session {
  private String token;
  private String username;
  private String verifier;
  private String lang;
  private boolean rememberMe;
  private boolean isMobile;
  private Date lastUse;
  private String space;
  private String node;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getVerifier() {
    return verifier;
  }

  public void setVerifier(String verifier) {
    this.verifier = verifier;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public boolean isRememberMe() {
    return rememberMe;
  }

  public void setRememberMe(boolean rememberMe) {
    this.rememberMe = rememberMe;
  }

  public boolean isMobile() {
    return isMobile;
  }

  public void setMobile(boolean isMobile) {
    this.isMobile = isMobile;
  }

  public Date getLastUse() {
    return lastUse;
  }

  public void setLastUse(Date lastUse) {
    this.lastUse = lastUse;
  }

  public String getSpace() {
    return space;
  }

  public void setSpace(String space) {
    this.space = space;
  }

  public String getNode() {
    return node;
  }

  public void setNode(String node) {
    this.node = node;
  }
}