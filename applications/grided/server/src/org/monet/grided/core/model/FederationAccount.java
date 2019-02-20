package org.monet.grided.core.model;


public class FederationAccount {
  private String userName;
  private String lang;
  private String email;
  
  public FederationAccount(String userName, String lang, String email) {
    this.userName = userName;
    this.lang = lang;
    this.email = email;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String user) {
    this.userName = user;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
   
}
