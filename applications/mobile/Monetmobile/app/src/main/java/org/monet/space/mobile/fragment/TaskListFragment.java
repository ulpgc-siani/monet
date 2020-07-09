package org.monet.space.mobile.fragment;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.monet.space.mobile.R;
import org.monet.space.mobile.activity.TaskActivity;
import org.monet.space.mobile.adapter.TaskListAdapter;
import org.monet.space.mobile.events.*;
import org.monet.space.mobile.helpers.DateUtils;
import org.monet.space.mobile.helpers.LocationHelper;
import org.monet.space.mobile.helpers.NotificationHelper;
import org.monet.space.mobile.helpers.RouterHelper;
import org.monet.space.mobile.model.Coordinate;
import org.monet.space.mobile.model.TaskTray;
import org.monet.space.mobile.model.TasksSorting;
import org.monet.space.mobile.mvp.fragment.ListFragment;
import org.monet.space.mobile.presenter.TaskListPresenter;
import org.monet.space.mobile.provider.SuggestionsProvider;
import org.monet.space.mobile.view.TaskListView;
import org.monet.space.mobile.viewholders.TaskListItemViewHolder;

import roboguice.inject.InjectView;

import java.util.Date;

public class TaskListFragment extends ListFragment<TaskListView, TaskListPresenter, Void> implements TaskListView, OnClickListener {

    private static final String KEY_SOURCE_ID = "sourceId";
    private static final String KEY_CURRENT_TRAY = "currentTray";
    private static final String KEY_SELECTED_POSITION = "selectedPosition";
    private static final String KEY_SORTING_CRITERIA = "sortingCriteria";
    private static final String KEY_SORTING_MODE = "sortingMode";
    private static final String KEY_SEARCH_FILTER = "searchFilter";

    private static final String TAG_SORTING = "sorting";

    @InjectView(R.id.sync_text)
    private TextView syncTextView;

    @InjectView(android.R.id.empty)
    private TextView emptyText;

    @InjectView(R.id.radio_button_assigned)
    private RadioButton radioButtonAssigned;
    @InjectView(R.id.radio_button_finished)
    private RadioButton radioButtonFinished;
    @InjectView(R.id.radio_button_available)
    private RadioButton radioButtonAvailable;

    private Boolean syncInProgress = false;

    private TaskListAdapter taskListAdapter;
    private SearchRecentSuggestions searchSuggestions;

    private long selectedId = -1;
    private TaskTray currentTray = TaskTray.ASSIGNED;
    private TasksSorting sorting = null;
    private Long sourceId = null;
    private String searchFilter = null;
    private boolean isDualPane = false;

    private Coordinate currentCoordinate = new Coordinate(Coordinate.DEFAULT_LATITUDE, Coordinate.DEFAULT_LONGITUDE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tasklist, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioButtonAssigned.setOnClickListener(this);
        radioButtonFinished.setOnClickListener(this);
        radioButtonAvailable.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio_button_assigned:
            case R.id.radio_button_finished:
            case R.id.radio_button_available:
                this.tabChanged(v.getId());
                break;
        }
    }

    @Subscribe
    public void onSyncStartedEvent(SyncStartedEvent event) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                synchronized (TaskListFragment.this) {
                    syncInProgress = true;
                    syncTextView.setText(R.string.sync_updating);
                }
            }
        });
    }

    @Subscribe
    public void onSyncEndedEvent(SyncEndedEvent event) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                synchronized (TaskListFragment.this) {
                    if (syncInProgress) {
                        refreshLastUpdateDate(new Date());
                        syncInProgress = false;
                    }
                }
            }

        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.searchSuggestions = new SearchRecentSuggestions(getActivity(), SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);

        this.loadState(savedInstanceState);

        this.setHasOptionsMenu(true);

        this.isDualPane = ((TaskActivity) this.getActivity()).isDualPane();

        this.currentCoordinate = LocationHelper.getLastKnownLocation(getActivity());

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(RouterHelper.TASK_ID)) {
            this.selectedId = intent.getLongExtra(RouterHelper.TASK_ID, -1);
            if (!isDualPane) {
                this.presenter.openTask(this.selectedId);
                this.getActivity().finish();
            }
        }

        initAdapters();
        NotificationHelper.cancelTaskNotifications(getActivity());
        this.presenter.initialize();
    }

    private void loadState(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;

        this.selectedId = savedInstanceState.getLong(KEY_SELECTED_POSITION, -1);

        if (savedInstanceState.containsKey(KEY_CURRENT_TRAY))
            this.currentTray = TaskTray.values()[savedInstanceState.getInt(KEY_CURRENT_TRAY)];

        if (savedInstanceState.containsKey(KEY_SOURCE_ID))
            this.sourceId = savedInstanceState.getLong(KEY_SOURCE_ID);

        if (savedInstanceState.containsKey(KEY_SEARCH_FILTER))
            this.searchFilter = savedInstanceState.getString(KEY_SEARCH_FILTER);

        if (savedInstanceState.containsKey(KEY_SORTING_CRITERIA) && savedInstanceState.containsKey(KEY_SORTING_MODE))
            this.sorting = new TasksSorting(savedInstanceState.getInt(KEY_SORTING_CRITERIA), TasksSorting.SortMode.valueOf(savedInstanceState.getString(KEY_SORTING_MODE)));
    }

    private void initAdapters() {
        taskListAdapter = new TaskListAdapter((TaskActivity) getActivity(), getListView(), emptyText);
        setListAdapter(taskListAdapter);
        taskListAdapter
                .taskTray(currentTray)
                .taskListPresenter(presenter)
                .currentCoordinate(currentCoordinate)
                .selectedId(selectedId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(KEY_SELECTED_POSITION, this.selectedId);
        outState.putInt(KEY_CURRENT_TRAY, currentTray.ordinal());

        if (this.sourceId != null)
            outState.putLong(KEY_SOURCE_ID, this.sourceId);

        if (this.searchFilter != null && this.searchFilter.length() > 0)
            outState.putString(KEY_SEARCH_FILTER, this.searchFilter);

        if (this.sorting != null) {
            outState.putInt(KEY_SORTING_CRITERIA, this.sorting.getSortCriteria());
            outState.putString(KEY_SORTING_MODE, this.sorting.getSortMode().toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if ((this.selectedId != -1) && this.isDualPane && (getListView().getSelectedItemId() != this.selectedId))
            this.presenter.openTask(selectedId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasklist, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        //TODO Salir del paso
/*
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (searchFilter != null) {
                    searchFilter = null;
                    presenter.refresh();
                }
                return true;
            }
        });
*/
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        SearchableInfo info = searchManager.getSearchableInfo(getActivity().getComponentName());
        searchView.setSearchableInfo(info);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryRefinementEnabled(true);
        searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    searchView.setQuery(searchView.getQuery().toString(), false);
            }

        });
    }

    @Subscribe
    public void onSearchEvent(SearchEvent event) {
        this.resetSelectedPosition();

        this.searchFilter = event.query;
        presenter.refresh();

        searchSuggestions.saveRecentQuery(this.searchFilter, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sync:
                this.presenter.sync();
                break;
            case R.id.menu_new:
                this.presenter.newTask();
                break;
            case R.id.menu_sort:
                ChooseSortingDialogFragment.create(this.sorting).show(getFragmentManager(), TAG_SORTING);
                break;
            case R.id.menu_view_in_map:
                this.presenter.viewInMap();
                break;
            case R.id.menu_settings:
                this.presenter.openSettings();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void tabChanged(int tabId) {
        currentTray = TaskTray.forAction(tabId);
        taskListAdapter.taskTray(currentTray);
        this.resetSelectedPosition();
        this.presenter.refresh();
    }

    @Subscribe
    public void onSourceChanged(SourceChangedEvent event) {
        this.sourceId = event.id;
        this.resetSelectedPosition();
        this.presenter.refresh();
    }

    @Subscribe
    public void onNewTasks(NewTasksEvent event) {
        this.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                NotificationHelper.cancelTaskNotifications(getActivity());
                presenter.refresh();
            }
        });
    }

    @Subscribe
    public void onTaskSortingChanged(TaskSortingChangedEvent event) {
        this.sorting = event.newSorting;
        this.presenter.refresh();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (!(v.getTag() instanceof TaskListItemViewHolder)) return;
        TaskListItemViewHolder holder = (TaskListItemViewHolder) v.getTag();
        getListView().setItemChecked(position, holder.isChecked());
        if (isDualPane) {
            if (id == selectedId)
                return;
            selectedId = id;
        }
        taskListAdapter.selectedId(selectedId);
        this.presenter.openTask(id);
    }

    @Override
    public TaskListAdapter getTaskListAdapter() {
        return this.taskListAdapter;
    }

    @Override
    public void resetSelectedPosition() {
        this.selectedId = -1;
        taskListAdapter.selectedId(-1);
    }

    @Override
    public void selectTaskWithId(long id) {
        if (id == -1)
            selectedId = getListView().getItemIdAtPosition(0);
        else
            selectedId = id;
        if (isDualPane)
            presenter.openTask(selectedId);
        taskListAdapter.selectedId(selectedId);
    }

    @Override
    public void refreshLastUpdateDate(Date date) {
        if (date != null)
            syncTextView.setText(getString(R.string.sync_last_update, DateUtils.formatAsDateTime(getActivity(), date)));
        else
            syncTextView.setText(getString(R.string.not_synchronized));
    }

    @Override
    public Coordinate getCoordinate() {
        return currentCoordinate;
    }

    @Override
    public TasksSorting getSorting() {
        return sorting;
    }

    @Override
    public Long getSourceId() {
        return sourceId;
    }

    @Override
    public String getSearchFilter() {
        return searchFilter;
    }

    @Override
    public long getSelectedId() {
        return selectedId;
    }

    @Override
    public int getTaskTray() {
        return currentTray.ordinal();
    }

}
