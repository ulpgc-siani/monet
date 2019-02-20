package org.monet.space.mobile.view;

import java.util.ArrayList;

import org.monet.space.mobile.mvp.View;

public interface TaskMapListView extends View {

  public static class TaskMapItem {
    public long   id;
    public String title;
    public String description;
    public double   latitude;
    public double   longitude;
  }

  void setOverlayItems(ArrayList<TaskMapItem> items);

  void panToUserPosition();

  void switchMapViewTiles();

  long getSourceId();

  int getTaskTray();

  String getSearchFilter();


}
