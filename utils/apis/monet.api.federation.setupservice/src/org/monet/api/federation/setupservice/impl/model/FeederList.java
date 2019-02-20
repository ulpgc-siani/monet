package org.monet.api.federation.setupservice.impl.model;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xmlpull.v1.XmlSerializer;

public class FeederList extends BaseModelList<Feeder> {

  public FeederList() {
  }
  
  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag("", "feederlist");
    serializer.attribute("", "totalCount", String.valueOf(this.items.size()));
    for(Feeder feeder : this.items.values())
      feeder.serializeToXML(serializer);
    serializer.endTag("", "feederlist");
  }

  @SuppressWarnings("unchecked")
  public void deserializeFromXML(Element serviceList) throws ParseException {
    List<Element> nodes;
    Iterator<Element> iterator;
    
    if (serviceList == null) return;
    
    nodes = serviceList.getChildren("feeder");
    iterator = nodes.iterator();

    this.clear();

    while (iterator.hasNext()) {
      Feeder feeder = new Feeder();
      feeder.deserializeFromXML(iterator.next());
      if (feeder.getId().equals("-1")) feeder.setId(String.valueOf(this.items.size()+1)); 
      this.add(feeder);
    }
    
  }
  
  public void deserializeFromXML(String content) throws JDOMException, IOException, ParseException {
    SAXBuilder builder = new SAXBuilder();
    StringReader reader;
    org.jdom.Document document;
    Element feederList;
    
    if (content.equals("")) return;
    
    content = content.trim();
    reader = new StringReader(content);
    
    document = builder.build(reader);
    feederList = document.getRootElement();
    this.deserializeFromXML(feederList);
  }

}

