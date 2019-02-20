package org.monet.bpi;
  
public abstract class BPITaskService {
  
  protected static BPITaskService instance;
  
  public static BPITaskService getInstance() {
    return instance;
  }
  
  public abstract <T extends BPITask<?,?,?,?>> T get(String taskId);

  public abstract <T extends BPITask<?,?,?,?>> T add(Class<T> taskClass);
  
  public abstract <T extends BPITask<?,?,?,?>> T add(String name);

}