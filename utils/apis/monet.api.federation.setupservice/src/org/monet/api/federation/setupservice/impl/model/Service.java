package org.monet.api.federation.setupservice.impl.model;

import java.io.IOException;
import java.io.StringReader;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xmlpull.v1.XmlSerializer;

public class Service extends BaseObject {
  private String name;
  private String label;
  private String ontology;

  public Service(String name, String label, String ontology) {
    this.name = name;
    this.label = label;
    this.ontology = ontology;
  }
  
  public Service() {
    this("", "", "");
  }
  
  public String getName() {
    return this.name;
  }

  public String getLabel() {
    return this.label;
  }
  
  public String getOntology() {
    return this.ontology;
  }
  

  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {

    serializer.startTag("", "service");

    serializer.attribute("", "name", this.name);
    serializer.attribute("", "ontology", this.getOntology());
    
    serializer.startTag("", "label");
    serializer.text(this.label);
    serializer.endTag("", "label");
    
    serializer.endTag("", "service");
    
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
  
  public void deserializeFromXML(Element service) {
    if (service.getAttribute("name") != null) this.name = service.getAttributeValue("name");
    if (service.getAttribute("label") != null) this.label = service.getAttributeValue("label");
    if (service.getAttribute("ontology") != null) this.ontology = service.getAttributeValue("ontology");
  }

}

