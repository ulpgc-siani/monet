package org.monet.space.mobile.mvp.activity;

import android.os.Bundle;
import com.google.inject.Inject;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.View;
import roboguice.activity.RoboAccountAuthenticatorActivity;

public abstract class AccountAuthenticatorActivity<V extends View, T extends Presenter<V, A>, A> extends RoboAccountAuthenticatorActivity {

    @Inject
    protected T presenter;
    private boolean registered;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.presenter.injectView((V) this);

        BusProvider.get().register(this);
        this.registered = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!this.registered) {
            BusProvider.get().register(this);
            this.registered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        BusProvider.get().unregister(this);
        this.registered = false;
    }

}
