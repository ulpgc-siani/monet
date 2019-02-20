package org.monet.space.mobile.presenter;

import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.view.NewTaskContainerView;

public class NewTaskContainerPresenter extends Presenter<NewTaskContainerView, Void> {

    public void up() {
        this.routerHelper.goUpToTaskList();
    }
}
