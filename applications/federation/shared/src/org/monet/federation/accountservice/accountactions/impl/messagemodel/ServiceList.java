package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "servicelist")
public class ServiceList {
  private @ElementList(inline=true,required=false) ArrayList<Service> services;

  public ServiceList() {
  }
  
  public ArrayList<Service> getAll() {
    
    if (this.services == null)
      return new ArrayList<Service>();
    
    return this.services;
  }
}