package org.monet.space.mobile.mvp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;

import com.google.inject.Inject;
import com.google.inject.Key;

import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;
import roboguice.inject.RoboInjector;
import roboguice.util.RoboContext;


public abstract class Activity<V extends View, T extends Presenter<V, A>, A> extends AppCompatActivity implements RoboContext {

    protected HashMap<Key<?>,Object> scopedObjects = new HashMap<Key<?>, Object>();

    @Inject
    protected T presenter;
    private boolean registered;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        final RoboInjector injector = RoboGuice.getInjector(this);
        injector.injectMembersWithoutViews(this);

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

    @Override
    protected void onDestroy() {
        try {
            RoboGuice.destroyInjector(this);
        } finally {
            super.onDestroy();
        }
    }

    @Override
    public android.view.View onCreateView(String name, Context context, AttributeSet attrs) {
        if (Activity.shouldInjectOnCreateView(name))
            return Activity.injectOnCreateView(name, context, attrs);

        return super.onCreateView(name, context, attrs);
    }
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        RoboGuice.getInjector(this).injectViewMembers(this);
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }

    protected static boolean shouldInjectOnCreateView(String name) {
        return false; // && Character.isLowerCase(name.charAt(0)) && !name.startsWith("com.android") && !name.equals("fragment");
    }

    protected static android.view.View injectOnCreateView(String name, Context context, AttributeSet attrs) {
        try {
            final Constructor<?> constructor = Class.forName(name).getConstructor(Context.class, AttributeSet.class);
            final android.view.View view = (android.view.View) constructor.newInstance(context, attrs);
            RoboGuice.getInjector(context).injectMembers(view);
            RoboGuice.getInjector(context).injectViewMembers(view);
            return view;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
