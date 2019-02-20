package org.gisc.libs.io.watcherservice.watcher;

public enum WatcherState {

    STARTED(true),
    STOPPED(false);
    
    private boolean state; 
    
    private WatcherState(boolean value) {
        this.state = value;
    }    
}
