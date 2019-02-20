package org.monet.space.mobile.activity;

import android.os.Bundle;
import com.google.inject.Inject;

import org.monet.space.mobile.R;
import org.monet.space.mobile.helpers.RouterHelper;

import roboguice.activity.RoboFragmentActivity;

public class PickCheckContainerActivity extends RoboFragmentActivity {

    @Inject
    RouterHelper routerHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_pickcheckcontainer);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        routerHelper.goUpToTaskList();
        return true;
    }

}
