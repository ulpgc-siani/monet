package org.monet.space.mobile.presenter;

import org.monet.space.mobile.helpers.SyncAccountHelper;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.view.PreferenceContainerView;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.google.inject.Inject;

public class SettingsContainerPresenter extends Presenter<PreferenceContainerView, Void> implements OnSharedPreferenceChangeListener {

  @Inject
  SharedPreferences sharedPreferences;

  public void initialize() {
    this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }

  public void destroy() {
    this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
  }

  public void up() {
    this.routerHelper.goUpToTaskList();
  }
  
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals("pref_key_sync_interval")) {
      SyncAccountHelper.setupAllAccountsSyncInterval(this.context);
    }
  }

}
