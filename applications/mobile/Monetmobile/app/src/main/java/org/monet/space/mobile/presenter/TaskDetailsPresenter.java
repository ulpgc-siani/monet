package org.monet.space.mobile.presenter;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import com.google.inject.Inject;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.events.TaskLoadedEvent;
import org.monet.space.mobile.events.TaskLoadingEvent;
import org.monet.space.mobile.helpers.RouterHelper;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.content.SimpleDataLoader;
import org.monet.space.mobile.view.TaskDetailsView;

public class TaskDetailsPresenter extends Presenter<TaskDetailsView, Void> implements LoaderCallbacks<TaskDetails> {

    @Inject
    Repository repository;

    private long taskId;
    private TaskDetails taskDetails;

    public void initialize() {
        this.taskId = this.getArgs().getLong(RouterHelper.ID);
        this.getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<TaskDetails> onCreateLoader(int id, Bundle args) {
        BusProvider.get().post(new TaskLoadingEvent());
        return new SimpleDataLoader<TaskDetails>(this.context, args) {

            @Override
            protected SimpleDataObserver getObserver() {
                return repository;
            }

            @Override
            public TaskDetails loadInBackground() {
                taskDetails = repository.getTaskDetails(taskId);
                if (taskDetails != null)
                    taskDetails.chatItems = repository.getChatItems(taskId);
                return taskDetails;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<TaskDetails> loader, TaskDetails value) {
        if (value != null) {
            this.view.setTaskDetails(value);
            if (this.getArgs().getBoolean(RouterHelper.SHOW_CHAT_PAGE, false))
                this.view.showChatPage();
        }
        BusProvider.get().post(new TaskLoadedEvent());
    }

    @Override
    public void onLoaderReset(Loader<TaskDetails> loader) {
    }

    public void viewChats() {
        if (taskDetails.notReadChats > 0) {
            repository.resetNotReadChatCount(taskId);
            taskDetails.notReadChats = 0;
        }
    }

}
