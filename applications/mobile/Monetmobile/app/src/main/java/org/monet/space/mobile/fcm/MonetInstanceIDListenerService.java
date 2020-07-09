package org.monet.space.mobile.fcm;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MonetInstanceIDListenerService extends FirebaseInstanceIdService {
        final String TAG = "MonetIdFirebaseMessage";
        @Override
        public void onTokenRefresh() {
            // Get updated InstanceID token.
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            // TODO: Implement this method to send any registration to your app's servers.
            Intent intent = new Intent(this, MonetRegistrationIntentService.class);
            startService(intent);
            //sendRegistrationToServer(refreshedToken);
        }

}
