package org.gisc.libs.io.watcherservice.checker;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.gisc.libs.io.watcherservice.event.FileDeletedEvent;
import org.gisc.libs.io.watcherservice.event.FileSystemEvent;
import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;
import org.gisc.libs.io.watcherservice.watcher.FileAttributes;
import org.gisc.libs.io.watcherservice.watcher.Watchable;

public class FileDeletedChecker implements IFileChangesChecker {
    
    public void check(Watchable watchable, List<IFileSystemEventListener> listeners) {
        boolean fileExistingFlag = (Boolean) watchable.getAttribute(FileAttributes.FILE_EXISTING_FLAG);
        String  filename         = (String) watchable.getAttribute(FileAttributes.FILENAME);
        File file                = new File(filename);
                
        if (fileExistingFlag && (! file.exists())) {            
        	Logger.getLogger(FileDeletedChecker.class).info("File deleted event: " + filename);
        	
            FileDeletedEvent event = new FileDeletedEvent(filename);
            fireEvent(event, listeners);            
        }
    }
    
    private void fireEvent(FileSystemEvent event, List<IFileSystemEventListener> listeners) {
        for (IFileSystemEventListener listener : listeners) {
            listener.onChange(event);
        }        
    }
}
