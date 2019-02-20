package org.monet.bpi;

public abstract class BPIBusinessUnit {

  protected static BPIBusinessUnit instance;
  
  public static BPIBusinessUnit getInstance() {
    return instance;
  }
  
  public abstract String getName();
  
}
