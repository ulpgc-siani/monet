package org.monet.federation.accountoffice.core.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Root(name="businessunitlist")
public class BusinessUnitList extends ObjectList<BusinessUnit> {
  private static final long serialVersionUID = 1L;
  @ElementList(inline=true,required=false) private LinkedList<BusinessUnit> items = new LinkedList<BusinessUnit>();
  private HashMap<String, BusinessUnit> itemsNames = new HashMap<String, BusinessUnit>();

  public List<BusinessUnit> getAll() {
    return this.items;
  }

  public BusinessUnit getByName(String name) {
    return this.itemsNames.get(name);
  }

  public void add(BusinessUnit object) {
    this.items.add(object);
    this.itemsNames.put(object.getName(), object);
    this.totalCount++;
  }

  public void remove(String name) {
    for (BusinessUnit object : this.items) {
      if (object.getName().equals(name)) {
        this.items.remove(object);
        break;
      }
    }
    this.itemsNames.remove(name);
    this.totalCount--;
  }
}
