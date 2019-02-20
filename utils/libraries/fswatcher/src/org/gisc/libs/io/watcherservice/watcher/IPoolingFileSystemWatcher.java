package org.gisc.libs.io.watcherservice.watcher;

import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;

public interface IPoolingFileSystemWatcher extends IFileSystemWatcher {
    public void setDelay(long delay);
    public void setPeriod(long period);    
}
