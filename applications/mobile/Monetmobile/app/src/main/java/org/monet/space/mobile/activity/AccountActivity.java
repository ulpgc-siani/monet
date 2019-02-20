package org.monet.space.mobile.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import org.monet.space.mobile.R;
import org.monet.space.mobile.helpers.FontUtils;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.mvp.activity.Activity;
import org.monet.space.mobile.presenter.AccountPresenter;
import org.monet.space.mobile.view.AccountView;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.io.File;

@ContentView(R.layout.activity_source)
public class AccountActivity extends Activity<AccountView, AccountPresenter, Void> implements AccountView, OnCheckedChangeListener {

    @InjectView(R.id.text_business_unit_url)
    private TextView businessUnitUrl;
    @InjectView(R.id.checkBoxSyncEnabled)
    private CheckBox syncEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareActionBar();
        FontUtils.setRobotoFont(this, this.findViewById(android.R.id.content));
        syncEnabled.setOnCheckedChangeListener(this);
        presenter.create();
    }

    private void prepareActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        File appIcon = LocalStorage.getAppIconFile(this);
        if (appIcon.exists())
            actionBar.setIcon(Drawable.createFromPath(appIcon.getAbsolutePath()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:
                presenter.edit();
                break;
            case R.id.menu_remove:
                presenter.remove();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.activityResult(requestCode, resultCode);
    }

    @Override
    public void setUsername(String value) {
        setTitle(value);
    }

    @Override
    public void setBusinessUnitUrl(String value) {
        businessUnitUrl.setText(value);
    }

    @Override
    public void setSyncEnabled(boolean value) {
        syncEnabled.setChecked(value);
    }

    @Override
    public void backToLoginActivity() {
        Intent intent = new Intent(this, FederationAccountAuthenticatorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            intent.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.checkBoxSyncEnabled)
            presenter.setSyncEnabled(isChecked);
    }

}
