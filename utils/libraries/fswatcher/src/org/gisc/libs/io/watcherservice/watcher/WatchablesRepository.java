package org.gisc.libs.io.watcherservice.watcher;

import java.util.Map;
import java.util.WeakHashMap;

import org.gisc.libs.io.watcherservice.event.FileSystemEventType;

public class WatchablesRepository {

    private Map<String, Watchable> watchablesMap;
    
    public WatchablesRepository() {
        this.watchablesMap = new WeakHashMap<String, Watchable>();
    }
    
    private Watchable createWatchable(String filename) {
        Watchable watchable = new Watchable(filename);
        if (watchablesMap.containsKey(filename)) {
            watchable = this.watchablesMap.get(filename);
        }
        return watchable;
    }
    
    
    public Watchable getWatchable(String filename) {
        return this.watchablesMap.get(filename);        
    }
    
    public void addWatchable(String filename, FileSystemEventType... types) {
       Watchable watchable = createWatchable(filename);
       watchable.addFileSystemEventTypes(types);   
       this.watchablesMap.put(filename, watchable);
    }    
    
    public void removeWatchable(String filename) {         
        this.watchablesMap.remove(filename);
    }
    
    public int size() {
        return this.watchablesMap.size();
    }
    
    public void clear() {
        this.watchablesMap.clear();
    }
    
    public Watchable[] getWatchables() {
        Watchable copy[] = new Watchable[this.watchablesMap.size()];
        return this.watchablesMap.values().toArray(copy);     
    }            
}
