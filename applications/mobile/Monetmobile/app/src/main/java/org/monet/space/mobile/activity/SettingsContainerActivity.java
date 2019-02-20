package org.monet.space.mobile.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.monet.space.mobile.R;
import org.monet.space.mobile.events.AccountListChangedEvent;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.presenter.SettingsContainerPresenter;
import org.monet.space.mobile.view.PreferenceContainerView;
import org.monet.space.mobile.widget.AccountsPreference;

import java.io.File;

public class SettingsContainerActivity extends Activity<PreferenceContainerView, SettingsContainerPresenter, Void> implements PreferenceContainerView {

    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preference_container);

        this.prepareActionBar();

        this.presenter.initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.presenter.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    private void prepareActionBar() {
        this.actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        File appIcon = LocalStorage.getAppIconFile(this);
        if (appIcon.exists()) {
            Drawable icon = Drawable.createFromPath(appIcon.getAbsolutePath());
            actionBar.setIcon(icon);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.presenter.up();
                break;

            case R.id.menu_add_account:
                Intent intent = new Intent(this, FederationAccountAuthenticatorActivity.class);
                this.startActivityForResult(intent, AccountsPreference.ADD_ACCOUNT_REQUEST);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AccountsPreference.ADD_ACCOUNT_REQUEST:
                case AccountsPreference.EDIT_ACCOUNT_REQUEST:
                    BusProvider.get().post(new AccountListChangedEvent());
                    break;

                default:
                    break;
            }
        }
    }

}
