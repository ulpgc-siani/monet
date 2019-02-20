package org.monet.space.mobile.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.mobile.R;
import org.monet.space.mobile.adapter.ParentPickCheckAdapter;
import org.monet.space.mobile.model.schema.Check;
import org.monet.space.mobile.mvp.fragment.ListFragmentNoActionBar;
import org.monet.space.mobile.presenter.PickCheckPresenter;
import org.monet.space.mobile.view.PickCheckView;

import java.util.ArrayList;

public class PickCheckFragment extends ListFragmentNoActionBar<PickCheckView, PickCheckPresenter, Void> implements PickCheckView, OnClickListener {

    private boolean isStarted = false;
    private Flatten flatten;
    private ParentPickCheckAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_check, null);

        Button buttonAccept = (Button) view.findViewById(R.id.button_ok);
        buttonAccept.setOnClickListener(this);

        Button buttonSelectAll = (Button) view.findViewById(R.id.button_select_all);
        buttonSelectAll.setOnClickListener(this);
        Button buttonSelectNone = (Button) view.findViewById(R.id.button_select_none);
        buttonSelectNone.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.presenter.initialize();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!this.isStarted) {
            this.presenter.restart();
        }
        this.isStarted = true;
    }

    @Override
    public void setFlatten(Flatten flatten) {
        this.flatten = flatten;
    }

    @Override
    public void setTerms(Cursor cursor) {
        ArrayList<Check> checkedChecks = this.getArgs().getParcelableArrayList(PickCheckPresenter.VALUE);
        String glossaryCode = this.getArgs().getString(PickCheckPresenter.GLOSSARY_CODE);

        ExpandableListView listView = (ExpandableListView) this.getListView();
        this.adapter = new ParentPickCheckAdapter(this.getActivity(), listView, cursor, checkedChecks, flatten, (glossaryCode == null ? ParentPickCheckAdapter.SaveMode.SAVE_ALL : ParentPickCheckAdapter.SaveMode.SAVE_CHECKED));
        listView.setAdapter(this.adapter);
        this.adapter.expandGroups();
    }

    public Bundle getArgs() {
        if (this.getArguments() == null)
            return this.getActivity().getIntent().getExtras();
        return this.getArguments();
    }

    @Override
    public void setResult(int result, Intent intent) {
        this.getActivity().setResult(result, intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                this.presenter.accept(this.adapter.getChecksToSave());
                break;

            case R.id.button_select_all:
                this.adapter.selectAll();
                break;

            case R.id.button_select_none:
                this.adapter.selectNone();
                break;

        }
    }


}
