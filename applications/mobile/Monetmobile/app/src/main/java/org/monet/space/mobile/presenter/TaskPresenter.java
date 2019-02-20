package org.monet.space.mobile.presenter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import org.monet.space.mobile.content.SourceListLoader;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.content.CursorLoaderCallbacks;
import org.monet.space.mobile.mvp.content.CursorLoaderCallbacks.LoaderFactory;
import org.monet.space.mobile.view.TaskView;

public class TaskPresenter extends Presenter<TaskView, Void> {

    private void refreshSourceList() {
        LoaderFactory loaderFactory = new LoaderFactory() {

            @Override
            public Loader<Cursor> build(Bundle args) {
                return new SourceListLoader(context, args);
            }
        };
        this.getLoaderManager().restartLoader(0, null, new CursorLoaderCallbacks(this.view.getSourceListAdapter(), loaderFactory));
    }

    public void initialize() {
        this.refreshSourceList();
    }

    public void restart() {
        this.refreshSourceList();
    }

    public void open(long id, boolean showChatByDefault) {
        this.routerHelper.goToTaskDetails(id, showChatByDefault);
    }
}
