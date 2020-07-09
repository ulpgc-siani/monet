package org.monet.space.mobile.fcm;

//import android.app.NotificationChannel;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.monet.mobile.service.PushOperations;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.helpers.SyncAccountHelper;
import org.monet.space.mobile.model.Preferences;

import java.util.Map;

public class MonetFireBaseMessagingService extends FirebaseMessagingService{
    final String TAG = "MonetFirebaseMessage";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Map data = message.getData();

        String opName = data.get("operation").toString();
        String messageBusinessUid = data.get("business_unit_uid").toString();

        if (opName == null) return;
        PushOperations pushOp = PushOperations.valueOf(opName);

        String accountBusinessUid;

        AccountManager accountManager = AccountManager.get(this);
        for (Account account : accountManager.getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE)) {
            accountBusinessUid = accountManager.getUserData(account, FederationAccountAuthenticator.FEDERATION_URL) + accountManager.getUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT);
            if (accountBusinessUid.equals(messageBusinessUid) || (pushOp == PushOperations.PASSWORD_RESET))
                performOperation(pushOp, account);
        }





       // Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Toast.makeText("Ha llegado Notificacion","notificacion recibida");
        //notification.getTag()

/*
        String opName = data.getString("operation");
        String messageBusinessUid = data.getString("business_unit_uid");

        if (opName == null) return;
        PushOperations pushOp = PushOperations.valueOf(opName);

        String accountBusinessUid;

        AccountManager accountManager = AccountManager.get(this);
        for (Account account : accountManager.getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE)) {
            accountBusinessUid = accountManager.getUserData(account, FederationAccountAuthenticator.FEDERATION_URL) + accountManager.getUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT);
            if (accountBusinessUid.equals(messageBusinessUid) || (pushOp == PushOperations.PASSWORD_RESET))
                performOperation(pushOp, account);
        }
*/

    }

    private void performOperation(PushOperations operation, Account account) {
        switch (operation) {
            case DEFINITION_UPDATE:
                SyncAccountHelper.syncAccountDefinitions(account);
                break;

            case GLOSSARY_UPDATE:
                SyncAccountHelper.syncAccountGlossaries(account);
                break;

            case CHAT_UPDATE:
                SyncAccountHelper.syncAccountChats(account);
                break;

            case TASK_UPDATE:
                SyncAccountHelper.syncAccountTasks(account);
                break;

            case PASSWORD_RESET:
                SyncAccountHelper.passwordChanged(getApplicationContext(), account);
                break;
        }
    }
/*
    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
        Preferences preferences = new Preferences(this);
        try {
            org.monet.space.mobile.helpers.Log.debug("Registration Token" + s);
            preferences.setCgmToken(s);
        } catch (Exception e) {
            preferences.setCgmToken("");
        }
    }
*/

}
