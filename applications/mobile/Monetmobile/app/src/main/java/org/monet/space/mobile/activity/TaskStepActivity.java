package org.monet.space.mobile.activity;

//import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.squareup.otto.Subscribe;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.CapturingDataFinishedEvent;
import org.monet.space.mobile.events.CapturingDataStartedEvent;
import org.monet.space.mobile.events.FinishLoadingEvent;
import org.monet.space.mobile.events.StartLoadingEvent;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.presenter.TaskStepPresenter;
import org.monet.space.mobile.view.TaskStepView;

import java.io.File;

public class TaskStepActivity extends Activity<TaskStepView, TaskStepPresenter, Void> implements TaskStepView {

    private int loaders = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskstep);
        prepareActionBar();
    }

    private void prepareActionBar() {
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        File appIcon = LocalStorage.getAppIconFile(this);
        if (appIcon.exists())
            actionBar.setIcon(Drawable.createFromPath(appIcon.getAbsolutePath()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home)
            return super.onOptionsItemSelected(item);
        presenter.up();
        return true;
    }

    @Subscribe
    public void showLoading(StartLoadingEvent event) {
        loaders++;
        setProgressBarIndeterminateVisibility(true);
    }

    @Subscribe
    public void hideLoading(FinishLoadingEvent event) {
        loaders--;
        if (loaders <= 0)
            setProgressBarIndeterminateVisibility(false);
    }

    @Subscribe
    public void capturingDataStarted(CapturingDataStartedEvent event) {
        showLoading(null);
    }

    @Subscribe
    public void capturingDataFinished(CapturingDataFinishedEvent event) {
        hideLoading(null);
    }

}
