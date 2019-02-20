package org.monet.space.mobile.presenter;

import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.view.TaskStepView;

import android.os.Bundle;

public class TaskStepPresenter extends Presenter<TaskStepView, Void> {

  public void initialize(Bundle savedInstanceState) {
    
  }
  
  public void up() {
    this.routerHelper.goUpToTaskList();
  }

}
