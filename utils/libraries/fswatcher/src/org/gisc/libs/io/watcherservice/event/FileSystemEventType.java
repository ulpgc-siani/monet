package org.gisc.libs.io.watcherservice.event;

public enum FileSystemEventType {
    FILE_CREATED(0, "FILE CREATED"),
    FILE_MODIFIED(1, "FILE MODIFIED"),    
    FILE_DELETED(2, "FILE_DELETED");
    
    private int value;
    private String name;
    
    private FileSystemEventType(int value, String name) {
        this.value = value;
        this.name  = name;
    }
    
    public int intValue() {
        return this.value;
    }
            
    @Override
    public String toString() {
        return this.name; 
    }
}
