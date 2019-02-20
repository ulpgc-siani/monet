package org.monet.federation.accountoffice.core.model;

import java.net.URI;
import java.net.URISyntaxException;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "service")
public class Federation extends NamedObject {
  private String id;
  private @Attribute(name="uri") String uri;
  private @Element(name="label",required=false) String label;
  private boolean trusted;
  
  public Federation(@Attribute(name="name") String name, @Attribute(name="uri") String uri) {
    this.id = null;
    this.name = name;
    this.uri = uri;
    this.label = "";
    this.trusted = false;
  }
  
  public Federation() {
    this("", "");
  }
  
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  public URI getUri() {
    try {
      return new URI(this.uri);
    } catch (URISyntaxException e) {
      return null;
    }
  }
  
  public void setUri(URI uri) {
    this.uri = uri.toString();
  }
  
  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public boolean isTrusted() {
    return this.trusted;
  }

  public void setTrusted(boolean value) {
    this.trusted = value;
  }

}