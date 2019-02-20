package org.monet.docservice.guice.factory;

public interface Factory<T,V> {

  public V create(T key);
  
}
