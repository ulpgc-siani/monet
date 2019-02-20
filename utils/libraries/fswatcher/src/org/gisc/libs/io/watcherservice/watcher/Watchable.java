package org.gisc.libs.io.watcherservice.watcher;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gisc.libs.io.watcherservice.event.FileSystemEventType;


public class Watchable {
                
    private String filename;
    private List<FileSystemEventType> eventTypes;
    private Map<String, Object> attributes;
        
    public Watchable(String filename) {
        this.filename   = filename;
        this.eventTypes = new LinkedList<FileSystemEventType>();
        this.attributes = new HashMap<String, Object>();        
        updateFileAttributes();
    }
    
    public void addFileSystemEventTypes(FileSystemEventType... types) {
        for (FileSystemEventType type : types) {
            if (! eventTypes.contains(type)) {
                this.eventTypes.add(type);                        
            }
        }
    }         
    
    public List<FileSystemEventType> getFileSystemEventTypes() {        
        return this.eventTypes;
    }
    
    public Object getAttribute(String attributeName) {        
        return this.attributes.get(attributeName);
    }
    
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);        
    }
        
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Watchable)) return false;
        if (this == object) return true;
        return equals((Watchable) object);
    }
    
    private boolean equals(Watchable watchable) {
        return filename.equals(watchable.filename);
    }
        
    private void updateFileAttributes() {
        File file = new File(filename);
        setAttribute(FileAttributes.FILE, file);
        setAttribute(FileAttributes.FILE_EXISTING_FLAG, file.exists());
        setAttribute(FileAttributes.FILENAME, this.filename);
        setAttribute(FileAttributes.LAST_MODIFIED_DATE, file.lastModified());         
    }
}
