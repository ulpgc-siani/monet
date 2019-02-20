package org.gisc.libs.io.watcherservice.event;

public interface IFileSystemEventsSource {
    public void addIFileSystemEventListener(IFileSystemEventListener listener);
    public void removeIFileSystemEventListener(IFileSystemEventListener listener);
}
