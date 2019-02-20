package org.monet.api.deploy.setupservice.impl.model;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

public class FederationManifest extends BaseObject {
  private String url;

  public FederationManifest() {
    this.url = "";
  }

  public String getBaseUrl() {
    return this.url;
  }

  public void setBaseUrl(String baseUrl) {
    this.url = baseUrl;
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag("", "federation-manifest");

    serializer.attribute("", "url", this.url);
    
    serializer.endTag("", "federation-manifest");
  }
  
}