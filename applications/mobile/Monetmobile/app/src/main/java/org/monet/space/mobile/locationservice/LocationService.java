package org.monet.space.mobile.locationservice;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

public class LocationService extends Service {
    private static final int LOCATION_INTERVAL = 20000;
    private static final float LOCATION_DISTANCE = 10;

    private LocationManager locationManager;

    private class LocationListener implements android.location.LocationListener {

        Location lastLocation;

        public LocationListener(String provider) {
            this.lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            this.lastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    private LocationListener[] locationListeners = new LocationListener[]{new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)};


    private void initializeLocationManager() {
        if (this.locationManager != null) return;

        this.locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        initializeLocationManager();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        try {
            this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, this.locationListeners[1]);
        } catch (Exception ignored) {
        }

        try {
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, this.locationListeners[0]);
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.locationManager == null) return;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        for (LocationListener locationListener : this.locationListeners) {
            try {
                this.locationManager.removeUpdates(locationListener);
            } catch (Exception e) {
            }
        }
    }
}

