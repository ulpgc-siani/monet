package org.monet.space.mobile.view;

import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.mvp.View;

public interface AvailableTaskDetailsView extends View {

  void setTitle(String title);

  void setTaskDetails(TaskDetails value);

}
