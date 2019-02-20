package org.monet.federation.accountoffice.core.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;

public class FederationUnit extends BusinessUnit {
  private static Serializer persister = new Persister();
  private @Element(name="servicelist") ServiceList serviceList = new ServiceList();
  private @Element(name="feederlist") FeederList feederList = new FeederList();
  
  public void setServiceList(ServiceList serviceList) {
    this.serviceList = serviceList;
  }

  public ServiceList getServiceList() {
    return this.serviceList;
  }
  
  public void setFeederList(FeederList feederList) {
    this.feederList = feederList;
  }

  public FeederList getFeederList() {
    return this.feederList;
  }
  
  public String serialize() throws Exception{
    StringWriter writer = new StringWriter();
    persister.write(this, writer);
    return writer.toString();
  }
  
}
