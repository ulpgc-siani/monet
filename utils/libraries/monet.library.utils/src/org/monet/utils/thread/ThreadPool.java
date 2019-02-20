package org.monet.utils.thread;

public interface ThreadPool {
  public void execute(Runnable task);
  public void shutDown();
  public boolean awaitTermination(int timeout) throws Exception;
}
