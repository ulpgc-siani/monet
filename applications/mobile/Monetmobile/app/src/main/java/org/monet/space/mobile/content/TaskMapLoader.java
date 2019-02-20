package org.monet.space.mobile.content;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import com.google.inject.Inject;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.model.TaskTray;
import org.monet.space.mobile.mvp.content.DbCursorLoader;

public class TaskMapLoader extends DbCursorLoader {

    public static final String TASK_TRAY = "tray";
    public static final String SOURCE_ID = "id";
    public static final String SEARCH_FILTER = "searchfilter";

    @Inject
    Repository repositoryProvider;

    private long sourceId = -1;
    private TaskTray taskTray;
    private String filter;

    public TaskMapLoader(Context context, Bundle args) {
        super(context, args);
        if (args != null) {
            this.filter = args.getString(SEARCH_FILTER);

            this.taskTray = TaskTray.values()[args.getInt(TASK_TRAY)];
            this.sourceId = args.getLong(SOURCE_ID);
        }
    }

    @Override
    protected SimpleDataObserver getObserver() {
        return repositoryProvider;
    }

    @Override
    public Cursor loadInBackground() {
        if (taskTray == TaskTray.ASSIGNED)
            return repositoryProvider.getTasksAssignedWithLocations(this.sourceId, this.filter);
        if (taskTray == TaskTray.FINISHED)
            return repositoryProvider.getTasksFinishedWithLocations(this.sourceId, this.filter);
        return repositoryProvider.getTasksUnassignedWithLocations(this.sourceId, this.filter);
    }
}
