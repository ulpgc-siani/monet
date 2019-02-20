package org.monet.space.mobile.activity;

import java.io.File;

import org.monet.space.mobile.R;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.presenter.PickPositionContainerPresenter;
import org.monet.space.mobile.view.PickPositionContainerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PickPositionContainerActivity extends Activity<PickPositionContainerView, PickPositionContainerPresenter, Void> implements PickPositionContainerView {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.setContentView(R.layout.activity_pick_position);

    prepareActionBar();

    this.presenter.initialize();
  }

  private void prepareActionBar() {
    ActionBar actionBar = this.getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setDisplayShowHomeEnabled(false);
    
    File appIcon = LocalStorage.getAppIconFile(this);
    if (appIcon.exists()) {
      Drawable icon = Drawable.createFromPath(appIcon.getAbsolutePath());
      actionBar.setIcon(icon);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.pick_position, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_cancel:
        this.setResult(RESULT_CANCELED);
        this.finish();
        break;
      default:
        return super.onOptionsItemSelected(item);
    }

    return true;
  }

}
