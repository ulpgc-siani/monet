package org.monet.bpi;

public abstract class BPIClientService {

  protected static BPIClientService instance;
  
  public static BPIClientService getInstance() {
    return instance;
  }
  
  public abstract void redirectUserTo(BPIMonetLink monetLink);
  
  public abstract void sendMessageToUser(String message);
  
}
