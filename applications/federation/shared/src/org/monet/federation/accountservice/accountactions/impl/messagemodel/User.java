package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;

@Root(name = "account")
public class User {

  @Attribute(name = "id")
  private String    id;
  @Attribute(name = "username")
  private String    username;
  @Attribute(name = "fullname")
  private String    fullname;
  @Attribute(name = "lang", required = false)
  private String    lang    = "en";
  @Attribute(name = "email", required = false)
  private String    email;
  @Attribute(name = "is-human")
  private Boolean   isHuman = true;

  public User() {
  }

  public User(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getFullname() {
    return fullname;
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

  public boolean isHuman() {
    return isHuman;
  }

  public void setHuman(Boolean isHuman) {
    this.isHuman = isHuman;
  }

  public String serialize() throws Exception {
    StringWriter writer = new StringWriter();
    Persister persister = new Persister();
    persister.write(this, writer);
    return writer.toString();
  }

  public void fillFrom(User localUser) {
    this.id = this.id != null ? this.id : localUser.id;
    this.username = this.username != null ? this.username : localUser.username;
    this.fullname = this.fullname != null ? this.fullname : localUser.fullname;
    this.lang = this.lang != null ? this.lang : localUser.lang;
    this.email = this.email != null ? this.email : localUser.email;
    this.isHuman = this.isHuman != null ? this.isHuman : localUser.isHuman;
  }

}
