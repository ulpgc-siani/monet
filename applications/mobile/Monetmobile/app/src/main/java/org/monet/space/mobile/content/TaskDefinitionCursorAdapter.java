package org.monet.space.mobile.content;


import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter;

import org.monet.space.mobile.R;

public class TaskDefinitionCursorAdapter extends SimpleCursorAdapter {
  
  public TaskDefinitionCursorAdapter(Context context) {
    super(context, R.layout.new_task_list_item, null, new String[] { "label", "description" }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);
  }

}
