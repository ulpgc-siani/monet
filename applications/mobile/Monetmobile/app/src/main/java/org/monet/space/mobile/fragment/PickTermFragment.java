package org.monet.space.mobile.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.mobile.R;
import org.monet.space.mobile.adapter.PickTermAdapter;
import org.monet.space.mobile.mvp.fragment.ListFragmentNoActionBar;
import org.monet.space.mobile.presenter.PickTermPresenter;
import org.monet.space.mobile.view.PickTermView;

public class PickTermFragment extends ListFragmentNoActionBar<PickTermView, PickTermPresenter, Void> implements PickTermView {

	private boolean isStarted = false;
	private Flatten flatten;
	private boolean isMaxDepth;
	private TextView emptyView;
	private PickTermAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pick_term, container, false);
		emptyView = (TextView) view.findViewById(android.R.id.empty);
		EditText filterView = (EditText) view.findViewById(R.id.filter);
		filterView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (adapter != null) adapter.getFilter().filter(s.toString());
			}
		});
		return view;
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
	public void onListItemClick(ListView l, View v, int position, long id) {
		this.presenter.open((Cursor) l.getItemAtPosition(position), false);
	}

	@Override
	public void setFlatten(Flatten flatten) {
		this.flatten = flatten;
	}

	@Override
	public void setTerms(Cursor cursor) {
		if (cursor == null || cursor.getCount() == 0) {
			emptyView.setText(R.string.term_list_empty);
			return;
		}

		adapter = new PickTermAdapter(getActivity(), cursor, presenter, getListView(), emptyView, flatten, isMaxDepth);
		setListAdapter(adapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		presenter.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void setResult(int result, Intent intent) {
		this.getActivity().setResult(result, intent);
	}

	@Override
	public void setIsMaxDepth(boolean isMaxDepth) {
		this.isMaxDepth = isMaxDepth;
	}

}
