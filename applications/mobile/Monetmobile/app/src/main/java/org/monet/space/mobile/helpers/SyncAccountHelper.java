package org.monet.space.mobile.helpers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.model.Preferences;
import org.monet.space.mobile.model.SourceDetails;
import org.monet.space.mobile.provider.MonetDataProvider;
import org.monet.space.mobile.syncservice.SyncAdapter;
import org.monet.space.mobile.syncservice.SynchronizationFailure;

import java.util.Date;

public class SyncAccountHelper {

    public static final String TASKS_SYNC_MARKER_KEY = "org.monet.mobile.syncservice.task.marker";
    public static final String CHAT_SYNC_MARKER_KEY = "org.monet.mobile.syncservice.chat.marker";
    public static final String DEFINITION_SYNC_MARKER_KEY = "org.monet.mobile.syncservice.definition.marker";
    public static final String GLOSSARY_SYNC_MARKER_KEY = "org.monet.mobile.syncservice.glossary.marker";

    public static synchronized SourceDetails getSourceDetails(Account account, Repository repository, Context context) {
        SourceDetails source = repository.getSourceOfUser(account.name);
        if (source != null && source.title != null)
            return source;

        AccountManager accountManager = AccountManager.get(context);

        int indexSeparator = account.name.indexOf(FederationAccountAuthenticator.ACCOUNT_NAME_SPLITTER);
        String username = account.name.substring(0, indexSeparator);
        String businessUnitLabel = accountManager.getUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT_LABEL);
        String title = accountManager.getUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT_TITLE);
        String subtitle = accountManager.getUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT_SUBTITLE);

        if (source != null)
            repository.updateSource(source.id, businessUnitLabel, title, subtitle, account.name, username);
        else
            repository.addSource(businessUnitLabel, title, subtitle, account.name, username);

        SyncAccountHelper.resetSyncMarkers(context, account);

        return repository.getSourceOfUser(account.name);
    }

    public static void resetSyncMarkers(Context context, Account account) {
        AccountManager accountManager = AccountManager.get(context);

        accountManager.setUserData(account, TASKS_SYNC_MARKER_KEY, null);
        accountManager.setUserData(account, CHAT_SYNC_MARKER_KEY, null);
        accountManager.setUserData(account, DEFINITION_SYNC_MARKER_KEY, null);
        accountManager.setUserData(account, GLOSSARY_SYNC_MARKER_KEY, null);
    }

    public static void setServerSyncMarker(Context context, Account account, String marker, long markerValue) {
        AccountManager accountManager = AccountManager.get(context);

        accountManager.setUserData(account, marker, Long.toString(markerValue));
    }

    public static long getServerSyncMarker(Context context, Account account, String marker) {
        AccountManager accountManager = AccountManager.get(context);

        String markerValue = accountManager.getUserData(account, marker);
        if (!TextUtils.isEmpty(markerValue)) {
            return Long.parseLong(markerValue);
        }
        return 0;
    }

    public static Date getLastUpdateDate(Context context) {
        long updateTick = Long.MAX_VALUE;
        String accountDate;
        long marker;

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE);

        for (final Account account : accounts) {

            accountDate = accountManager.getUserData(account, TASKS_SYNC_MARKER_KEY);
            if (accountDate == null)
                marker = Long.MAX_VALUE;
            else
                marker = Long.parseLong(accountDate);
            if (marker < updateTick)
                updateTick = marker;

            accountDate = accountManager.getUserData(account, CHAT_SYNC_MARKER_KEY);
            if (accountDate == null)
                marker = Long.MAX_VALUE;
            else
                marker = Long.parseLong(accountDate);
            if (marker < updateTick)
                updateTick = marker;

            accountDate = accountManager.getUserData(account, GLOSSARY_SYNC_MARKER_KEY);
            if (accountDate == null)
                marker = Long.MAX_VALUE;
            else
                marker = Long.parseLong(accountDate);
            if (marker < updateTick)
                updateTick = marker;

            accountDate = accountManager.getUserData(account, DEFINITION_SYNC_MARKER_KEY);
            if (accountDate == null)
                marker = Long.MAX_VALUE;
            else
                marker = Long.parseLong(accountDate);
            if (marker < updateTick)
                updateTick = marker;

        }
        if (updateTick == Long.MAX_VALUE)
            return null;
        else
            return new Date(updateTick);
    }

    public static void setupAccount(Context context, Account account) {
        Preferences preferences = new Preferences(context);

        ContentResolver.setIsSyncable(account, MonetDataProvider.AUTHORITIES, 1);
        ContentResolver.setSyncAutomatically(account, MonetDataProvider.AUTHORITIES, true);
        ContentResolver.addPeriodicSync(account, MonetDataProvider.AUTHORITIES, new Bundle(), preferences.getSyncInterval());
    }

    private static void setupAccountSyncInterval(Preferences preferences, Account account) {
        ContentResolver.addPeriodicSync(account, MonetDataProvider.AUTHORITIES, new Bundle(), preferences.getSyncInterval());
    }

    public static void setupAllAccountsSyncInterval(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Preferences preferences = new Preferences(context);

        Account[] accounts = accountManager.getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE);
        for (Account account : accounts)
            setupAccountSyncInterval(preferences, account);
    }

    public static boolean isSyncEnabled(Account account) {
        return ContentResolver.getSyncAutomatically(account, MonetDataProvider.AUTHORITIES);
    }

    public static void enableSync(Context context, Account account) {
        ContentResolver.setSyncAutomatically(account, MonetDataProvider.AUTHORITIES, true);

        NotificationHelper.cancelSyncDisabledNotification(context, account.name);
    }

    public static void enableAllAccountsSync(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE);
        for (final Account account : accounts) {
            enableSync(context, account);
        }
    }

    public static void disableSync(Context context, Account account, boolean notify) {
        disableSync(context, account, notify, SynchronizationFailure.DEFAULT);
    }

    public static void disableSync(Context context, Account account, boolean notify, SynchronizationFailure failure) {
        ContentResolver.setSyncAutomatically(account, MonetDataProvider.AUTHORITIES, false);

        if (notify)
            NotificationHelper.notifySyncDisabled(context, account.name, context.getString(failure.causeStringId()));
    }

    @SuppressWarnings("deprecation")
    private static void syncAccount(Account account) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putInt(SyncAdapter.SYNC_MODE, SyncAdapter.SYNC_MODE_ALL);
        ContentResolver.requestSync(account, MonetDataProvider.AUTHORITIES, bundle);
    }

    public static void syncAllAccounts(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE);
        for (final Account account : accounts) {
            syncAccount(account);
        }
    }

    @SuppressWarnings("deprecation")
    public static void syncAccountTasks(Account account) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putInt(SyncAdapter.SYNC_MODE, SyncAdapter.SYNC_MODE_TASKS);
        ContentResolver.requestSync(account, MonetDataProvider.AUTHORITIES, bundle);
    }

    public static void syncAllAccountsTasks(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE);
        for (final Account account : accounts) {
            syncAccountTasks(account);
        }
    }

    @SuppressWarnings("deprecation")
    public static void syncAccountDefinitions(Account account) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putInt(SyncAdapter.SYNC_MODE, SyncAdapter.SYNC_MODE_DEFINITIONS);
        ContentResolver.requestSync(account, MonetDataProvider.AUTHORITIES, bundle);
    }

    public static void syncAllAccountsDefinitions(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE);
        for (final Account account : accounts) {
            syncAccountDefinitions(account);
        }
    }

    @SuppressWarnings("deprecation")
    public static void syncAccountGlossaries(Account account) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putInt(SyncAdapter.SYNC_MODE, SyncAdapter.SYNC_MODE_GLOSSARIES);
        ContentResolver.requestSync(account, MonetDataProvider.AUTHORITIES, bundle);
    }

    public static void syncAllAccountsGlossaries(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE);
        for (final Account account : accounts) {
            syncAccountGlossaries(account);
        }
    }

    @SuppressWarnings("deprecation")
    public static void syncAccountChats(Account account) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putInt(SyncAdapter.SYNC_MODE, SyncAdapter.SYNC_MODE_CHATS);
        ContentResolver.requestSync(account, MonetDataProvider.AUTHORITIES, bundle);
    }

    public static void syncAllAccountsChats(Context context) {
        Account[] accounts = AccountManager.get(context).getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE);
        for (final Account account : accounts) {
            syncAccountChats(account);
        }
    }

    public static void passwordChanged(Context context, Account account) {
        Log.error("User credentials have changed");
        SyncAccountHelper.disableSync(context, account, true, SynchronizationFailure.INCORRECT_CREDENTIALS);
    }
}
