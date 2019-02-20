package org.monet.space.mobile.federation;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import org.monet.mobile.service.federation.Response;
import org.monet.space.mobile.R;
import org.monet.space.mobile.activity.FederationAccountAuthenticatorActivity;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.net.ProxyLayer;
import org.monet.space.mobile.presenter.FederationAccountAuthenticatorPresenter;

import java.util.HashMap;
import java.util.Map;

public class FederationAccountAuthenticator extends AbstractAccountAuthenticator {

    public static final String ACCOUNT_TYPE = "org.monet.mobile.accounts.android";
    public static final String AUTHTOKEN_TYPE = "org.monet.mobile.accounts.android";
    public static final String FEDERATION = "FEDERATION";
    public static final String FEDERATION_URL = "FEDERATION_URL";
    public static final String BUSINESS_UNIT = "BUSINESS_UNIT";
    public static final String BUSINESS_UNIT_LABEL = "BUSINESS_UNIT_LABEL";
    public static final String BUSINESS_UNIT_TITLE = "BUSINESS_UNIT_TITLE";
    public static final String BUSINESS_UNIT_SUBTITLE = "BUSINESS_UNIT_SUBTITLE";
    public static final String ACCOUNT_NAME_SPLITTER_REGEX = "\\$#\\$";
    public static final String ACCOUNT_NAME_SPLITTER = "$#$";

    private Context context;

    public FederationAccountAuthenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        final Intent intent = new Intent(this.context, FederationAccountAuthenticatorActivity.class);
        intent.putExtra(FederationAccountAuthenticatorPresenter.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        if (!authTokenType.equals(AUTHTOKEN_TYPE)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }

        final AccountManager am = AccountManager.get(this.context);
        final String password = am.getPassword(account);

        String[] components = account.name.split(ACCOUNT_NAME_SPLITTER_REGEX);

        final String federationUrl = am.getUserData(account, FEDERATION_URL);
        final String businessUnit = am.getUserData(account, BUSINESS_UNIT);
        final String secret = this.context.getString(R.string.secret);
        final String accessToken = onlineConfirmPassword(federationUrl, businessUnit, secret, components[0], password);

        if (accessToken == null) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "unable to get AuthToken");
            return result;
        }

        final Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
        result.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
        return result;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return AUTHTOKEN_TYPE.equals(authTokenType) ? context.getString(R.string.federation_authenticator_name) : null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
        return result;
    }

    private String onlineConfirmPassword(String federationUrl, String businessUnit, String secret, String username, String password) {
        OAuthAPI oAuthAPI = new OAuthAPI(federationUrl);
        OAuthProvider provider = new CommonsHttpOAuthProvider(oAuthAPI.getRequestTokenEndpoint(), oAuthAPI.getAccessTokenEndpoint(), oAuthAPI.getAuthorizationUrl()); //, HttpClientHelper.setupHttpClient(this.context)
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(businessUnit, secret);

        String requestToken;
        try {
            provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
            requestToken = consumer.getToken();
        } catch (Exception e) {
            Log.error("Error getting request token.");
            return null;
        }

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("oauth_token", requestToken);
        parameters.put("username", username);
        parameters.put("password", password);
        parameters.put("space", businessUnit);

        Response loginResponse;
        try {
            loginResponse = ProxyLayer.login(this.context, oAuthAPI.getAuthorizationUrl(), parameters);
        } catch (Exception e) {
            Log.error("Error doing login.", e);
            return null;
        }

        if (!(loginResponse.getResponseCode() == Response.RESPONSE_LOGIN_SUCCESSFULLY)) {
            Log.error("Error login response unsucessfully.");
            return null;
        }

        try {
            provider.retrieveAccessToken(consumer, loginResponse.getVerifier());
            return consumer.getToken();
        } catch (Exception e) {
            Log.error("Error getting access token.");
            return null;
        }
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(this.context, FederationAccountAuthenticatorActivity.class)
                .putExtra(FederationAccountAuthenticatorPresenter.PARAM_USERNAME, account.name)
                .putExtra(FederationAccountAuthenticatorPresenter.PARAM_AUTHTOKEN_TYPE, authTokenType)
                .putExtra(FederationAccountAuthenticatorPresenter.PARAM_CONFIRM_CREDENTIALS, false);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

}
