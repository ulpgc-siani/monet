package org.monet.space.mobile.activity;

import org.monet.space.mobile.R;
import org.monet.space.mobile.helpers.RouterHelper;
import org.monet.space.mobile.mvp.View;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.presenter.PickTermPresenter;
import org.monet.space.mobile.view.PickTermView;

import android.os.Bundle;

import com.google.inject.Inject;

public class PickTermContainerActivity extends Activity<PickTermView, PickTermPresenter, Void> implements View {

	@Inject
	RouterHelper routerHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_picktermcontainer);
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				routerHelper.goUpToTaskList();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}

}
