package org.monet.bpi;

public abstract class BusinessUnit {

  protected static BusinessUnit instance;
  
  public static String getName() {
    return instance.getNameImpl();
  }
  
  protected abstract String getNameImpl();
  
}
