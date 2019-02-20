package org.monet.utils.thread.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.monet.utils.thread.ThreadPool;
import org.monet.utils.thread.guice.annotation.ThreadPoolConfiguration.CorePoolSize;
import org.monet.utils.thread.guice.annotation.ThreadPoolConfiguration.KeepAlive;
import org.monet.utils.thread.guice.annotation.ThreadPoolConfiguration.MaxPoolSize;

import com.google.inject.Inject;

public class ThreadPoolImpl implements ThreadPool{

  private ThreadPoolExecutor threadPool = null;
  private final ArrayBlockingQueue<Runnable> queue;
  @Inject
  public ThreadPoolImpl (@CorePoolSize int corePoolSize, @MaxPoolSize int maxPoolSize, @KeepAlive int keepAlive){
    this.queue = new ArrayBlockingQueue<Runnable>(maxPoolSize);
    this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, TimeUnit.SECONDS, queue);
  }
  
  @Override
  public void execute(Runnable task) {
    this.threadPool.execute(task);
  }

  @Override
  public void shutDown() {
    this.threadPool.shutdown();    
  }

  @Override
  public boolean awaitTermination(int timeout) throws Exception {
    return this.threadPool.awaitTermination(timeout, TimeUnit.SECONDS);
  }

}
