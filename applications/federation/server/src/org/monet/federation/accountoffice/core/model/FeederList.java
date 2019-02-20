package org.monet.federation.accountoffice.core.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="feederlist")
public class FeederList extends ObjectList<Feeder> {
  private static final long serialVersionUID = 1L;
  @ElementList(inline=true,required=false) private LinkedList<Feeder> items = new LinkedList<Feeder>();
  private HashMap<String, Feeder> itemsNames = new HashMap<String, Feeder>();
  
  public List<Feeder> getAll() {
    return this.items;
  }
  
  public Feeder getByName(String name) {
    return this.itemsNames.get(name);
  }
  
  public void add(Feeder object) {
    this.items.add(object);
    this.itemsNames.put(object.getName(), object);
    this.totalCount++;
  }
  
  public void remove(String name) {
    for (Feeder object : this.items) {
      if (object.getName().equals(name)) {
        this.items.remove(object);
        break;
      }
    }
    this.itemsNames.remove(name);
    this.totalCount--;
  }
}
