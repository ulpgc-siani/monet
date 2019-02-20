package org.monet.space.mobile.mvp;

import org.monet.space.mobile.helpers.RouterHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;

import com.google.inject.Inject;

public class Presenter<T extends View, A> {

    private static final int VIEW_TYPE_ACTIVITY = 0;
    private static final int VIEW_TYPE_FRAGMENT_ACTIVITY = 1;
    private static final int VIEW_TYPE_FRAGMENT = 2;

    protected Context context;
    protected T view;
    protected A args;

    private Activity activity;
    private FragmentActivity fragmentActivity;
    private Fragment fragment;

    private int viewType;

    @Inject
    protected RouterHelper routerHelper;

    @Inject
    public void injectContext(Context context) {
        this.context = context;
    }

    public void init(A args) {
        this.args = args;
    }

    public void injectView(T view) {
        this.view = view;

        if (view instanceof Fragment) {
            this.fragment = (Fragment) view;
            this.activity = this.fragment.getActivity();
            this.viewType = VIEW_TYPE_FRAGMENT;
        } else if (view instanceof FragmentActivity) {
            this.fragmentActivity = (FragmentActivity) view;
            this.activity = (Activity) view;
            this.viewType = VIEW_TYPE_FRAGMENT_ACTIVITY;
        } else {
            this.activity = (Activity) view;
            this.viewType = VIEW_TYPE_ACTIVITY;
        }
    }

    public LoaderManager getLoaderManager() {
        if (viewType == VIEW_TYPE_ACTIVITY)
            throw new RuntimeException("Normal activity doesn't have LoaderManager, use Activity instead.");
        if (viewType == VIEW_TYPE_FRAGMENT_ACTIVITY)
            return fragmentActivity.getSupportLoaderManager();
        if (viewType == VIEW_TYPE_FRAGMENT)
            return fragment.getLoaderManager();
        throw new RuntimeException("Unknown view type");
    }

    public Intent getIntent() {
        if (viewType == VIEW_TYPE_ACTIVITY || viewType == VIEW_TYPE_FRAGMENT_ACTIVITY)
            return activity.getIntent();
        if (viewType == VIEW_TYPE_FRAGMENT)
            return fragment.getActivity().getIntent();
        throw new RuntimeException("Unknown view type");
    }

    public Bundle getArgs() {
        if (viewType == VIEW_TYPE_ACTIVITY || viewType == VIEW_TYPE_FRAGMENT_ACTIVITY)
            return activityArgs(activity);
        if (viewType == VIEW_TYPE_FRAGMENT)
            return fragmentArgs();
        throw new RuntimeException("Unknown view type");
    }

    private Bundle activityArgs(Activity activity) {
        if (activity.getIntent().getExtras() == null)
            return new Bundle();
        return activity.getIntent().getExtras();
    }

    private Bundle fragmentArgs() {
        if (fragment.getArguments() != null)
            return fragment.getArguments();
        return activityArgs(fragment.getActivity());
    }

    protected void finish() {
        activity.finish();
    }
}
