package org.monet.grided.control.actions;

public interface Factory<T,V> {

  public V create(T key);
  
}
