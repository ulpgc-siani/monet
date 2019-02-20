package org.monet.bpi.types;


public class Link {

  public static enum LinkType { NODE, TASK }
  
  private String id;
  private LinkType type;
  private String label;
  
  public Link() {}
  
  public Link(String id, String label) {
    this.id = id;
    this.label = label;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  public String getId() {
    return id;
  }
  public void setLabel(String label) {
    this.label = label;
  }
  public String getLabel() {
    return label;
  }
  
  public Term toTerm() {
  	return new Term(id, label);
  }

  public LinkType getType() {
    return type;
  }

  public void setType(LinkType type) {
    this.type = type;
  }
  
}
