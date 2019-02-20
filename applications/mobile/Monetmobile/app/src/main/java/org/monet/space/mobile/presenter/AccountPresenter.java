package org.monet.space.mobile.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.TypedValue;
import android.webkit.URLUtil;
import android.widget.Toast;
import com.google.inject.Inject;

import org.monet.space.mobile.R;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.helpers.Log;
import org.monet.space.mobile.helpers.RouterHelper;
import org.monet.space.mobile.helpers.SyncAccountHelper;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.view.AccountView;


public class AccountPresenter extends Presenter<AccountView, Void> implements AccountManagerCallback<Boolean> {

    private static final int EDIT_ACCOUNT_REQUEST = 0;

    @Inject
    private Repository repository;

    private Account account = null;
    private AccountManager accountManager = null;

    public void create() {
        String accountName = this.getArgs().getString(RouterHelper.ACCOUNT_NAME);
        this.accountManager = AccountManager.get(this.context);
        this.account = new Account(accountName, FederationAccountAuthenticator.ACCOUNT_TYPE);

        refresh();
    }

    public void activityResult(int requestCode, int resultCode) {
        if (requestCode == EDIT_ACCOUNT_REQUEST && resultCode == Activity.RESULT_OK)
            refresh();
    }

    private void refresh() {
        String[] accountNameComponents = this.account.name.split(FederationAccountAuthenticator.ACCOUNT_NAME_SPLITTER_REGEX);

        String username = accountNameComponents[0];
        String businessUnitUrl = URLUtil.guessUrl(accountNameComponents[1]);
        boolean syncEnabled = SyncAccountHelper.isSyncEnabled(this.account);

        this.view.setUsername(username);
        this.view.setBusinessUnitUrl(businessUnitUrl);
        this.view.setSyncEnabled(syncEnabled);
    }

    public void edit() {
        this.routerHelper.goToSourceEdit(this.view, EDIT_ACCOUNT_REQUEST, this.account);
    }

    public void remove() {
        new AlertDialog.Builder(this.context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.remove_account_title)
                .setMessage(R.string.remove_account_message)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        repository.deleteSource(account.name);
                        accountManager.removeAccount(account, AccountPresenter.this, null);
                    }
                })
                .show();
    }

    private boolean noMoreAccounts() {
        return accountManager.getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE).length == 0;
    }

    @Override
    public void run(AccountManagerFuture<Boolean> future) {
        try {
            if (future.isDone()) {
                future.getResult();
                Toast.makeText(this.context, this.context.getString(R.string.account_removed), Toast.LENGTH_SHORT).show();
                view.setResult(Activity.RESULT_OK, new Intent());
                if (noMoreAccounts()) view.backToLoginActivity();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this.context, this.context.getString(R.string.account_cant_be_removed), Toast.LENGTH_SHORT).show();
            Log.error(e);
        }
    }

    public void setSyncEnabled(boolean value) {
        if (value)
            SyncAccountHelper.enableSync(this.context, this.account);
        else
            SyncAccountHelper.disableSync(this.context, this.account, false);
    }

}
