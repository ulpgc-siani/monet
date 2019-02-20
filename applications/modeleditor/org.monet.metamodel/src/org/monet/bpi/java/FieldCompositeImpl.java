package org.monet.bpi.java;

import java.util.List;

import org.monet.bpi.Field;
import org.monet.bpi.FieldComposite;

public class FieldCompositeImpl extends FieldImpl<List<Field<?>>> implements FieldComposite {
  
  public FieldCompositeImpl() {}
  
  protected <T, F extends Field<V>, V> T getField(String definitionName, String name) {
    return (T) null;
  }
  
  @Override
  public List<Field<?>> get() { return null; }

  @Override
  public void set(List<Field<?>> value) { }
  
  public <T, F extends Field<V>, V> T getField(String key) {
    return null;
  }

  @Override
  public void clear() { }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public void setEnabled(boolean value) {
  }

  @Override
  public boolean isExtended() {
    return false;
  }

  @Override
  public void setExtended(boolean value) {
  }

}