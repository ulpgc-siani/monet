package org.gisc.libs.io.watcherservice.watcher;


import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.gisc.libs.io.watcherservice.checker.FileChangesCheckerFactory;
import org.gisc.libs.io.watcherservice.checker.FileCreatedChecker;
import org.gisc.libs.io.watcherservice.checker.FileDeletedChecker;
import org.gisc.libs.io.watcherservice.checker.FileModifiedChecker;
import org.gisc.libs.io.watcherservice.checker.IFileChangesChecker;
import org.gisc.libs.io.watcherservice.event.FileSystemEventType;
import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;
import org.gisc.libs.io.watcherservice.event.IFileSystemEventsSource;

public class PoolingFileSystemWatcher implements IPoolingFileSystemWatcher, IFileSystemEventsSource {

    private static final String ILLEGAL_ACCESS = "Watcher is running. It's only permited call this method when watcher is stopped";
    
    private WatchablesRepository watchablesRepository;    
    private List<IFileSystemEventListener> listeners;
    private WatcherState      state;
    private boolean           isCancel;
    private long              delay;
    private long              period;
    private Timer             timer;
    FileChangesCheckerFactory factory;
    private Logger logger;
    
    private static final long DEFAUL_DELAY   = 100;
    private static final long DEFAULT_PERIOD = 5 * 60 * 1000;
    
    public PoolingFileSystemWatcher() {
        this.watchablesRepository = new WatchablesRepository();
        this.factory    = new FileChangesCheckerFactory();
        this.listeners  = new LinkedList<IFileSystemEventListener>();
        this.state      = WatcherState.STOPPED;
        this.isCancel   = false;
        this.delay      = DEFAUL_DELAY;
        this.period     = DEFAULT_PERIOD;
        
        this.logger = Logger.getLogger(PoolingFileSystemWatcher.class);        
        init();
    }
  
    
    public void setDelay(long delay) {
        if (state == WatcherState.STARTED) throw new IllegalStateException(ILLEGAL_ACCESS);
        this.delay = delay;
    }

    
    public void setPeriod(long period) {
        if (state == WatcherState.STARTED) throw new IllegalStateException(ILLEGAL_ACCESS);
        this.period = period;
    }
        
    public void run() {
        this.state = WatcherState.STARTED;   
        this.schedule(this.delay, this.period);          

        this.logger.info("FileSystem watcher started with values delay:" + this.delay + " period:" + this.period);
    }

    public void watchFile(File file, FileSystemEventType... types) {
        if (state == WatcherState.STARTED) throw new IllegalStateException(ILLEGAL_ACCESS);
        this.watchablesRepository.addWatchable(file.getAbsolutePath(), types);                        
    }

    public void unWatchable(File file) {
        if (state == WatcherState.STARTED) throw new IllegalStateException(ILLEGAL_ACCESS);
        this.watchablesRepository.removeWatchable(file.getAbsolutePath());
    } 

    public void schedule(long delay, long period) {
        timer = new Timer();           
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PoolingFileSystemWatcher.this.lookForChanges();
            }            
        }, delay, period);         
    }
        
       
    public void addIFileSystemEventListener(IFileSystemEventListener listener) {
        if (! this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    
    public void removeIFileSystemEventListener(IFileSystemEventListener listener) {
        this.listeners.remove(listener);
    }

    public void stop() {
        this.timer.cancel();
        this.isCancel = true;
        this.state = WatcherState.STOPPED;
        
        this.logger.info("FileSystem watcher stopped");
    }    
    
    private void lookForChanges() {        
        for (Watchable watchable : this.watchablesRepository.getWatchables()) {   
            if (this.isCancel) return;
            lookForChanges(watchable);
        }                
    }
    
    private void lookForChanges(Watchable watchable) {               
        for (FileSystemEventType type : watchable.getFileSystemEventTypes()) {
            IFileChangesChecker checker = factory.getFileChangesChecker(type);                                   
            checker.check(watchable, this.listeners);                     
        }               
    }

    private void init() {
        this.factory.register(FileSystemEventType.FILE_MODIFIED, FileModifiedChecker.class);
        this.factory.register(FileSystemEventType.FILE_DELETED, FileDeletedChecker.class);    
        this.factory.register(FileSystemEventType.FILE_CREATED, FileCreatedChecker.class);        
    }

}
