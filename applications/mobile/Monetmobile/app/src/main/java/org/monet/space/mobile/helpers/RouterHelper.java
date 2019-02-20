package org.monet.space.mobile.helpers;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.inject.Inject;
import org.monet.space.mobile.activity.*;
import org.monet.space.mobile.mvp.View;
import org.monet.space.mobile.presenter.FederationAccountAuthenticatorPresenter;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class RouterHelper {

    public static final String ID = "id";
    public static final String READONLY = "readonly";
    public static final String TASK = "task";
    public static final String RESET = "reset";
    public static final String ACCOUNT = "account";
    public static final String TASK_ID = "taskid";
    public static final String SOURCE_ID = "sourceid";
    public static final String ACCOUNT_NAME = "accountname";
    public static final String FLAG_FIRST_ACCOUNT = "flagfirstaccount";
    public static final String SHOW_CHAT_PAGE = "showchatpage";

    @Inject
    Context context;

    public void goToTaskList() {
        context.startActivity(new Intent(context, TaskActivity.class));
    }

    public void goUpToTaskList() {
        Intent intent = new Intent(context, TaskActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public void goToTaskMap() {
        context.startActivity(new Intent(context, TaskMapActivity.class));
    }

    public void goToTaskDetails(long id, boolean showChatByDefault) {
        Intent intent = new Intent(context, TaskDetailsContainerActivity.class)
                .putExtra(ID, id)
                .putExtra(SHOW_CHAT_PAGE, showChatByDefault);
        context.startActivity(intent);
    }

    public void goToTaskDisplay(long id) {
        Intent intent = new Intent(context, TaskStepActivity.class)
                .putExtra(ID, id)
                .putExtra(READONLY, true)
                .putExtra(RESET, true);
        context.startActivity(intent);
    }

    public void goToTaskStep(long id, boolean readonly) {
        Intent intent = new Intent(context, TaskStepActivity.class)
                .putExtra(ID, id)
                .putExtra(READONLY, readonly);
        context.startActivity(intent);
    }

    public void goToSourceEdit(View view, int requestCode, Account account) {
        final Intent intent = new Intent(context, FederationAccountAuthenticatorActivity.class)
                .putExtra(FederationAccountAuthenticatorPresenter.PARAM_USERNAME, account.name)
                .putExtra(FederationAccountAuthenticatorPresenter.PARAM_AUTHTOKEN_TYPE, account.type);
        ((Activity) view).startActivityForResult(intent, requestCode);
    }

    public void goToSettings() {
        context.startActivity(new Intent(context, SettingsContainerActivity.class));
    }

    public static void goToNewAccount(Context context) {
        Intent intent = new Intent(context, FederationAccountAuthenticatorActivity.class).putExtra(FLAG_FIRST_ACCOUNT, true);
        context.startActivity(intent);
    }

    public void goToNewTask() {
        context.startActivity(new Intent(context, NewTaskContainerActivity.class));
    }

    public void goToPickTerm(View view, int requestCode, Bundle args) {
        if (view instanceof Activity)
            ((Activity) view).startActivityForResult(createIntentWithArgs(PickTermContainerActivity.class, args), requestCode);
        else if (view instanceof Fragment)
            ((Fragment) view).startActivityForResult(createIntentWithArgs(PickTermContainerActivity.class, args), requestCode);

    }

    public void goToPickCheck(View view, int requestCode, Bundle args) {
        if (view instanceof Activity)
            ((Activity) view).startActivityForResult(createIntentWithArgs(PickCheckContainerActivity.class, args), requestCode);
        else if (view instanceof Fragment)
            ((Fragment) view).startActivityForResult(createIntentWithArgs(PickCheckContainerActivity.class, args), requestCode);
    }

    private Intent createIntentWithArgs(Class targetActivity, Bundle args) {
        return new Intent(context, targetActivity).putExtras(args);
    }
}
