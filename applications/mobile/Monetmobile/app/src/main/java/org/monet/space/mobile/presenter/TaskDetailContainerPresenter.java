package org.monet.space.mobile.presenter;

import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.view.TaskDetailContainerView;

public class TaskDetailContainerPresenter extends Presenter<TaskDetailContainerView, Void> {

  public void up() {
    this.routerHelper.goUpToTaskList();
  }

}
