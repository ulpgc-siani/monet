package org.monet.bpi;

public abstract class ClientService {

  protected static ClientService instance;
  
  public static void redirectUserTo(MonetLink monetLink) {
    instance.redirectUserToImpl(monetLink);
  }
  
  protected abstract void redirectUserToImpl(MonetLink monetLink);
  
}
