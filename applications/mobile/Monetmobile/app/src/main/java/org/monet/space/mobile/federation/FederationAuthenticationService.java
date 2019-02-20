package org.monet.space.mobile.federation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.inject.Key;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import roboguice.util.RoboContext;

public class FederationAuthenticationService extends Service implements RoboContext {

    private FederationAccountAuthenticator authenticator;
    private Map<Key<?>,Object> scopedObjects = new HashMap<Key<?>, Object>();

    @Override
    public void onCreate() {
        super.onCreate();
        this.authenticator = new FederationAccountAuthenticator(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return this.authenticator.getIBinder();
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }
}
