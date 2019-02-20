package org.monet.federation.accountoffice.core.model;

import java.io.Serializable;
import java.io.StringWriter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public abstract class ObjectList<T extends NamedObject> implements Serializable {
  private static final long serialVersionUID = 1L;
  private static Serializer persister = new Persister();
  
  @Attribute(name="totalCount",required=false) protected int totalCount;
  
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
