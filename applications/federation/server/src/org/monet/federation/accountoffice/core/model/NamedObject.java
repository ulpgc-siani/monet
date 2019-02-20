package org.monet.federation.accountoffice.core.model;

import org.simpleframework.xml.Attribute;

public abstract class NamedObject {
  protected @Attribute(name="name",required=false) String name;
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
}
