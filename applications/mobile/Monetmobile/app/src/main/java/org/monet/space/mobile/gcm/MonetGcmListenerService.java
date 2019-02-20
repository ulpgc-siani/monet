package org.monet.space.mobile.gcm;


import android.accounts.Account;
import android.accounts.AccountManager;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import org.monet.mobile.service.PushOperations;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.helpers.SyncAccountHelper;

public class MonetGcmListenerService extends GcmListenerService {


    @Override
    public void onMessageReceived(String from, Bundle data) {
        String opName = data.getString("operation");
        String messageBusinessUid = data.getString("business_unit_uid");

        if (opName == null) return;
        PushOperations pushOp = PushOperations.valueOf(opName);

        String accountBusinessUid;

        AccountManager accountManager = AccountManager.get(this);
        for (Account account : accountManager.getAccountsByType(FederationAccountAuthenticator.ACCOUNT_TYPE)) {
            accountBusinessUid = accountManager.getUserData(account, FederationAccountAuthenticator.FEDERATION_URL) + accountManager.getUserData(account, FederationAccountAuthenticator.BUSINESS_UNIT);
            if (accountBusinessUid.equals(messageBusinessUid) || (pushOp == PushOperations.PASSWORD_RESET))
                performOperation(pushOp, account);
        }
    }

    private void performOperation(PushOperations operation, Account account) {
        switch (operation) {
            case DEFINITION_UPDATE:
                SyncAccountHelper.syncAccountDefinitions(account);
                break;

            case GLOSSARY_UPDATE:
                SyncAccountHelper.syncAccountGlossaries(account);
                break;

            case CHAT_UPDATE:
                SyncAccountHelper.syncAccountChats(account);
                break;

            case TASK_UPDATE:
                SyncAccountHelper.syncAccountTasks(account);
                break;

            case PASSWORD_RESET:
                SyncAccountHelper.passwordChanged(getApplicationContext(), account);
                break;
        }
    }

}
