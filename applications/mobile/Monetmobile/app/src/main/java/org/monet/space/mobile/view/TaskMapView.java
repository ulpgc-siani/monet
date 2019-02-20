package org.monet.space.mobile.view;

import org.monet.space.mobile.mvp.View;

import android.support.v4.widget.CursorAdapter;

public interface TaskMapView extends View {

  CursorAdapter getSourceListAdapter();

  boolean hasDetailsPanel();

  void showDetailsPanel();

  void hideDetailsPanel();

}
