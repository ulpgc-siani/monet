package org.monet.space.mobile.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.widget.CursorAdapter;
import android.widget.Toast;
import com.google.inject.Inject;
import org.monet.mobile.model.TaskDefinition;
import org.monet.space.mobile.content.TaskDefinitionCursorAdapter;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.events.FinishLoadingEvent;
import org.monet.space.mobile.events.StartLoadingEvent;
import org.monet.space.mobile.helpers.LocationHelper;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.model.Coordinate;
import org.monet.space.mobile.model.SourceDetails;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.view.NewTaskView;
import roboguice.content.RoboAsyncTaskLoader;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class NewTaskPresenter extends Presenter<NewTaskView, Void> implements LoaderCallbacks<Map<String, CursorAdapter>> {

    @Inject
    Repository repository;

    public void initialize() {
        BusProvider.get().post(new StartLoadingEvent());

        getLoaderManager().restartLoader(0, null, this);
    }

    public void newTask(long definitionId) {
        try {
            long sourceId = repository.getDefinitionSource(definitionId);
            TaskDefinition definition = repository.getDefinition(definitionId);
            SourceDetails sourceDetails = repository.getSource(sourceId);

            Coordinate coordinate = LocationHelper.getLastKnownLocation(context);

            long taskId = repository.addTask(sourceId, sourceDetails.label, null, null, definition.code, definition.label, definition.description, coordinate.getLat(), coordinate.getLon(), null, null, false, null, definition.stepList.size(), TaskDetails.TASK_TRAY_ASSIGNED, true);
            repository.updateTaskStartDate(taskId, new Date());
            routerHelper.goToTaskStep(taskId, false);
            finish();
        } catch (Exception ex) {
            Log.error(ex);
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void restart() {
        getLoaderManager().getLoader(0).forceLoad();
    }

    private static class Loader extends RoboAsyncTaskLoader<Map<String, CursorAdapter>> {

        @Inject
        Repository repository;

        public Loader(Context context) {
            super(context);
        }

        @Override
        public Map<String, CursorAdapter> loadInBackground() {

            Cursor sources = null;
            Map<String, CursorAdapter> sectionsMap = new LinkedHashMap<>();
            try {
                sources = repository.getSources();

                int sourceIdIndex = sources.getColumnIndex("_id");
                int sourceLabelIndex = sources.getColumnIndex("label");
                while (sources.moveToNext()) {
                    String section = sources.getString(sourceLabelIndex);

                    Cursor cursor = repository.getDefinitions(sources.getLong(sourceIdIndex));
                    TaskDefinitionCursorAdapter cursorAdapter = new TaskDefinitionCursorAdapter(getContext());
                    cursorAdapter.swapCursor(cursor);
                    sectionsMap.put(section, cursorAdapter);
                }

            } catch (Exception ex) {
                if (sources != null && !sources.isClosed())
                    sources.close();
            }
            return sectionsMap;
        }

    }

    @Override
    public android.support.v4.content.Loader<Map<String, CursorAdapter>> onCreateLoader(int id, Bundle args) {
        return new Loader(this.context);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Map<String, CursorAdapter>> loader, Map<String, CursorAdapter> data) {
        view.setDefinitions(data);
        BusProvider.get().post(new FinishLoadingEvent());
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Map<String, CursorAdapter>> loader) {
    }

}
