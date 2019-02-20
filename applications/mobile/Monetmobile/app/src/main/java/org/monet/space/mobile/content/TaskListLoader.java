package org.monet.space.mobile.content;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import com.google.inject.Inject;

import org.monet.space.mobile.R;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.model.TaskTray;
import org.monet.space.mobile.model.TasksSorting;
import org.monet.space.mobile.mvp.content.DbCursorLoader;

public class TaskListLoader extends DbCursorLoader {

    public static final String TASK_TRAY = "tray";
    public static final String SOURCE_ID = "id";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String SORT_CRITERIA = "sortCriteria";
    public static final String SORT_MODE = "sortMode";
    public static final String FILTER = "filter";

    @Inject
    private Repository repositoryProvider;

    private TaskTray taskTray;
    private long sourceId = -1;
    private double latitude;
    private double longitude;
    private String sort;
    private String filter;

    public TaskListLoader(Context context, Bundle args) {
        super(context, args);
        if (args != null) {
            this.latitude = args.getDouble(LATITUDE);
            this.longitude = args.getDouble(LONGITUDE);
            this.filter = args.getString(FILTER);

            int modeIndex = 0;
            String mode = args.getString(SORT_MODE);
            if ((mode != null) && (TasksSorting.SortMode.valueOf(mode).equals(TasksSorting.SortMode.DESC))) {
                modeIndex = 1;
            }

            this.sort = context.getResources().getStringArray(R.array.sort_tasks_entry_values)[args.getInt(SORT_CRITERIA, 0) * 2 + modeIndex];

            this.taskTray = TaskTray.values()[args.getInt(TASK_TRAY)];
            this.sourceId = args.getLong(SOURCE_ID);
        }
    }

    @Override
    protected SimpleDataObserver getObserver() {
        return this.repositoryProvider;
    }

    @Override
    public Cursor loadInBackground() {
        if (taskTray == TaskTray.ASSIGNED)
            return repositoryProvider.getTasksAssigned(this.sourceId, this.latitude, this.longitude, this.sort, this.filter);
        if (taskTray == TaskTray.FINISHED)
            return repositoryProvider.getTasksFinished(this.sourceId, this.latitude, this.longitude, this.sort, this.filter);
        return repositoryProvider.getTasksUnassigned(this.sourceId, this.latitude, this.longitude, this.sort, this.filter);
    }

}
