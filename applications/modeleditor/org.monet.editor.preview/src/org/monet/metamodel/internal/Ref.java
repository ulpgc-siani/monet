package org.monet.metamodel.internal;

public class Ref {

  private String definition;
  private String value;
  
  public Ref(String value) {
    this.value = value;
  }
  
  public Ref(String value, String definition) {
    this.value = value;
    this.definition = definition;
  }

  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
  
  
  
}
