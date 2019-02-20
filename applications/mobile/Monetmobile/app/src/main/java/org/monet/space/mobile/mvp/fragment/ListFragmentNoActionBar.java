package org.monet.space.mobile.mvp.fragment;

import android.os.Bundle;
import android.support.v4.app.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.google.inject.Inject;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.mvp.View;

import roboguice.RoboGuice;
import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectView;

public abstract class ListFragmentNoActionBar<V extends View, T extends Presenter<V, A>, A> extends android.support.v4.app.ListFragment {

    @Inject
    protected T presenter;

    @InjectView(android.R.id.list)
    private ListView listView;

    private boolean registered;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectMembers(this);

        presenter.injectView((V) this);

        BusProvider.get().register(this);
        registered = true;
    }

    @Override
    public void onViewCreated(android.view.View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectViewMembers(this);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, android.view.View v, int position, long id) {
                onListItemClick((ListView) l, v, position, id);
            }

        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> l, android.view.View v, int position, long id) {
                return onListItemLongClick((ListView) l, v, position, id);
            }
        });
    }

    public void onListItemClick(ListView l, android.view.View v, int position, long id) {
    }

    public boolean onListItemLongClick(ListView l, android.view.View v, int position, long id) {
        return false;
    }

    public ListView getListView() {
        return this.listView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (registered) return;
        BusProvider.get().register(this);
        registered = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        BusProvider.get().unregister(this);
        registered = false;
    }

}
