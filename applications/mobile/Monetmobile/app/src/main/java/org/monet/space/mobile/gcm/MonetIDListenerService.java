package org.monet.space.mobile.gcm;

import android.content.Intent;

public class MonetIDListenerService extends com.google.android.gms.iid.InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, MonetRegistrationIntentService.class);
        startService(intent);
    }
}
