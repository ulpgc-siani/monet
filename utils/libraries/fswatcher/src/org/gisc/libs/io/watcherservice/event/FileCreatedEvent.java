package org.gisc.libs.io.watcherservice.event;

public class FileCreatedEvent extends FileSystemEvent {

    public FileCreatedEvent(String filename) {
        super(FileSystemEventType.FILE_CREATED, filename);        
    }
}
