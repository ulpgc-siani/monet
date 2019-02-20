package org.gisc.libs.io.watcherservice.checker;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.gisc.libs.io.watcherservice.event.FileModifiedEvent;
import org.gisc.libs.io.watcherservice.event.FileSystemEvent;
import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;
import org.gisc.libs.io.watcherservice.watcher.FileAttributes;
import org.gisc.libs.io.watcherservice.watcher.Watchable;

public class FileModifiedChecker implements IFileChangesChecker {
        
    public void check(Watchable watchable, List<IFileSystemEventListener> listeners) {        
        String filename     = (String) watchable.getAttribute(FileAttributes.FILENAME);        
        long   lastModified = (Long)   watchable.getAttribute(FileAttributes.LAST_MODIFIED_DATE);
                        
        File file = new File(filename);
        if (! file.exists()) return;
        
        if (file.lastModified() > lastModified) {            
        	Logger.getLogger(FileModifiedChecker.class).info("File modified event: " + filename);

        	FileModifiedEvent event = new FileModifiedEvent(filename, file.lastModified());
            fireEvent(event, listeners);
            watchable.setAttribute(FileAttributes.LAST_MODIFIED_DATE, file.lastModified());            
        }
    }

    private void fireEvent(FileSystemEvent event, List<IFileSystemEventListener> listeners) {
        for (IFileSystemEventListener listener : listeners) {
            listener.onChange(event);
        }        
    }
}
