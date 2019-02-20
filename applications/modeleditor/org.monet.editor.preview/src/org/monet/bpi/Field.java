package org.monet.bpi;


public interface Field<V> {

  public String getCode();
  
  public String getName();
  
  public void clear();
  
  public V get();

  public void set(V value);

}