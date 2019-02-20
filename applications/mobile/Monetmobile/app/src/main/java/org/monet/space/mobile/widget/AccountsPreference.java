package org.monet.space.mobile.widget;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.monet.space.mobile.activity.AccountActivity;
import org.monet.space.mobile.events.AccountListChangedEvent;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.helpers.RouterHelper;
import org.monet.space.mobile.mvp.BusProvider;

import org.monet.space.mobile.R;

public class AccountsPreference extends Preference implements OnClickListener {

    public static final int ADD_ACCOUNT_REQUEST = 0;
    public static final int EDIT_ACCOUNT_REQUEST = 1;

    private AccountManager mAccountManager = null;
    private LayoutInflater mLayoutInflater = null;
    private LinearLayout accountsLayout = null;

    public AccountsPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AccountsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AccountsPreference(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        BusProvider.get().register(this);
    }

    @Override
    protected void onPrepareForRemoval() {
        super.onPrepareForRemoval();

        BusProvider.get().unregister(this);
    }

    private void init() {
        this.mAccountManager = AccountManager.get(this.getContext());
        this.mLayoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        accountsLayout = (LinearLayout) view.findViewById(R.id.account_list);
        loadAccountList();
        return view;
    }

    @Subscribe
    public void onAccountListChanged(AccountListChangedEvent event) {
        this.loadAccountList();
    }

    private void loadAccountList() {
        this.accountsLayout.removeAllViews();
        Account[] accounts = this.mAccountManager.getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE);
        for (Account account : accounts) {
            String[] accountName = account.name.split(FederationAccountAuthenticator.ACCOUNT_NAME_SPLITTER_REGEX);
            String username = accountName[0];
            String businessUnitUrl = URLUtil.guessUrl(accountName[1]);

            View itemView = this.mLayoutInflater.inflate(R.layout.preference_accounts_item, this.accountsLayout, false);
            TextView accountNameTxt = (TextView) itemView.findViewById(R.id.account_name);
            TextView businessUnitTxt = (TextView) itemView.findViewById(R.id.business_unit);

            accountNameTxt.setText(username);
            businessUnitTxt.setText(Html.fromHtml(businessUnitUrl));

            itemView.setTag(account.name);
            itemView.setOnClickListener(this);

            this.accountsLayout.addView(itemView);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.business_unit_item) return;
        Intent intent = new Intent(getContext(), AccountActivity.class)
                .putExtra(RouterHelper.ACCOUNT_NAME, (String) view.getTag());
        ((Activity) getContext()).startActivityForResult(intent, EDIT_ACCOUNT_REQUEST);
    }

}
