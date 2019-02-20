package org.monet.space.mobile.activity;


import android.app.SearchManager;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;

import com.squareup.otto.Subscribe;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.*;
import org.monet.space.mobile.fragment.TaskDetailsFragment;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.helpers.LocationHelper;
import org.monet.space.mobile.helpers.RouterHelper;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.presenter.TaskPresenter;
import org.monet.space.mobile.view.TaskView;
import org.monet.space.mobile.widget.StaticItemWithCursorAdapter;

import java.io.File;

public class TaskActivity extends Activity<TaskView, TaskPresenter, Void> implements TaskView, ActionBar.OnNavigationListener {

    private ActionBar actionBar;
    private CursorAdapter sourceListAdapter;
    private boolean isDualPane = false;
    private Intent lastIntent = null;
    private boolean lastIntentHasChange = false;
    private boolean showChatPage;
    private ServiceConnection locationServiceConnection;

    private void loadSourcesInActionBar() {
        SparseArray<String> allSources = new SparseArray<>();
        allSources.put(android.R.id.text1, this.getString(R.string.all));
        sourceListAdapter = new StaticItemWithCursorAdapter(this, R.layout.simple_navigation_menu_item, null, new String[]{"label", "subtitle"}, new int[]{android.R.id.text1, android.R.id.text2}, allSources, R.id.title, R.string.tasks_navigation_menu_title);
        actionBar.setListNavigationCallbacks(sourceListAdapter, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_task);

        prepareActionBar();
        loadSourcesInActionBar();

        View taskDetailsFrame = findViewById(R.id.taskdetails);
        isDualPane = taskDetailsFrame != null && taskDetailsFrame.getVisibility() == View.VISIBLE;

        showChatPage = getIntent().getBooleanExtra(RouterHelper.SHOW_CHAT_PAGE, false);

        presenter.initialize();
    }

    private void prepareActionBar() {
        this.actionBar = this.getSupportActionBar();;
        this.actionBar.setDisplayShowTitleEnabled(false);
        this.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        File appIcon = LocalStorage.getAppIconFile(this);
        if (appIcon.exists()) {
            Drawable icon = Drawable.createFromPath(appIcon.getAbsolutePath());
            actionBar.setIcon(icon);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.locationServiceConnection == null)
            this.locationServiceConnection = LocationHelper.bindLocationService(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.locationServiceConnection != null) {
            LocationHelper.unbindLocationService(this, this.locationServiceConnection);
            this.locationServiceConnection = null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        lastIntent = intent;
        lastIntentHasChange = true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (!lastIntentHasChange) return;
        if (Intent.ACTION_SEARCH.equals(lastIntent.getAction()))
            BusProvider.get().post(new SearchEvent(lastIntent.getStringExtra(SearchManager.QUERY)));
        lastIntentHasChange = false;
    }

    @Subscribe
    public void showLoading(TaskLoadingEvent event) {
        this.setProgressBarIndeterminateVisibility(true);
    }

    @Subscribe
    public void hideLoading(TaskLoadedEvent event) {
        this.setProgressBarIndeterminateVisibility(false);
    }

    @Subscribe
    public void showLoading(StartLoadingEvent event) {
        this.setProgressBarIndeterminateVisibility(true);
    }

    @Subscribe
    public void hideLoading(FinishLoadingEvent event) {
        this.setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        BusProvider.get().post(new SourceChangedEvent(itemId));
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.restart();
    }

    @Subscribe
    public void onTaskDeselected(TaskDeselectedEvent event) {
        if (!isDualPane) return;
        Fragment taskDetailsFragment = getSupportFragmentManager().findFragmentById(R.id.taskdetails);
        if (taskDetailsFragment == null) return;
        getSupportFragmentManager()
                .beginTransaction()
                .remove(taskDetailsFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commitAllowingStateLoss();
    }

    @Subscribe
    public void onTaskSelected(TaskSelectedEvent event) {
        if (this.isDualPane) {
            Fragment currentDetails = this.getSupportFragmentManager().findFragmentById(R.id.taskdetails);
            boolean isFirst = currentDetails == null;
            if (currentDetails != null && ((TaskDetailsFragment) currentDetails).getTaskId() == event.id) return;

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.taskdetails, TaskDetailsFragment.build(event.id, showChatPage))
                    .setTransition(isFirst ? FragmentTransaction.TRANSIT_FRAGMENT_OPEN : FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .commitAllowingStateLoss();
        } else {
            this.presenter.open(event.id, showChatPage);
        }
    }

    @Subscribe
    public void onDetailsPageChangedEvent(DetailsPageChangedEvent event) {
        this.showChatPage = event.isShowingChatPage();
    }

    @Override
    public CursorAdapter getSourceListAdapter() {
        return this.sourceListAdapter;
    }

    public boolean isDualPane() {
        return this.isDualPane;
    }

}
