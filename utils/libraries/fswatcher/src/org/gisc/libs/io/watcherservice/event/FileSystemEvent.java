package org.gisc.libs.io.watcherservice.event;

public class FileSystemEvent {
    
    private FileSystemEventType type;
    private String filename;
    
    public FileSystemEvent(FileSystemEventType type, String filename) {
        this.type = type;
        this.filename = filename;
    }
    
    public FileSystemEventType getType() {
        return this.type;
    }
    
    public String getFilename() {
        return this.filename;
    }
}
