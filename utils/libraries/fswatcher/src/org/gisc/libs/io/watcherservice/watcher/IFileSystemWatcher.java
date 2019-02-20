package org.gisc.libs.io.watcherservice.watcher;

import java.io.File;

import org.gisc.libs.io.watcherservice.event.FileSystemEventType;
import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;

public interface IFileSystemWatcher {
    
    public void watchFile(File file, FileSystemEventType... fileSystemEventType);
    public void unWatchable(File file);
    public void run();
    public void stop();    
    
    public void addIFileSystemEventListener(IFileSystemEventListener listener);
    public void removeIFileSystemEventListener(IFileSystemEventListener listener);
}
