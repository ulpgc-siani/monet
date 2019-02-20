package org.monet.api.federation.setupservice.impl.model;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

public class Federation extends BaseObject {
  private String name;
  private String uri;
  private String label;
  
  public Federation(String name, String uri, String label) {
    this.name = name;
    this.uri = uri;
    this.label = label;
  }
  
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getUri() {
    return this.uri;
  }
  
  public void setUri(String uri) {
    this.uri = uri;
  }
  
  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {

    serializer.startTag("", "federation");

    serializer.attribute("", "name", this.name);
    serializer.attribute("", "uri", this.uri);
    
    serializer.startTag("", "label");
    serializer.text(this.label);
    serializer.endTag("", "label");
    
    serializer.endTag("", "federation");
    
  }

}