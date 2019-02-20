package org.monet.bpi.java;

import org.monet.bpi.Field;
import org.monet.bpi.FieldSection;

public class FieldSectionImpl extends FieldImpl<Void> implements FieldSection {
  
  public FieldSectionImpl() {}
  
  protected <T, F extends Field<V>, V> T getField(String definitionName, String name) {
    return (T) null;
  }
  
  @Override
  public Void get() { return null; }

  @Override
  public void set(Void value) { }

  @Override
  public void clear() { }

  @Override
  public boolean isEnabled() {
    return false;
  }

}