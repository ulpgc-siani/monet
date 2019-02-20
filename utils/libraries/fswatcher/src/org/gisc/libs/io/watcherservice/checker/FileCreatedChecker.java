package org.gisc.libs.io.watcherservice.checker;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.gisc.libs.io.watcherservice.event.FileCreatedEvent;
import org.gisc.libs.io.watcherservice.event.FileSystemEvent;
import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;
import org.gisc.libs.io.watcherservice.watcher.FileAttributes;
import org.gisc.libs.io.watcherservice.watcher.Watchable;

public class FileCreatedChecker implements IFileChangesChecker {
    
    public void check(Watchable watchable, List<IFileSystemEventListener> listeners) {
        boolean fileExistingFlag = (Boolean) watchable.getAttribute(FileAttributes.FILE_EXISTING_FLAG);
        String  filename         = (String) watchable.getAttribute(FileAttributes.FILENAME);
        File file                = new File(filename);
                        
        if ((fileExistingFlag == false) && (file.exists())) {
        	Logger.getLogger(FileCreatedChecker.class).info("File created event: " + filename);

        	watchable.setAttribute(FileAttributes.FILE_EXISTING_FLAG, true);
            FileCreatedEvent event = new FileCreatedEvent(filename);
            fireEvent(event, listeners);            
        }
    }
    
    private void fireEvent(FileSystemEvent event, List<IFileSystemEventListener> listeners) {
        for (IFileSystemEventListener listener : listeners) {
            listener.onChange(event);
        }        
    }           
}
