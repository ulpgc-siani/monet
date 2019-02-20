package org.monet.space.mobile.mvp.fragment;

import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.View;

import android.os.Bundle;

import com.google.inject.Inject;

import roboguice.RoboGuice;


public abstract class Fragment<V extends View, T extends Presenter<V, A>, A> extends android.support.v4.app.Fragment {

    @Inject
    protected T presenter;
    private boolean registered;
    private A args;

    protected void init(A args) {
        this.args = args;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectMembersWithoutViews(this);

        this.presenter.injectView((V) this);
        this.presenter.init(args);

        BusProvider.get().register(this);
        this.registered = true;
    }

    @Override
    public void onViewCreated(android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectViewMembers(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!this.registered) {
            BusProvider.get().register(this);
            this.registered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        BusProvider.get().unregister(this);
        this.registered = false;
    }

}
