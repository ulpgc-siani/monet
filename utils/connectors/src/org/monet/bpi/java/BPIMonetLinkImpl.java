package org.monet.bpi.java;

import org.monet.bpi.BPIMonetLink;

public class BPIMonetLinkImpl extends BPIMonetLink {

  private String id;
  private Type type;
  
  public BPIMonetLinkImpl(Type type, String id) {
    this.setId(id);
    this.setType(type);
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }
  
  @Override
  public String toString() {
    switch (this.type) {
      case Node:
        return String.format("ml://node.%s", this.id);
      case Task:
        return String.format("ml://task.%s", this.id);
      default:
        return "";
    }
  }
  
}
