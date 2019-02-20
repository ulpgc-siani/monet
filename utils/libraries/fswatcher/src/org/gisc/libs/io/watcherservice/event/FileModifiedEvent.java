package org.gisc.libs.io.watcherservice.event;

public class FileModifiedEvent extends FileSystemEvent {    
    private long lastModified;
    
    public FileModifiedEvent(String filename, long lastModified) {
        super(FileSystemEventType.FILE_MODIFIED, filename);        
        this.lastModified = lastModified;
    }
        
    public long getLastModified() {
        return this.lastModified;
    }
}
