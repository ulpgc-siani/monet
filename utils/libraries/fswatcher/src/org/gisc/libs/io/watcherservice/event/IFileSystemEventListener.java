package org.gisc.libs.io.watcherservice.event;

public interface IFileSystemEventListener {
    public void onChange(FileSystemEvent event);
}
