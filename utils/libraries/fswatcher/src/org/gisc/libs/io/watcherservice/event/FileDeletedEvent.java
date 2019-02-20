package org.gisc.libs.io.watcherservice.event;


public class FileDeletedEvent extends FileSystemEvent {

    public FileDeletedEvent(String filename) {
        super(FileSystemEventType.FILE_DELETED, filename);        
    }
       
}
