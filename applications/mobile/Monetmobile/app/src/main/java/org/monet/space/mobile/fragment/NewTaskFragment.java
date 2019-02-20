package org.monet.space.mobile.fragment;

import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.monet.space.mobile.R;
import org.monet.space.mobile.mvp.content.SeparatedListAdapter;
import org.monet.space.mobile.mvp.fragment.ListFragment;
import org.monet.space.mobile.presenter.NewTaskPresenter;
import org.monet.space.mobile.view.NewTaskView;

import java.util.Map;
import java.util.Map.Entry;

public class NewTaskFragment extends ListFragment<NewTaskView, NewTaskPresenter, Void> implements NewTaskView {

    private boolean isStarted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newtask, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.initialize();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isStarted)
            presenter.restart();
        isStarted = true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long idDefinition) {
        presenter.newTask(idDefinition);
    }

    @Override
    public void setDefinitions(Map<String, CursorAdapter> data) {
        SeparatedListAdapter adapter = new SeparatedListAdapter(getActivity(), R.layout.new_task_section);
        for (Entry<String, CursorAdapter> entry : data.entrySet()) {
            if (entry.getValue().getCount() > 0)
                adapter.addSection(entry.getKey(), entry.getValue());
        }
        setListAdapter(adapter);
    }
}
