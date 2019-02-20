package org.monet.bpi;


public interface Field<V> {

  public String getCode();
  
  public String getName();
  
  public String getLabel();
  
  public void clear();
  
  public V get();

  public void set(V value);
  
  public boolean isValid();

}