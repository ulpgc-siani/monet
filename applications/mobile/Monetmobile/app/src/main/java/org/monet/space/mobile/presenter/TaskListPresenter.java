package org.monet.space.mobile.presenter;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.google.inject.Inject;

import org.monet.space.mobile.R;
import org.monet.space.mobile.adapter.TaskListAdapter;
import org.monet.space.mobile.content.TaskListLoader;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.events.FinishLoadingEvent;
import org.monet.space.mobile.events.StartLoadingEvent;
import org.monet.space.mobile.events.TaskDeselectedEvent;
import org.monet.space.mobile.events.TaskSelectedEvent;
import org.monet.space.mobile.helpers.DateUtils;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.helpers.OpenGLHelper;
import org.monet.space.mobile.helpers.SyncAccountHelper;
import org.monet.space.mobile.model.Coordinate;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.model.TasksSorting;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.content.CursorLoaderCallbacks;
import org.monet.space.mobile.mvp.content.CursorLoaderCallbacks.LoaderFactory;
import org.monet.space.mobile.view.TaskListView;
import org.siani.cluster.Item;
import org.siani.cluster.ItemList;
import org.siani.cluster.RegexClusterizer;

import java.util.ArrayList;
import java.util.List;

public class TaskListPresenter extends Presenter<TaskListView, Void> {

    @Inject
    private Repository repository;

    private LoaderFactory loaderFactory;
    private CursorLoaderCallbacks loaderCallbacks;


    private class AssignTasks extends AsyncTask<long[], Void, Void> {

        @Override
        protected Void doInBackground(long[]... taskToAssignIds) {

            for (long idTask : taskToAssignIds[0]) {
                repository.updateTaskToAssigned(idTask);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            SyncAccountHelper.syncAllAccountsTasks(context);
            Toast.makeText(context, context.getString(R.string.tasks_to_appear_when_sync_complete), Toast.LENGTH_SHORT).show();
        }
    }

    private class AbandonTasks extends AsyncTask<long[], Void, Void> {

        @Override
        protected Void doInBackground(long[]... taskToAbandonIds) {

            for (long idTask : taskToAbandonIds[0]) {
                if (repository.getTaskServerId(idTask) == null) {
                    repository.deleteTask(idTask);
                    LocalStorage.deleteTask(context, idTask);
                } else {
                    repository.updateTaskToUnassigned(idTask);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            SyncAccountHelper.syncAllAccountsTasks(context);
            Toast.makeText(context, context.getString(R.string.abandon_tasks_result_message), Toast.LENGTH_SHORT).show();
        }
    }


    private void createLoaderFactory() {
        this.loaderFactory = new LoaderFactory() {

            @Override
            public Loader<Cursor> build(Bundle args) {
                return new TaskListLoader(context, args);
            }
        };
    }

    private void createLoaderCallbacks() {
        this.loaderCallbacks = new CursorLoaderCallbacks(fakeCursor(), this.loaderFactory) {

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                final TaskListAdapter taskListAdapter = view.getTaskListAdapter();
                taskListAdapter.updateTasks(items(data));
                if (data.moveToFirst()) {
                    view.selectTaskWithId(view.getSelectedId());
                } else {
                    view.resetSelectedPosition();
                    BusProvider.get().post(new TaskDeselectedEvent());
                }

                BusProvider.get().post(new FinishLoadingEvent());
            }
        };
    }

    private List<Item> items(Cursor data) {
        final List<Item<TaskDetails>> list = sortedTasks(data);
        final List<Item> items = new ArrayList<>();
        for (Item<TaskDetails> item : list)
            items.add(item);
        return items;
    }

    private List<Item<TaskDetails>> sortedTasks(Cursor data) {
        final ItemList<TaskDetails> list = new RegexClusterizer<TaskDetails>().split(": ").clusterize(readTasks(data));
        if (view.getSorting() != null)
            list.sort(view.getSorting().getComparator());
        return list.edit();
    }

    private SimpleCursorAdapter fakeCursor() {
        return new SimpleCursorAdapter(context, R.layout.task_list_item, null, null, new int[]{android.R.id.text1, android.R.id.text2}, 0);
    }

    public void initialize() {
        this.createLoaderFactory();
        this.createLoaderCallbacks();

        this.view.refreshLastUpdateDate(SyncAccountHelper.getLastUpdateDate(context));
    }

    private void loadTasks() {
        Bundle args = new Bundle();

        args.putInt(TaskListLoader.TASK_TRAY, this.view.getTaskTray());

        Coordinate coordinate = view.getCoordinate();
        args.putDouble(TaskListLoader.LATITUDE, coordinate.getLat());
        args.putDouble(TaskListLoader.LONGITUDE, coordinate.getLon());

        Long sourceId = this.view.getSourceId();
        if (sourceId != null)
            args.putLong(TaskListLoader.SOURCE_ID, sourceId);

        String searchFilter = this.view.getSearchFilter();
        if (searchFilter != null)
            args.putString(TaskListLoader.FILTER, searchFilter);

        TasksSorting sorting = this.view.getSorting();
        if (sorting != null) {
            args.putInt(TaskListLoader.SORT_CRITERIA, sorting.getSortCriteria());
            args.putString(TaskListLoader.SORT_MODE, sorting.getSortMode().toString());
        }


        this.getLoaderManager().restartLoader(0, args, this.loaderCallbacks);
    }

    public void refresh() {
        BusProvider.get().post(new StartLoadingEvent());
        this.loadTasks();
    }

    public void sync() {
        SyncAccountHelper.enableAllAccountsSync(context);
        SyncAccountHelper.syncAllAccountsTasks(this.context);
    }

    public void openTask(long id) {
        BusProvider.get().post(new TaskSelectedEvent(id));
    }

    public void newTask() {
        this.routerHelper.goToNewTask();
    }

    public void assignTasks(final long[] taskToAssignIds) {
        new AssignTasks().execute(taskToAssignIds);
    }

    public void abandonTasks(final long[] taskToAbandonIds) {
        new AbandonTasks().execute(taskToAbandonIds);
    }

    public void viewInMap() {
        if (OpenGLHelper.supportOpenGLES20(context)) {
            this.routerHelper.goToTaskMap();
        } else
            Toast.makeText(context, R.string.maps_not_supported, Toast.LENGTH_LONG).show();
    }

    public void openSettings() {
        this.routerHelper.goToSettings();
    }

    private List<TaskDetails> readTasks(Cursor cursor) {
        final List<TaskDetails> tasks = new ArrayList<>();
        if (!cursor.moveToFirst()) return tasks;
        do {
            tasks.add(createTask(cursor));
        } while (cursor.moveToNext());

        return tasks;
    }

    private TaskDetails createTask(Cursor cursor) {
        TaskDetails task = new TaskDetails();
        task.id = cursor.getLong(cursor.getColumnIndex("_id"));
        task.label = cursor.getString(cursor.getColumnIndex("label"));
        task.sourceLabel = cursor.getString(cursor.getColumnIndex("source_label"));
        task.position = position(cursor);
        task.urgent = cursor.getInt(cursor.getColumnIndex("urgent")) > 0;
        task.suggestedEndDate = DateUtils.parseDateTime(cursor.getString(cursor.getColumnIndex("suggested_end_date")));
        task.notReadChats = cursor.getInt(cursor.getColumnIndex("not_read_chats"));
        return task;
    }

    private TaskDetails.Position position(Cursor cursor) {
        TaskDetails.Position position = new TaskDetails.Position();
        position.latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        position.longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
        return position;
    }

}
