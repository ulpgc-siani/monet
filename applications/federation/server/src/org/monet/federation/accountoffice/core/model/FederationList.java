package org.monet.federation.accountoffice.core.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="federationlist")
public class FederationList extends ObjectList<Federation> {
  private static final long serialVersionUID = 1L;
  @ElementList(inline=true,required=false) private LinkedList<Federation> items = new LinkedList<Federation>();
  private HashMap<String, Federation> itemsNames = new HashMap<String, Federation>();
  
  public List<Federation> getAll() {
    return this.items;
  }
  
  public Federation getByName(String name) {
    return this.itemsNames.get(name);
  }
  
  public void add(Federation object) {
    this.items.add(object);
    this.itemsNames.put(object.getName(), object);
    this.totalCount++;
  }
  
  public void remove(String name) {
    for (Federation object : this.items) {
      if (object.getName().equals(name)) {
        this.items.remove(object);
        break;
      }
    }
    this.itemsNames.remove(name);
    this.totalCount--;
  }
}
