package org.monet.space.mobile.activity;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.view.MenuItem;

import org.monet.space.mobile.R;
import org.monet.space.mobile.helpers.LocationHelper;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.presenter.NewTaskContainerPresenter;
import org.monet.space.mobile.view.NewTaskContainerView;

public class NewTaskContainerActivity extends Activity<NewTaskContainerView, NewTaskContainerPresenter, Void> implements NewTaskContainerView {

    private ServiceConnection locationServiceConnection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (locationServiceConnection == null)
            locationServiceConnection = LocationHelper.bindLocationService(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationServiceConnection == null) return;
        LocationHelper.unbindLocationService(this, locationServiceConnection);
        locationServiceConnection = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) return super.onOptionsItemSelected(item);
        presenter.up();
        return true;
    }
}
