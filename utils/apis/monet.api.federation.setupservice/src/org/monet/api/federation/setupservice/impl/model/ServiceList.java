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

public class ServiceList extends BaseModelList<Service> {

  public ServiceList() {
  }
  
  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag("", "servicelist");
    serializer.attribute("", "totalCount", String.valueOf(this.items.size()));
    for(Service service : this.items.values())
      service.serializeToXML(serializer);
    serializer.endTag("", "servicelist");
  }

  @SuppressWarnings("unchecked")
  public void deserializeFromXML(Element serviceList) throws ParseException {
    List<Element> nodes;
    Iterator<Element> iterator;
    
    if (serviceList == null) return;
    
    nodes = serviceList.getChildren("service");
    iterator = nodes.iterator();

    this.clear();

    while (iterator.hasNext()) {
      Service service = new Service();
      service.deserializeFromXML(iterator.next());
      if (service.getId().equals("-1")) service.setId(String.valueOf(this.items.size()+1)); 
      this.add(service);
    }
    
  }
  
  public void deserializeFromXML(String content) throws JDOMException, IOException, ParseException {
    SAXBuilder builder = new SAXBuilder();
    StringReader reader;
    org.jdom.Document document;
    Element serviceList;
    
    if (content.equals("")) return;
    
    content = content.trim();
    reader = new StringReader(content);
    
    document = builder.build(reader);
    serviceList = document.getRootElement();
    this.deserializeFromXML(serviceList);
  }

}

