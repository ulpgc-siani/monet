package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Root(name="userlist")
public class UserList implements Serializable {
  private static final long serialVersionUID = 1L;
  private static Serializer persister = new Persister();
  
  @ElementList(inline=true,required=false) private List<User> items = new ArrayList<>();
  @Attribute(name="totalCount") private int totalCount;
  
  public List<User> getUsers() {
    return this.items;
  }
  
  public void addUser(User user) {
    this.items.add(user);
  }
  
  public List<User> getAll() {
    return this.items;
  }

  public int getTotalCount() {
    return this.totalCount;
  }
  
  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }
    
  public String serialize() throws Exception{
    StringWriter writer = new StringWriter();
    persister.write(this, writer);
    return writer.toString();
  }

}
