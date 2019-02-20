package org.monet.api.deploy.setupservice.impl.model;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

public class SpaceManifest extends BaseObject {
  private String url;
  private String federationName;
  private boolean requireDatawareHouse;

  public SpaceManifest() {
    this.url = "";
    this.setFederationName("");
    this.setRequireDatawareHouse(false);
  }
 
  public String getBaseUrl() {
    return this.url;
  }

  public void setBaseUrl(String baseUrl) {
    this.url = baseUrl;
  }

  public String getFederationName() {
    return this.federationName;
  }

  public void setFederationName(String federationName) {
    this.federationName = federationName;
  }

  public boolean isRequireDatawareHouse() {
    return this.requireDatawareHouse;
  }

  public void setRequireDatawareHouse(boolean requireDatawareHouse) {
    this.requireDatawareHouse = requireDatawareHouse;
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag("", "space-manifest");

    serializer.attribute("", "url", this.url);
    serializer.attribute("", "federationName", this.federationName);
    serializer.attribute("", "requireDatawareHouse", Boolean.toString(this.requireDatawareHouse));
    
    serializer.endTag("", "space-manifest");
  }

}