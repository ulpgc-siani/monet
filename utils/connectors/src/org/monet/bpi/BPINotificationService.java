package org.monet.bpi;

public abstract class BPINotificationService {

  protected static BPINotificationService instance;
  
  public static BPINotificationService getInstance() {
    return instance;
  }
  
  public abstract void createForAll(String label, String icon, BPIMonetLink target);
  public abstract void createForTeam(String taskId, String label, String icon, BPIMonetLink target);
  public abstract void createForUser(String userId, String label, String icon, BPIMonetLink target);
  
}
