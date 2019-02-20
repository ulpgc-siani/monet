package org.monet.space.mobile.view;

import java.util.Map;

import org.monet.space.mobile.mvp.View;

import android.support.v4.widget.CursorAdapter;

public interface NewTaskView extends View {

  void setDefinitions(Map<String, CursorAdapter> data);

}
