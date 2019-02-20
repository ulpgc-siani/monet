package org.monet.space.mobile.presenter;

import java.util.ArrayList;

import org.monet.space.mobile.R;
import org.monet.space.mobile.content.TaskMapLoader;
import org.monet.space.mobile.events.FinishLoadingEvent;
import org.monet.space.mobile.events.StartLoadingEvent;
import org.monet.space.mobile.helpers.SyncAccountHelper;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.view.TaskMapListView;
import org.monet.space.mobile.view.TaskMapListView.TaskMapItem;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class TaskMapListPresenter extends Presenter<TaskMapListView, Void> {

  
  public void initialize() {
  }

  public void up() {
    this.routerHelper.goUpToTaskList();
  }
  
  public void showMyLocation() {
    this.view.panToUserPosition();
  }

  public void changeLayer() {
    this.view.switchMapViewTiles();
  }

  public void refresh() {
    BusProvider.get().post(new StartLoadingEvent());
    TasksLoader.create(this.context, this.view).execute();
  }

  static class TasksLoader extends AsyncTask<Long, Void, ArrayList<TaskMapItem>> {

    private Context     context;
    private long        sourceId;
    private int         taskTray;
    private String      searchFilter;
    private TaskMapListView view;

    public static TasksLoader create(Context context, TaskMapListView view) {
      return new TasksLoader(context, view);
    }

    private TasksLoader(Context context, TaskMapListView view) {
      this.context = context;
      this.view = view;
    }

    @Override
    protected ArrayList<TaskMapItem> doInBackground(Long... params) {
      ArrayList<TaskMapItem> items = new ArrayList<TaskMapItem>();

      this.sourceId = this.view.getSourceId();
      this.taskTray = this.view.getTaskTray();
      this.searchFilter = this.view.getSearchFilter();

      Bundle bp = new Bundle();
      bp.putLong(TaskMapLoader.SOURCE_ID, this.sourceId);
      bp.putInt(TaskMapLoader.TASK_TRAY, this.taskTray);
      bp.putString(TaskMapLoader.SEARCH_FILTER, this.searchFilter);

      TaskMapLoader loader = new TaskMapLoader(context, bp);
      Cursor cursor = null;
      try {
        cursor = loader.loadInBackground();
        int idIndex = cursor.getColumnIndex("_id");
        int latitudeIndex = cursor.getColumnIndex("latitude");
        int longitudeIndex = cursor.getColumnIndex("longitude");
        int labelIndex = cursor.getColumnIndex("label");
        int sourceLabelIndex = cursor.getColumnIndex("source_label");
        while (cursor.moveToNext()) {
          TaskMapItem item = new TaskMapItem();
          item.id = cursor.getLong(idIndex);
          item.latitude = cursor.getDouble(latitudeIndex);
          item.longitude = cursor.getDouble(longitudeIndex);
          item.title = cursor.getString(labelIndex);
          item.description = cursor.getString(sourceLabelIndex);
          items.add(item);
        }
      } catch (Exception ex) {
        Log.e("MONET", ex.getMessage(), ex);
      } finally {
        if (cursor != null)
          cursor.close();
      }

      return items;
    }

    @Override
    protected void onPostExecute(ArrayList<TaskMapItem> items) {
      super.onPostExecute(items);
      view.setOverlayItems(items);
      BusProvider.get().post(new FinishLoadingEvent());
    }

  }

  public void sync() {
    SyncAccountHelper.enableAllAccountsSync(context);
    SyncAccountHelper.syncAllAccountsTasks(context);

    Toast.makeText(this.context, R.string.sync_message, Toast.LENGTH_SHORT).show();
  }

}
