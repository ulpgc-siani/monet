package org.monet.space.mobile.view;

import org.monet.space.mobile.mvp.View;

import android.support.v4.widget.CursorAdapter;

public interface AvailableTaskListView extends View {

  CursorAdapter getTaskListAdapter();
  
  void resetSelectedPosition();

  void selectTaskWithId(long id);

  Long getSourceId();

  long getSelectedId();

}
