package org.monet.space.mobile.gcm;


import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.inject.Key;

import org.monet.space.mobile.R;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.model.Preferences;

import java.util.HashMap;
import java.util.Map;

import roboguice.util.RoboContext;

public class MonetRegistrationIntentService extends IntentService implements RoboContext {


    protected HashMap<Key<?>,Object> scopedObjects = new HashMap<Key<?>, Object>();

    private static final String TAG = "MonetRegistrationIntentService";


    public MonetRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Preferences preferences = new Preferences(this);
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.debug("Registration Token" + token);

            preferences.setCgmToken(token);
        } catch (Exception e) {
            preferences.setCgmToken("");
        }
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }
}
