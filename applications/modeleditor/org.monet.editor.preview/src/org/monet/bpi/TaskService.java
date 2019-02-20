package org.monet.bpi;
	
public abstract class TaskService {
  
  protected static TaskService instance;
  
  public static Task get(String taskId) {
    return instance.getImpl(taskId);
  }

  public static Task create(Class<? extends Task> taskClass) {
    return instance.createImpl(taskClass);
  }
  
  protected abstract Task getImpl(String taskId);
  
  protected abstract Task createImpl(Class<? extends Task> taskClass);
  
}