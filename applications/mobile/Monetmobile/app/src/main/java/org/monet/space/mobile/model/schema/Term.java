package org.monet.space.mobile.model.schema;


public class Term {

  public String code;
  public String label;

  @Override
  public String toString() {
    return this.label;
  }
  
}
