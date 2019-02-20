package org.monet.docservice.core.log.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.monet.docservice.core.log.EventLog;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.guice.InjectorFactory;

public class DatabaseAppender extends AppenderSkeleton {
    private static final int CACHE_TIMER = 5000;
    private static final BlockingQueue<LoggingEvent> loggingEventQueue = new LinkedBlockingDeque<LoggingEvent>();
    private static DatabaseAppender instance;
    private static Timer timer;
    private static boolean stop = false;
    private static Thread thread;
    
    private ArrayList<EventLog> cache = new ArrayList<EventLog>();
    private Repository repository;
    
    public DatabaseAppender() {
        super();
        instance = this;
    }

    @Override
    protected void append(LoggingEvent event) {
        loggingEventQueue.add(event);
    }

    public boolean requiresLayout() {
        return false;
    }

    private void processEvent(LoggingEvent loggingEvent) {
      EventLog eventLog = new EventLog();
      if(loggingEvent.getThrowableInformation() != null) {
        eventLog.setStacktrace(ThrowableUtil.getStacktrace(loggingEvent.getThrowableInformation().getThrowable()));
      }
      
      eventLog.setLogger("docservice");
      eventLog.setPriority(loggingEvent.getLevel().toString());
      eventLog.setMessage(loggingEvent.getMessage().toString());
      eventLog.setCreationTime(new Date(loggingEvent.getTimeStamp()));
      
      this.cache.add(eventLog);
    }

    protected void persistCache() {
      synchronized(this.cache) {
        if(this.cache.size() == 0) return;
        this.repository.insertEventLogBlock(this.cache);
        this.cache.clear();
      }
    }
    
    public void close() {
      this.closed = true;
    }

    private static void processQueue() {
        do {
          if((instance != null) && (InjectorFactory.get() != null)) {
            instance.repository = InjectorFactory.get().getInstance(Repository.class);
          } else {
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {}
          }
        } while(instance != null && instance.repository == null);
        while (!stop) {
            try {
                LoggingEvent event = loggingEventQueue.poll(1L, TimeUnit.SECONDS);
                if (event != null) instance.processEvent(event);
            } catch (Exception e) {
                // No operations.
            }
        }
    }

    
    static {
        thread = new Thread(new Runnable() {
            public void run() {
                processQueue();
            }
        });
        thread.setName("DatabaseAppender");
        thread.setDaemon(true);
        thread.start();
    
        timer = new Timer("DatabaseLogAppender", true);
        timer.schedule(new TimerTask() {

          @Override
          public void run() {
            if(instance != null)
              instance.persistCache();              
          }}, 1000, CACHE_TIMER);
    }
    
    public static void destroy(){
      stop = true;
      thread.interrupt();
      timer.cancel();
      timer.purge();
    }
}
