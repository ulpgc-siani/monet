package org.monet.api.deploy.setupservice.impl.model;

import java.io.IOException;
import java.io.StringReader;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xmlpull.v1.XmlSerializer;

public class Ticket extends BaseObject {
  private String id;

  public Ticket() {
    this.id = "";
  }
 
  public String getId() {
    return this.id;
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
  
  public void deserializeFromXML(Element status) {
    if (status.getAttribute("id") != null) this.id = status.getAttributeValue("id");
  }

  @Override
  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
  }

}