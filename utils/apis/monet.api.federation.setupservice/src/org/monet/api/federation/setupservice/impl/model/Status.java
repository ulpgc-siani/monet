package org.monet.api.federation.setupservice.impl.model;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Status {
  private Date runningDate;

  public Status() {
    this.setRunningDate(null);
  }

  public boolean isRunning() {
    return this.runningDate != null;
  }

  public Date getRunningDate() {
    return this.runningDate;
  }

  public void setRunningDate(Date runningDate) {
    this.runningDate = runningDate;
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
    if (status.getAttribute("runningDate") != null) this.runningDate = new Date(Long.parseLong(status.getAttributeValue("runningDate")));
  }
  
}

