package org.monet.federation.accountoffice.core.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "servicelist")
public class ServiceList extends ObjectList<Service> {
  private static final long serialVersionUID = 1L;
  @ElementList(inline=true,required=false) private LinkedList<Service> items = new LinkedList<Service>();
  private HashMap<String, Service> itemsNames = new HashMap<String, Service>();
  
  public List<Service> getAll() {
    return this.items;
  }
  
  public Service getByName(String name) {
    return this.itemsNames.get(name);
  }
  
  public void add(Service object) {
    this.items.add(object);
    this.itemsNames.put(object.getName(), object);
    this.totalCount++;
  }
  
  public void remove(String name) {
    for (Service object : this.items) {
      if (object.getName().equals(name)) {
        this.items.remove(object);
        break;
      }
    }
    this.itemsNames.remove(name);
    this.totalCount--;
  }
}