package org.monet.space.mobile.mvp.fragment;

import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.View;

import android.os.Bundle;

import com.google.inject.Inject;

public abstract class DialogFragment<V extends View, T extends Presenter<V, A>, A> extends Fragment {

  @Inject
  protected T presenter;

  @SuppressWarnings("unchecked")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.presenter.injectView((V) this);
  }

  @Override
  public void onResume() {
    super.onResume();

    BusProvider.get().register(this);
  }

  @Override
  public void onPause() {
    super.onPause();

    BusProvider.get().unregister(this);
  }

}
