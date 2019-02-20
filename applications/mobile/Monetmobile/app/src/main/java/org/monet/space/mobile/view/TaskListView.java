package org.monet.space.mobile.view;

import org.monet.space.mobile.adapter.TaskListAdapter;
import org.monet.space.mobile.model.Coordinate;
import org.monet.space.mobile.model.TasksSorting;
import org.monet.space.mobile.mvp.View;

import java.util.Date;

public interface TaskListView extends View {

  TaskListAdapter getTaskListAdapter();

  void resetSelectedPosition();

  void selectTaskWithId(long id);

  TasksSorting getSorting();

  Long getSourceId();
  
  int getTaskTray();

  String getSearchFilter();

  long getSelectedId();

  void refreshLastUpdateDate(Date date);

  Coordinate getCoordinate();

}
