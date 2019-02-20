package org.monet.space.mobile.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.google.inject.Inject;

import org.monet.space.mobile.R;
import org.monet.space.mobile.helpers.FontUtils;
import org.monet.space.mobile.mvp.activity.AccountAuthenticatorActivity;
import org.monet.space.mobile.presenter.FederationAccountAuthenticatorPresenter;
import org.monet.space.mobile.view.FederationAccountAuthenticatorView;
import org.monet.space.mobile.view.validator.FormValidator;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.federation_account_authenticator_view)
public class FederationAccountAuthenticatorActivity extends AccountAuthenticatorActivity<FederationAccountAuthenticatorView, FederationAccountAuthenticatorPresenter, Void> implements FederationAccountAuthenticatorView, OnClickListener {

    @InjectView(R.id.login_form)
    private View loginFormView;
    @InjectView(R.id.login_loading)
    private View loginLoadingView;

    @InjectView(R.id.txt_serverurl)
    private EditText txtServerUrl;
    @InjectView(R.id.txt_username)
    private EditText txtUsername;
    @InjectView(R.id.txt_password)
    private EditText txtPassword;

    @Inject
    private FormValidator formValidator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        formValidator.verifyValidURL(txtServerUrl).atErrorShow(R.string.err_bad_serverurl);
        formValidator.verifyNotEmpty(txtUsername).atErrorShow(R.string.err_empty_username);
        formValidator.verifyNotEmpty(txtPassword).atErrorShow(R.string.err_empty_password);

        findViewById(R.id.login_button).setOnClickListener(this);

        FontUtils.setRobotoFont(this, findViewById(android.R.id.content));

        presenter.initialize();

        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE)
                    onLoginClick();
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_button)
            onLoginClick();
    }

    private void onLoginClick() {
        if (formValidator.isValid())
            presenter.login(stringValue(txtServerUrl), stringValue(txtUsername), stringValue(txtPassword));
    }

    public void setUsername(String username) {
        txtUsername.setText(username);
        txtUsername.setEnabled(false);
    }

    @Override
    public void setServerUrl(String serverUrl) {
        txtServerUrl.setText(serverUrl);
        txtServerUrl.setEnabled(false);
    }

    @Override
    public void showLoading() {
        loginLoadingView.setVisibility(View.VISIBLE);
        loginFormView.setVisibility(View.GONE);

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView == null)
            focusedView = txtPassword;
        inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void showForm() {
        loginLoadingView.setVisibility(View.GONE);
        loginFormView.setVisibility(View.VISIBLE);
    }

    public String stringValue(EditText editText) {
        return editText.getText().toString();
    }

}
