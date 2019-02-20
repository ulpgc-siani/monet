package org.monet.space.mobile.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.monet.space.mobile.R;
import org.monet.space.mobile.mvp.fragment.MapFragment;
import org.monet.space.mobile.presenter.PickPositionPresenter;
import org.monet.space.mobile.view.PickPositionView;

import java.util.List;

public class PickPositionFragment extends MapFragment<PickPositionView, PickPositionPresenter, Void> implements PickPositionView, OnMapClickListener, OnMapLongClickListener, OnInfoWindowClickListener {

    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String LABEL = "label";

    private Geocoder geocoder;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    map.setMyLocationEnabled(true);
                map.setOnMapClickListener(PickPositionFragment.this);
                map.setOnMapLongClickListener(PickPositionFragment.this);
                map.setOnInfoWindowClickListener(PickPositionFragment.this);
            }
        });

        this.geocoder = new Geocoder(this.getActivity());

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap map) {
                        Intent intent = getActivity().getIntent();
                        if (intent != null) {
                            String title = intent.getStringExtra(LABEL);
                            double longitude = intent.getDoubleExtra(LONGITUDE, Double.MIN_VALUE);
                            double latitude = intent.getDoubleExtra(LATITUDE, Double.MIN_VALUE);

                            if (longitude == Double.MIN_VALUE) {
                                panToUserPosition();
                            } else {
                                LatLng position = new LatLng(latitude, longitude);
                                addSelectedPoint(title, position);
                                map.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder().include(position).build(), 100));
                            }
                        }

                    }
                });
            }
        }, 200);
    }

    @Override
    public void onMapClick(LatLng position) {
        this.addSelectedPoint(null, position);
    }

    @Override
    public void onMapLongClick(LatLng position) {
        this.addSelectedPoint(null, position);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        LatLng position = marker.getPosition();

        Intent intent = new Intent("monet.action.PICK_POSITION");
        intent.putExtra(LATITUDE, position.latitude);
        intent.putExtra(LONGITUDE, position.longitude);
        intent.putExtra(LABEL, marker.getTitle());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void addSelectedPoint(final String title, final LatLng position) {
        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.clear();

                String titleInt = title;
                if (titleInt == null || titleInt.length() == 0) {
                    StringBuilder builder = new StringBuilder();
                    try {
                        List<Address> addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                builder.append(address.getAddressLine(i));
                                if (i < address.getMaxAddressLineIndex() - 1)
                                    builder.append(", ");
                            }
                        }
                    } catch (Exception e) {
                    }
                    titleInt = builder.toString();
                }

                String description = getActivity().getString(R.string.default_point_description, position.latitude, position.longitude);
                if (titleInt.length() == 0)
                    titleInt = description;

                Marker marker = map.addMarker(new MarkerOptions().position(position).title(titleInt).snippet(description));
                marker.showInfoWindow();
            }
        });

    }

    private void panToUserPosition() {
        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                Location myLocation = map.getMyLocation();
                if (myLocation != null)
                    map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
            }
        });
    }

}
