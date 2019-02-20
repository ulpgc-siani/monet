package org.monet.federation.accountoffice.core.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="userlist")
public class UserList extends ObjectList<User> {
  private static final long serialVersionUID = 1L;
  @ElementList(inline=true,required=false) private LinkedList<User> items = new LinkedList<User>();
  private HashMap<String, User> itemsNames = new HashMap<String, User>();
  
  public List<User> getAll() {
    return this.items;
  }
  
  public User getByName(String name) {
    return this.itemsNames.get(name);
  }
  
  public void add(User object) {
    this.items.add(object);
    this.itemsNames.put(object.getUsername(), object);
    this.totalCount++;
  }
  
  public void remove(String name) {
    for (User object : this.items) {
      if (object.getUsername().equals(name)) {
        this.items.remove(object);
        break;
      }
    }
    this.itemsNames.remove(name);
    this.totalCount--;
  }
}
