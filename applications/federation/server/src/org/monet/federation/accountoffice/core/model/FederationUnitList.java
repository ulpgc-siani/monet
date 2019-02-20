package org.monet.federation.accountoffice.core.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Root(name="federationunitlist")
public class FederationUnitList extends ObjectList<FederationUnit> {
  private static final long serialVersionUID = 1L;
  @ElementList(inline=true,required=false) private LinkedList<FederationUnit> items = new LinkedList<FederationUnit>();
  private HashMap<String, FederationUnit> itemsNames = new HashMap<String, FederationUnit>();
  
  public List<FederationUnit> getAll() {
    return this.items;
  }
  
  public FederationUnit getByName(String name) {
    return this.itemsNames.get(name);
  }
  
  public void add(FederationUnit object) {
    this.items.add(object);
    this.itemsNames.put(object.getName(), object);
    this.totalCount++;
  }
  
  public void remove(String name) {
    for (FederationUnit object : this.items) {
      if (object.getName().equals(name)) {
        this.items.remove(object);
        break;
      }
    }
    this.itemsNames.remove(name);
    this.totalCount--;
  }
}
