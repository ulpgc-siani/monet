package org.gisc.libs.io.watcherservice.checker;

import java.util.List;

import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;
import org.gisc.libs.io.watcherservice.watcher.Watchable;

public interface IFileChangesChecker {
    public void check(Watchable watchable, List<IFileSystemEventListener> listeners);
}
