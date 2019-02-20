package org.monet.space.mobile.view;

import org.monet.space.mobile.mvp.View;

import android.support.v4.widget.CursorAdapter;

public interface TaskView extends View {

  CursorAdapter getSourceListAdapter();

}
