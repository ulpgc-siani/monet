package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "account")
public class FederationAccount {
  @Attribute(name = "id")
  private String id;
  @Attribute(name = "username")
  private String  username;
  @Attribute(name = "fullname")
  private String  fullname;
  @Attribute(name = "lang", required = false)
  private String  lang;
  @Attribute(name = "email", required = false)
  private String  email;

  public FederationAccount() {
  }

  public FederationAccount(String id, String username, String fullname, String lang, String email) {
    this.id = id;
    this.username = username;
    this.fullname = fullname;
    this.lang = lang;
    this.email = email;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return this.username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getFullname() {
    return this.fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
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
