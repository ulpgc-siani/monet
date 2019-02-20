package org.monet.space.mobile.fragment;

import android.Manifest;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.squareup.otto.Subscribe;

import org.monet.space.mobile.R;
import org.monet.space.mobile.activity.TaskMapActivity;
import org.monet.space.mobile.events.*;
import org.monet.space.mobile.helpers.LocationHelper;
import org.monet.space.mobile.helpers.NotificationHelper;
import org.monet.space.mobile.model.Coordinate;
import org.monet.space.mobile.model.TaskTray;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.fragment.MapFragment;
import org.monet.space.mobile.presenter.TaskMapListPresenter;
import org.monet.space.mobile.provider.SuggestionsProvider;
import org.monet.space.mobile.view.TaskMapListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TaskMapFragment extends MapFragment<TaskMapListView, TaskMapListPresenter, Void> implements TaskMapListView, OnInfoWindowClickListener, OnMarkerClickListener {

    private static final String KEY_SOURCE_ID = "sourceId";
    private static final String KEY_CURRENT_TRAY = "currentTray";
    private static final String KEY_SATELLITE_VIEW = "satelliteView";
    private static final String KEY_SEARCH_FILTER = "searchFilter";

    private boolean inSatelliteMapView = false;
    private Long sourceId;
    private TaskTray currentTray = TaskTray.ASSIGNED;
    private String searchFilter;
    private SearchRecentSuggestions suggestions;
    private Map<String, Long> markerTaskMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                suggestions = new SearchRecentSuggestions(TaskMapFragment.this.getActivity(), SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);

                map.setOnInfoWindowClickListener(TaskMapFragment.this);
                map.setOnMarkerClickListener(TaskMapFragment.this);
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    map.setMyLocationEnabled(true);

            }
        });
        this.loadState(savedInstanceState);

        NotificationHelper.cancelTaskNotifications(this.getActivity());

        this.presenter.initialize();
    }

    private void loadState(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;

        this.inSatelliteMapView = savedInstanceState.getBoolean(KEY_SATELLITE_VIEW);

        if (savedInstanceState.containsKey(KEY_CURRENT_TRAY))
            this.currentTray = TaskTray.values()[savedInstanceState.getInt(KEY_CURRENT_TRAY)];

        if (savedInstanceState.containsKey(KEY_SOURCE_ID))
            this.sourceId = savedInstanceState.getLong(KEY_SOURCE_ID);

        if (savedInstanceState.containsKey(KEY_SEARCH_FILTER))
            this.searchFilter = savedInstanceState.getString(KEY_SEARCH_FILTER);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_SATELLITE_VIEW, this.inSatelliteMapView);
        outState.putInt(KEY_CURRENT_TRAY, currentTray.ordinal());

        if (this.sourceId != null)
            outState.putLong(KEY_SOURCE_ID, this.sourceId);

        if (this.searchFilter != null && this.searchFilter.length() > 0)
            outState.putString(KEY_SEARCH_FILTER, this.searchFilter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.taskmap, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        BusProvider.get().post(new SearchEvent(null));
                        return true;
                    }
                });


/*
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                BusProvider.get().post(new SearchEvent(null));
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
                if (!hasFocus)
                    return;
                String query = searchView.getQuery().toString();
                searchView.setQuery(query, false);
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.presenter.up();
                break;
            case R.id.menu_sync:
                this.presenter.sync();
                break;
            case R.id.menu_switch_tiles:
                this.presenter.changeLayer();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Subscribe
    public void onTabChanged(TabChangedEvent event) {
        currentTray = TaskTray.forAction(event.tabId);
        this.presenter.refresh();
    }

    @Subscribe
    public void onSourceChangedEvent(SourceChangedEvent event) {
        this.sourceId = event.id;
        this.presenter.refresh();
    }

    @Override
    public void setOverlayItems(final ArrayList<TaskMapItem> items) {
        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.clear();
                markerTaskMap.clear();

                Builder boundsBuilder = LatLngBounds.builder();
                for (TaskMapItem item : items) {
                    LatLng position = new LatLng(item.latitude, item.longitude);

                    Marker marker = map.addMarker(new MarkerOptions().title(item.title).snippet(item.description).position(position));

                    markerTaskMap.put(marker.getId(), item.id);

                    boundsBuilder.include(position);
                }

                moveCamera(map, items.size() > 0, boundsBuilder);
            }
        });
    }

    private void moveCamera(GoogleMap map, Boolean hasTasks, Builder boundsBuilder) {
        if (hasTasks) {

            map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 20));

        } else {
            Coordinate coordinate = LocationHelper.getLastKnownLocation(this.getActivity());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(coordinate.getLat(), coordinate.getLon()))
                    .zoom(13)
                    .bearing(0)
                    .tilt(0)
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            Toast.makeText(this.getActivity(), R.string.no_tasks_founds, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Long taskId = this.markerTaskMap.get(marker.getId());
        if (taskId != null) {
            BusProvider.get().post(new TaskSelectedEvent(taskId));

            if (((TaskMapActivity) this.getActivity()).hasDetailsPanel())
                this.centerOnMarker(marker);
        }
    }

    @Override
    public void switchMapViewTiles() {
        this.inSatelliteMapView = !this.inSatelliteMapView;
        final int mapType;
        if (this.inSatelliteMapView)
            mapType = GoogleMap.MAP_TYPE_SATELLITE;
        else
            mapType = GoogleMap.MAP_TYPE_NORMAL;

        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.setMapType(mapType);
            }
        });
    }

    @Subscribe
    public void onSearchEvent(SearchEvent event) {
        searchFilter = event.query;
        presenter.refresh();
        suggestions.saveRecentQuery(event.query, null);
    }

    @Subscribe
    public void onNewTasks(NewTasksEvent event) {
        this.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                NotificationHelper.cancelTaskNotifications(getActivity());
            }

        });
    }

    @Override
    public long getSourceId() {
        return this.sourceId;
    }

    @Override
    public String getSearchFilter() {
        return this.searchFilter;
    }

    @Override
    public void panToUserPosition() {
        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                Location myLocation = map.getMyLocation();
                if (myLocation != null)
                    map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        centerOnMarker(marker);
        marker.showInfoWindow();
        return true;
    }

    @Override
    public int getTaskTray() {
        return currentTray.ordinal();
    }

    private void centerOnMarker(final Marker marker) {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                int width = getView().getWidth();

                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                Projection projection = map.getProjection();
                Point basePoint = projection.toScreenLocation(marker.getPosition());
                Point centerPoint = null;
                if (((TaskMapActivity) TaskMapFragment.this.getActivity()).isDetailsPanelOpen())
                    centerPoint = new Point(basePoint.x + (width >> 2), basePoint.y);
                else
                    centerPoint = basePoint;
                LatLng center = projection.fromScreenLocation(centerPoint);

                map.animateCamera(CameraUpdateFactory.newLatLng(center));
            }
        });

    }

}
