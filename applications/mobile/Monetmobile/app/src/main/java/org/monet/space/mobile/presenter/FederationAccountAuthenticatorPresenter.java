package org.monet.space.mobile.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.webkit.URLUtil;
import android.widget.Toast;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;
import org.monet.mobile.service.results.HeloResult;
import org.monet.space.mobile.R;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.events.UntrustedServerEvent;
import org.monet.space.mobile.exception.ConnectionException;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.helpers.NotificationHelper;
import org.monet.space.mobile.helpers.SyncAccountHelper;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.net.ProxyLayer;
import org.monet.space.mobile.view.FederationAccountAuthenticatorView;

public class FederationAccountAuthenticatorPresenter extends Presenter<FederationAccountAuthenticatorView, Void> {

    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    @Inject
    private Repository repository;

    private String serverUrl;

    public void initialize() {
        String rawUsername = this.getIntent().getStringExtra(FederationAccountAuthenticatorPresenter.PARAM_USERNAME);
        if (rawUsername != null) {
            String[] components = rawUsername.split(FederationAccountAuthenticator.ACCOUNT_NAME_SPLITTER_REGEX);

            this.view.setUsername(components[0]);
            this.view.setServerUrl(components[1]);
        }
    }

    @Subscribe
    public void onUntrustedServer(UntrustedServerEvent event) {
        NotificationHelper.notifyUntrustedServer(context, this.serverUrl);
    }

    public void login(String serverUrl, String username, String password) {
        view.showLoading();

        BusProvider.get().register(this);
        try {
            this.serverUrl = serverUrl;
            new DoHeloToServerTask().execute(serverUrl, username, password);
        } finally {
            BusProvider.get().unregister(this);
        }
    }

    private class DoHeloToServerTask extends AsyncTask<String, String, Account> {

        private boolean hasError = false;
        private String errorMsg;

        private Account setupAccount(String serverUrl, String username, String password, HeloResult heloResult) {


            Account account = new Account(username + FederationAccountAuthenticator.ACCOUNT_NAME_SPLITTER + serverUrl, FederationAccountAuthenticator.ACCOUNT_TYPE);

            AccountManager accountManager = AccountManager.get(context);
            if (accountManager.addAccountExplicitly(account, password, null)) {
                SyncAccountHelper.setupAccount(context, account);

                repository.addSource(heloResult.getBusinessUnitLabel(), heloResult.getTitle(), heloResult.getSubtitle(), account.name, username);
            } else {
                accountManager.setPassword(account, password);
            }

            accountManager.setUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT, heloResult.getBusinessUnit());
            accountManager.setUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT_LABEL, heloResult.getBusinessUnitLabel());
            accountManager.setUserData(account, FederationAccountAuthenticator.FEDERATION, heloResult.getFederation());
            accountManager.setUserData(account, FederationAccountAuthenticator.FEDERATION_URL, heloResult.getFederationUrl());
            accountManager.setUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT_TITLE, heloResult.getTitle());
            accountManager.setUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT_SUBTITLE, heloResult.getSubtitle());

            return account;
        }

        private boolean checkConnection() {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) return false;
            if (!networkInfo.isConnected()) return false;
            if (!networkInfo.isAvailable()) return false;
            return true;
        }

        @Override
        protected Account doInBackground(String... params) {

            if (!this.checkConnection()) {
                this.hasError = true;
                this.errorMsg = context.getResources().getString(R.string.error_no_data_connection);
                return null;
            }

            String serverUrl = params[0];
            String username = params[1];
            String password = params[2];

            try {
                return setupAccount(serverUrl, username, password, ProxyLayer.hello(context, URLUtil.guessUrl(serverUrl)));

            } catch (ConnectionException ex) {
                this.hasError = true;
                this.errorMsg = context.getResources().getString(R.string.error_cant_connect_server);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Account account) {
            super.onPostExecute(account);

            if (hasError) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                view.showForm();
                return;
            }

            final Intent intent = new Intent()
                    .putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name)
                    .putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            view.setAccountAuthenticatorResult(intent.getExtras());
            view.setResult(Activity.RESULT_OK, intent);
            finish();
        }

    }

}
