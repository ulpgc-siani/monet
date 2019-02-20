package org.gisc.libs.io.watcherservice.checker;

import java.util.Hashtable;
import java.util.Map;

import org.gisc.libs.io.watcherservice.event.FileSystemEventType;

public class FileChangesCheckerFactory {
    
    private Map<FileSystemEventType, Class<? extends IFileChangesChecker>> checkersTable;

    public FileChangesCheckerFactory() {
        this.checkersTable = new Hashtable<FileSystemEventType, Class<? extends IFileChangesChecker>>();
    }
    
    public void register(FileSystemEventType type,  Class<? extends IFileChangesChecker> classFile) {       
        checkersTable.put(type, classFile);
    }
     
    public void unRegister(FileSystemEventType type) {
        checkersTable.remove(type);       
    }
    
    public IFileChangesChecker getFileChangesChecker(FileSystemEventType type) {
        Class<? extends IFileChangesChecker> classFile = checkersTable.get(type);
        IFileChangesChecker checker;
        try {
            checker = classFile.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("IFileChangesChecker not found", ex);
        }                
        return checker;
    }

    public void clear() {
        checkersTable.clear();
    }
    
    public void dispose() {
        clear();
        checkersTable = null;        
    }
}
