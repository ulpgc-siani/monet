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

@Root(name="federationunitlist")
public class FederationUnitList implements Serializable {
  private static final long serialVersionUID = 1L;
  private static Serializer persister = new Persister();
  
  @ElementList(inline=true,required=false) private List<FederationUnit> items = new ArrayList<FederationUnit>();
  @Attribute(name="totalCount") private int totalCount;
  
  public List<FederationUnit> getAll() {
    return this.items;
  }
  
  public void add(FederationUnit federationUnit) {
    this.items.add(federationUnit);
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
