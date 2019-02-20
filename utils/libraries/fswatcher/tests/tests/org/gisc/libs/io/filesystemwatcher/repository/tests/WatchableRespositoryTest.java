package tests.org.gisc.libs.io.filesystemwatcher.repository.tests;

import static org.junit.Assert.assertEquals;

import org.gisc.libs.io.watcherservice.event.FileSystemEventType;
import org.gisc.libs.io.watcherservice.watcher.FileAttributes;
import org.gisc.libs.io.watcherservice.watcher.Watchable;
import org.gisc.libs.io.watcherservice.watcher.WatchablesRepository;
import org.junit.Test;

public class WatchableRespositoryTest {

    private final static String FILENAME = "c:/filetest.txt";
    
    @Test
    public void testAddWatchable() {
       WatchablesRepository repository = new WatchablesRepository();
       repository.addWatchable(FILENAME, FileSystemEventType.FILE_CREATED);
       
       Watchable watchable      = repository.getWatchable(FILENAME);
       String filename          = (String) watchable.getAttribute(FileAttributes.FILENAME);
       FileSystemEventType type = watchable.getFileSystemEventTypes().get(0);
       
       assertEquals(1, repository.size());       
       assertEquals(FILENAME, filename);
       assertEquals(FileSystemEventType.FILE_CREATED, type);              
    }
    
    @Test
    public void testAddWatchableWithTwoFilesystemEventTypes() {
        WatchablesRepository repository = new WatchablesRepository();
        repository.addWatchable(FILENAME, FileSystemEventType.FILE_CREATED);
        repository.addWatchable(FILENAME, FileSystemEventType.FILE_MODIFIED);
    
        Watchable watchable       = repository.getWatchable(FILENAME);
        String filename           = (String) watchable.getAttribute(FileAttributes.FILENAME);
        FileSystemEventType type1 = watchable.getFileSystemEventTypes().get(0);
        FileSystemEventType type2 = watchable.getFileSystemEventTypes().get(1);        
        
        assertEquals(1, repository.size());       
        assertEquals(FILENAME, filename);
        assertEquals(FileSystemEventType.FILE_CREATED, type1);
        assertEquals(FileSystemEventType.FILE_MODIFIED, type2);
    }   
    
    @Test
    public void testOrderOfFileSystemEventTypeInsertion() {
        WatchablesRepository repository = new WatchablesRepository();
        repository.addWatchable(FILENAME, FileSystemEventType.FILE_CREATED, FileSystemEventType.FILE_MODIFIED, FileSystemEventType.FILE_DELETED);
        
        Watchable watchable       = repository.getWatchable(FILENAME);        
        FileSystemEventType type1 = watchable.getFileSystemEventTypes().get(0);
        FileSystemEventType type2 = watchable.getFileSystemEventTypes().get(1);        
        FileSystemEventType type3 = watchable.getFileSystemEventTypes().get(2);
        
        assertEquals(FileSystemEventType.FILE_CREATED,  type1);
        assertEquals(FileSystemEventType.FILE_MODIFIED, type2);
        assertEquals(FileSystemEventType.FILE_DELETED,  type3);
    }   
                   
}
