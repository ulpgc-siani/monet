package org.monet.space.mobile.view;

import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.mvp.View;

public interface TaskDetailsView extends View {

  void setTaskDetails(TaskDetails value);

  void showChatPage();

}
