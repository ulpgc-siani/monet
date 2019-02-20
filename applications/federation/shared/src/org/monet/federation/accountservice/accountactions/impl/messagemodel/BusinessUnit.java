package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class BusinessUnit {
  @Attribute(name="id",required=false) private String id;
  @Attribute(name="name",required=false) private String name;
  @Attribute(name="visible",required=false) private boolean visible;
  private String federationId;
  private URI uri;
  @Element(name="url") private String url;
  private String secret;
  private boolean enable;
  private String type;
  @Element(name="label",required=false) private String label;
  private String logoUrl;
  
  public static final class Type {
    public static final String MEMBER  = "member";
    public static final String PARTNER = "partner";
  }

  public BusinessUnit(){
    this.id = "";
    this.name = "";
    this.visible = true;
    this.federationId = "";
    this.type = Type.MEMBER;
    this.secret = "";
    this.uri = null;
    this.url = null;
    this.enable = false;
    this.label = "";
    this.logoUrl = "";
  }
  
  public String getId() { return id;}
  public String getName() { return name;}
  public boolean isVisible() { return visible;}
  public String getFederationId() { return federationId;}
  public String getType() { return this.type; }
  public String getSecret() { return secret; }
  public URI getUri() {
    if (this.uri == null && this.url != null) {
      try {
        this.uri = new URI(this.url);
      } catch (URISyntaxException e) {
      }
    }
    return this.uri;
  }
  public boolean getEnable() { return enable; }
  
  public void setId(String id) { this.id = id; }
  public void setName(String name) { this.name = name; }
  public void setVisible(boolean visible) { this.visible = visible; }
  public void setFederationId(String id) { this.federationId = id; }
  public void setType(String type) { this.type = type; }
  public void setSecret(String secret) { this.secret = secret; }
  public void setUri(URI uri) { 
    this.uri = uri;
     
    try {
      this.url = this.uri.toURL().toString();
    }
    catch (MalformedURLException ex) {
      this.url = "";
    }
  }
  public void setEnable(boolean enable) { this.enable = enable; }
  
  public String getLabel() { 
    if(label == null) return "No Label";
    return label;
  }
  
  public void setLabel(String label) {     
    this.label = label;
  }
  
  public String getLogoUrl() { 
    if(logoUrl == null) return "";
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {     
    this.logoUrl = logoUrl;
  }

  public boolean isMember() {
    return this.type.equals(Type.MEMBER);
  }

  public boolean isPartner() {
    return this.type.equals(Type.PARTNER);
  }

  public boolean isEnable() {
    return this.enable;
  }

}
