package org.monet.space.mobile.activity;

import java.io.File;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.TaskAbandonedEvent;
import org.monet.space.mobile.events.TaskLoadedEvent;
import org.monet.space.mobile.events.TaskLoadingEvent;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.presenter.TaskDetailContainerPresenter;
import org.monet.space.mobile.view.TaskDetailContainerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.Window;

import com.squareup.otto.Subscribe;

public class TaskDetailsContainerActivity extends Activity<TaskDetailContainerView, TaskDetailContainerPresenter, Void> implements TaskDetailContainerView {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

    this.setContentView(R.layout.activity_taskdetailscontainer);

    prepareActionBar();
  }

  private void prepareActionBar() {
    ActionBar actionBar = this.getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    
    File appIcon = LocalStorage.getAppIconFile(this);
    if (appIcon.exists()) {
      Drawable icon = Drawable.createFromPath(appIcon.getAbsolutePath());
      actionBar.setIcon(icon);
    }
  }

  @Subscribe
  public void showLoading(TaskLoadingEvent event) {
    this.setProgressBarIndeterminateVisibility(true);
  }

  @Subscribe
  public void hideLoading(TaskLoadedEvent event) {
    this.setProgressBarIndeterminateVisibility(false);
  }
  
  @Subscribe
  public void taskAbandoned(TaskAbandonedEvent event) {
    this.finish();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        this.presenter.up();
        break;
      default:
        return super.onOptionsItemSelected(item);
    }

    return true;
  }

}
