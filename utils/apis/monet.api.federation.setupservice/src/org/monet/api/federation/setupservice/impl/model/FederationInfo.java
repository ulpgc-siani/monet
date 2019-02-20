package org.monet.api.federation.setupservice.impl.model;

import java.io.IOException;
import java.io.StringReader;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xmlpull.v1.XmlSerializer;

public class FederationInfo extends BaseObject {
  private String name;
  private String label;
  
  public FederationInfo() {
    this.name = "";
    this.label = "";
  }
  
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void deserializeFromXML(String content) throws JDOMException, IOException {
    SAXBuilder builder = new SAXBuilder();
    StringReader reader;
    org.jdom.Document document;
    Element node;

    if (content.isEmpty())
      return;
    
    while (!content.substring(content.length() - 1).equals(">"))
      content = content.substring(0, content.length() - 1);

    reader = new StringReader(content);

    document = builder.build(reader);
    node = document.getRootElement();
    
    this.deserializeFromXML(node);
  }
  
  public void deserializeFromXML(Element info) {
    if (info.getAttribute("name") != null) this.name = info.getAttributeValue("name");
    if (info.getAttribute("label") != null) this.label = info.getAttributeValue("label");
  }

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {

    serializer.startTag("", "federationinfo");

    serializer.attribute("", "name", this.name);
    serializer.attribute("", "label", this.label);
    
    serializer.endTag("", "federationinfo");
    
  }

}