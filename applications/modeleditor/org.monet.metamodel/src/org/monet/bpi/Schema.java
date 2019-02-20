package org.monet.bpi;

import java.util.HashMap;


public abstract class Schema {

  public String toJson() {
    return null;
  }

  public String toString() {
    return null;
  }
  
  public abstract HashMap<String, Object> getAll();
  
  public abstract void set(String key, Object value); 

}
