package org.monet.space.mobile.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import com.squareup.otto.Subscribe;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.*;
import org.monet.space.mobile.fragment.TaskDetailsFragment;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.presenter.TaskMapPresenter;
import org.monet.space.mobile.view.TaskMapView;
import org.monet.space.mobile.widget.StaticItemWithCursorAdapter;

import roboguice.inject.InjectView;

import java.io.File;

public class TaskMapActivity extends Activity<TaskMapView, TaskMapPresenter, Void> implements TaskMapView, ActionBar.OnNavigationListener, OnClickListener {


    private ActionBar actionBar;
    private CursorAdapter sourceListAdapter;
    private Intent lastIntent;
    private boolean lastIntentHasChange;

    @InjectView(R.id.radio_button_assigned)
    private RadioButton radioButtonAssigned;
    @InjectView(R.id.radio_button_finished)
    private RadioButton radioButtonFinished;
    @InjectView(R.id.radio_button_available)
    private RadioButton radioButtonAvailable;

    RelativeLayout detailsPanel;
    ImageButton closeDetailsBtn;


    private void prepareActionBar() {
        this.actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        File appIcon = LocalStorage.getAppIconFile(this);
        if (appIcon.exists())
            actionBar.setIcon(Drawable.createFromPath(appIcon.getAbsolutePath()));
    }

    private void loadSourcesInActionBar() {
        SparseArray<String> allSources = new SparseArray<>();
        allSources.put(android.R.id.text1, this.getString(R.string.all));
        this.sourceListAdapter = new StaticItemWithCursorAdapter(this, R.layout.simple_navigation_menu_item, null, new String[]{"label"}, new int[]{android.R.id.text1}, allSources, R.id.title, R.string.tasks_navigation_menu_title);
        this.actionBar.setListNavigationCallbacks(this.sourceListAdapter, this);
    }

    private void initializeComponents() {
        detailsPanel = (RelativeLayout) findViewById(R.id.details_panel);
        closeDetailsBtn = (ImageButton) findViewById(R.id.closeDetails);
        if (closeDetailsBtn != null)
            closeDetailsBtn.setOnClickListener(this);

        radioButtonAssigned.setOnClickListener(this);
        radioButtonFinished.setOnClickListener(this);
        radioButtonAvailable.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_taskmap);
        this.initializeComponents();

        this.prepareActionBar();
        this.loadSourcesInActionBar();

        this.presenter.initialize();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        this.lastIntent = intent;
        this.lastIntentHasChange = true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        if (this.lastIntentHasChange) {
            if (Intent.ACTION_SEARCH.equals(this.lastIntent.getAction())) {
                String query = this.lastIntent.getStringExtra(SearchManager.QUERY);
                BusProvider.get().post(new SearchEvent(query));
                this.presenter.closeDetailsPanel();
            }
            this.lastIntentHasChange = false;
        }
    }

    @Subscribe
    public void onTaskSelected(TaskSelectedEvent event) {
        if (hasDetailsPanel()) {
            TaskDetailsFragment details = TaskDetailsFragment.build(event.id, false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.taskdetails, details);
            ft.commit();
            this.showDetailsPanel();
        } else {
            presenter.openTask(event.id);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        this.hideDetailsPanel();
        BusProvider.get().post(new SourceChangedEvent(itemId));
        return true;
    }

    @Override
    public CursorAdapter getSourceListAdapter() {
        return this.sourceListAdapter;
    }

    @Subscribe
    public void showLoading(StartLoadingEvent event) {
        this.setProgressBarIndeterminateVisibility(true);
    }

    @Subscribe
    public void hideLoading(FinishLoadingEvent event) {
        this.setProgressBarIndeterminateVisibility(false);
    }

    @Subscribe
    public void taskAbandoned(TaskAbandonedEvent event) {
        hideDetailsPanel();
    }

    @Override
    public void showDetailsPanel() {
        if (detailsPanel != null)
            this.detailsPanel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDetailsPanel() {
        if (detailsPanel != null)
            this.detailsPanel.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeDetails:
                this.presenter.closeDetailsPanel();
                break;

            case R.id.radio_button_assigned:
            case R.id.radio_button_finished:
            case R.id.radio_button_available:
                BusProvider.get().post(new TabChangedEvent(v.getId()));
                break;

            default:
                break;
        }
    }

    @Override
    public boolean hasDetailsPanel() {
        return this.detailsPanel != null;
    }

    public boolean isDetailsPanelOpen() {
        return this.detailsPanel != null && this.detailsPanel.getVisibility() == View.VISIBLE;
    }

}
