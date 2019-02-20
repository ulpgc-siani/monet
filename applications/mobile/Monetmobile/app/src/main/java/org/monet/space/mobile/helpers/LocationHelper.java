package org.monet.space.mobile.helpers;

import org.monet.space.mobile.locationservice.LocationService;
import org.monet.space.mobile.model.Coordinate;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

public class LocationHelper {

    public static Coordinate getLastKnownLocation(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);

        Location lastLocationGPS = null;
        Location lastLocationNetwork = null;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if ((lastLocationGPS == null) && (lastLocationNetwork == null)) {
            return new Coordinate(Coordinate.DEFAULT_LATITUDE, Coordinate.DEFAULT_LONGITUDE);
        }

        if (lastLocationGPS == null) {
            return new Coordinate(lastLocationNetwork.getLatitude(), lastLocationNetwork.getLongitude());
        }

        if (lastLocationNetwork == null) {
            return new Coordinate(lastLocationGPS.getLatitude(), lastLocationGPS.getLongitude());
        }

        if (lastLocationGPS.getTime() >= lastLocationNetwork.getTime()) {
            return new Coordinate(lastLocationGPS.getLatitude(), lastLocationGPS.getLongitude());
        } else {
            return new Coordinate(lastLocationNetwork.getLatitude(), lastLocationNetwork.getLongitude());
        }
    }

    public static ServiceConnection bindLocationService(Context context) {
        try {
            Intent intent = new Intent(context, LocationService.class);
            ServiceConnection conn = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                }

            };
            context.bindService(intent, conn, Service.BIND_AUTO_CREATE);
            return conn;
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }

    public static void unbindLocationService(Context context, ServiceConnection conn) {
        try {
            context.unbindService(conn);
        } catch (Exception e) {
            Log.error(e);
        }
    }

}
