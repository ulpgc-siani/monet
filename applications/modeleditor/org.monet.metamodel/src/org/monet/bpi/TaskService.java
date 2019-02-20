package org.monet.bpi;
	
public abstract class TaskService {
  
  protected static TaskService instance;
  
  public static Task get(String id) {
    return instance.getImpl(id);
  }

  public static Task create(Class<? extends Task> clazz) {
    return instance.createImpl(clazz);
  }
  
  protected abstract Task getImpl(String id);
  
  protected abstract Task createImpl(Class<? extends Task> clazz);

}