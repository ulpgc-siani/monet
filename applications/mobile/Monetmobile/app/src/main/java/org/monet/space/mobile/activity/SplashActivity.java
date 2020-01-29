package org.monet.space.mobile.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.webkit.URLUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.monet.mobile.service.results.HeloResult;
import org.monet.space.mobile.R;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.exception.ConnectionException;
import org.monet.space.mobile.fcm.MonetRegistrationIntentService;
import org.monet.space.mobile.helpers.FontUtils;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.helpers.SyncAccountHelper;
import org.monet.space.mobile.model.SourceDetails;
import org.monet.space.mobile.net.ProxyLayer;

import roboguice.activity.RoboSplashActivity;

import javax.inject.Inject;
import java.io.FileOutputStream;

import static org.monet.space.mobile.federation.FederationAccountAuthenticator.*;

public class SplashActivity extends RoboSplashActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static int ACTIVITY_AUTHENTICATION = 1;

    @Inject
    private Repository repository;

    private void registerNewAccount() {
        Intent intent = new Intent(this, FederationAccountAuthenticatorActivity.class);
        this.startActivityForResult(intent, ACTIVITY_AUTHENTICATION);
    }

    private void syncAndShowTasks() {
        SyncAccountHelper.syncAllAccounts(this);
        startActivity(new Intent(this, TaskActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_phone);

        FontUtils.setRobotoFont(this, findViewById(android.R.id.content));

        changeAppIcon();

        if (checkPlayServices()) {
            Intent intent = new Intent(this, MonetRegistrationIntentService.class);
            startService(intent);
        }

        checkLocationPermission();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        this.startNextActivity();
    }

    private void changeAppIcon() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            deleteAppIcon();
            return;
        }

        byte[] b = extras.getByteArray("APP_ICON");
        if (b == null) {
            deleteAppIcon();
            return;
        }

        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

        try {
            FileOutputStream fOut = new FileOutputStream(LocalStorage.getAppIconFile(this));
            bmp.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
        }
    }

    private void deleteAppIcon() {
        LocalStorage.getAppIconFile(this).delete();
    }

    @Override
    protected void startNextActivity() {
        AccountManager manager = AccountManager.get(this);
        if (manager.getAccountsByType(ACCOUNT_TYPE).length == 0) {
            registerNewAccount();
            return;
        }
        if (manager.getUserData(manager.getAccountsByType(ACCOUNT_TYPE)[0], BUSINESS_UNIT_TITLE) == null)
            syncSources(manager.getAccountsByType(ACCOUNT_TYPE));
        syncAndShowTasks();
    }

    @Override
    protected void andFinishThisOne() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == ACTIVITY_AUTHENTICATION) && (resultCode == RESULT_OK))
            syncAndShowTasks();
    }

    private void syncSources(Account[] accounts) {
        new SyncSourcesTask(this).execute(accounts);
    }

    private class SyncSourcesTask extends AsyncTask<Account, Void, Void> {

        private final Context context;
        private final AccountManager manager;

        public SyncSourcesTask(Context context) {
            this.context = context;
            this.manager = AccountManager.get(context);
        }

        @Override
        protected Void doInBackground(Account... accounts) {
           // for (Account account : accounts)
                //syncAccount(account);
            return null;
        }

        private void syncAccount(Account account) {
            try {
                String url = manager.getUserData(account, FEDERATION_URL);
                url = url.substring(0, url.lastIndexOf("/")) + "/" + manager.getUserData(account, BUSINESS_UNIT);
                HeloResult result = ProxyLayer.hello(context, URLUtil.guessUrl(url));
                manager.setUserData(account, BUSINESS_UNIT_TITLE, result.getTitle());
                manager.setUserData(account, BUSINESS_UNIT_SUBTITLE, result.getSubtitle());

                int indexSeparator = account.name.indexOf(ACCOUNT_NAME_SPLITTER);
                String username = account.name.substring(0, indexSeparator);
                SourceDetails source = repository.getSourceOfUser(account.name);
                repository.updateSource(source.id, source.label, result.getTitle(), result.getSubtitle(), source.accountName, username);
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }
        }
    }

}
