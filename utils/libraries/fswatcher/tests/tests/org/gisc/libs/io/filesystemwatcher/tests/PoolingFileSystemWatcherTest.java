package tests.org.gisc.libs.io.filesystemwatcher.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.gisc.libs.io.watcherservice.event.FileSystemEvent;
import org.gisc.libs.io.watcherservice.event.FileSystemEventType;
import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;
import org.gisc.libs.io.watcherservice.watcher.IPoolingFileSystemWatcher;
import org.gisc.libs.io.watcherservice.watcher.PoolingFileSystemWatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PoolingFileSystemWatcherTest {

    private static final long   DELAY             = 100;
    private static final long   PERIOD            = 1000;
    private static final long   TEST_WAIT_TIME    = 400;

    private static final String TEST_FILE         = "C:/Users/ycaballero/filetest.txt";
    
    private CountDownLatch      latch;
    private FileSystemEvent     firedEvent;
    private File                file;
    private int                 eventsCount;

    public PoolingFileSystemWatcherTest() {        
    }

    @Before
    public void setUp() throws IOException {
        file = FileSystemHelper.createFile(TEST_FILE);
        eventsCount = 0;
        firedEvent = null;
        latch = new CountDownLatch(1);        
    }

    @Test
    public void testListenModifiedFileEvent() throws InterruptedException, IOException {
        IPoolingFileSystemWatcher watcher = new PoolingFileSystemWatcher();
        watcher.watchFile(file, FileSystemEventType.FILE_MODIFIED);
        watcher.addIFileSystemEventListener(new IFileSystemEventListener() {            
            public void onChange(FileSystemEvent event) {
                firedEvent = event;
            }
        });

        watcher.setDelay(DELAY);
        watcher.setPeriod(PERIOD);
        watcher.run();

        FileSystemHelper.writeFile(file, "modification");
        latch.await(TEST_WAIT_TIME, TimeUnit.MILLISECONDS);
        watcher.stop();

        assertNotNull(firedEvent);
        assertTrue("SE CAPTURA EL EVENTO DE MODIFICACIÓN", firedEvent.getType() == FileSystemEventType.FILE_MODIFIED);
    }

    @Test
    public void testListenDeletedFileEvent() throws InterruptedException {
        IPoolingFileSystemWatcher watcher = new PoolingFileSystemWatcher();
        watcher.watchFile(file, FileSystemEventType.FILE_DELETED);
        watcher.addIFileSystemEventListener(new IFileSystemEventListener() {            
            public void onChange(FileSystemEvent event) {
                firedEvent = event;
            }
        });

        watcher.setDelay(DELAY);
        watcher.setPeriod(PERIOD);
        watcher.run();
        FileSystemHelper.deleteFile(TEST_FILE);
        latch.await(TEST_WAIT_TIME, TimeUnit.MILLISECONDS);
        watcher.stop();

        assertNotNull(firedEvent);
        assertTrue("SE CAPTURA EL EVENTO DE BORRADO", firedEvent.getType() == FileSystemEventType.FILE_DELETED);
    }

    @Test
    public void testListenCreatedFileEvent() throws InterruptedException, IOException {
        FileSystemHelper.deleteFile(TEST_FILE);

        IPoolingFileSystemWatcher watcher = new PoolingFileSystemWatcher();
        watcher.watchFile(file, FileSystemEventType.FILE_CREATED);
        watcher.addIFileSystemEventListener(new IFileSystemEventListener() {            
            public void onChange(FileSystemEvent event) {
                firedEvent = event;
            }
        });

        watcher.setDelay(DELAY);
        watcher.setPeriod(PERIOD);
        watcher.run();
        FileSystemHelper.createFile(TEST_FILE);
        latch.await(TEST_WAIT_TIME, TimeUnit.MILLISECONDS);
        watcher.stop();

        assertNotNull(firedEvent);
        assertTrue("SE CAPTURA EL EVENTO DE CREACIÓN", firedEvent.getType() == FileSystemEventType.FILE_CREATED);
    }

    @Test
    public void testListenCreatedAndModifiedFileEvents() throws IOException, InterruptedException {
        FileSystemHelper.deleteFile(TEST_FILE);

        IPoolingFileSystemWatcher watcher = new PoolingFileSystemWatcher();
        watcher.watchFile(file, FileSystemEventType.FILE_CREATED, FileSystemEventType.FILE_MODIFIED);
        watcher.addIFileSystemEventListener(new IFileSystemEventListener() {            
            public void onChange(FileSystemEvent event) {
                firedEvent = event;
                eventsCount += 1;
            }
        });

        watcher.setDelay(DELAY);
        watcher.setPeriod(PERIOD);
        watcher.run();

        FileSystemHelper.createFile(TEST_FILE);
        FileSystemHelper.writeFile(file, "content");
        latch.await(TEST_WAIT_TIME, TimeUnit.MILLISECONDS);
        watcher.stop();

        assertNotNull(firedEvent);
        Assert.assertEquals(2, eventsCount);
        assertTrue("SE CAPTURAN LOS EVENTOS DE CREACIÓN Y MODIFICACIÓN", firedEvent.getType() == FileSystemEventType.FILE_CREATED || firedEvent.getType() == FileSystemEventType.FILE_MODIFIED);
    }
    
    @After
    public void tearDown() throws IOException {
        FileSystemHelper.deleteFile(TEST_FILE);
    }
}
