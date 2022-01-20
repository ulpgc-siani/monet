package org.monet.space.mobile.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.Inject;

import roboguice.RoboGuice;

public class Preferences {

    private static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    private static final String CGM_TOKEN = "cgmtoken";

    private static final String KEY_DAYS_KEEP_FINISHED = "pref_key_days_keep_finished";
    private static final String KEY_SYNC_INTERVAL = "pref_key_sync_interval";
    private static final String KEY_NOTIFICATIONS = "pref_key_notifications";
    private static final String KEY_WIFI_ONLY = "pref_key_wifi_only";
    private static final String KEY_ROAMING_SYNC = "pref_key_roaming_sync";
    private static final String KEY_QRCODE = "pref_key_qrcode";

    @Inject
    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        RoboGuice.getInjector(context).injectMembers(this);
    }

    public int getDaysKeepFinished() {
        String value = this.sharedPreferences.getString(KEY_DAYS_KEEP_FINISHED, "1");
        return Integer.valueOf(value);
    }

    public long getSyncInterval() {
        String value = this.sharedPreferences.getString(KEY_SYNC_INTERVAL, "1800");
        return Integer.valueOf(value);
    }

    public boolean isNotificationsEnabled() {
        return this.sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true);
    }

    public boolean isQrCodeEnabled() {
        return this.sharedPreferences.getBoolean(KEY_QRCODE, false);
    }

    public boolean syncOnWiFiOnly() {
        return this.sharedPreferences.getBoolean(KEY_WIFI_ONLY, false);
    }

    public boolean syncOnRoaming() {
        return !this.sharedPreferences.getBoolean(KEY_ROAMING_SYNC, true);
    }

    public void setSentTokenToServer(boolean value) {
        this.sharedPreferences.edit().putBoolean(Preferences.SENT_TOKEN_TO_SERVER, false).apply();
    }

    public String cgmToken() {
        return this.sharedPreferences.getString(CGM_TOKEN, "");
    }

    public void setCgmToken(String value) {
        this.sharedPreferences.edit().putString(Preferences.CGM_TOKEN, value).apply();
    }
}
