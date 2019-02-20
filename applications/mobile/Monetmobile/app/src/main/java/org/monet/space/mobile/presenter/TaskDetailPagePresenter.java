package org.monet.space.mobile.presenter;

import android.accounts.Account;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;
import com.google.inject.Inject;

import org.monet.space.mobile.R;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.events.TaskAbandonedEvent;
import org.monet.space.mobile.events.TaskLoadedEvent;
import org.monet.space.mobile.events.TaskLoadingEvent;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.helpers.LocalStorage;
import org.monet.space.mobile.helpers.SyncAccountHelper;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.model.TaskDetails.Attachment;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.mvp.Presenter;
import org.monet.space.mobile.view.TaskDetailPageView;

import roboguice.util.SafeAsyncTask;

import java.util.Date;
import java.util.List;

public class TaskDetailPagePresenter extends Presenter<TaskDetailPageView, Void> {

    @Inject
    Repository repository;

    public TaskDetails taskDetails;

    public void initialize(TaskDetails taskDetails) {
        this.taskDetails = taskDetails;
    }

    public void resume() {
    }

    public void openAttachment(Attachment attachment) {
        PackageManager packageManager = this.context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri pathUri = (new Uri.Builder()).scheme("file").path(attachment.path).build();
        intent.setDataAndType(pathUri, attachment.contentType);

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0)
            this.context.startActivity(intent);
        else
            Toast.makeText(this.context, this.context.getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
    }

    public void doContinue() {
        BusProvider.get().post(new TaskLoadingEvent());
        Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (taskDetails.startDate == null)
                    repository.updateTaskStartDate(taskDetails.id, new Date());
                BusProvider.get().post(new TaskLoadedEvent());
                routerHelper.goToTaskStep(taskDetails.id, false);
            }
        });
    }

    public void doDisplay() {
        BusProvider.get().post(new TaskLoadingEvent());
        Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                BusProvider.get().post(new TaskLoadedEvent());
                routerHelper.goToTaskDisplay(taskDetails.id);
            }
        });
    }

    public void doAbandon() {
        new SafeAsyncTask<Account>() {
            @Override
            public Account call() throws Exception {
                TaskDetails task = repository.getTaskDetails(taskDetails.id);
                if (task.serverId == null) {
                    repository.deleteTask(taskDetails.id);
                    LocalStorage.deleteTask(context, taskDetails.id);
                } else {
                    repository.updateTaskToUnassigned(taskDetails.id);
                }

                return new Account(repository.getSource(taskDetails.sourceId).accountName, FederationAccountAuthenticator.ACCOUNT_TYPE);
            }

            @Override
            protected void onSuccess(Account account) throws Exception {
                super.onSuccess(account);

                SyncAccountHelper.syncAccountTasks(account);
                Toast.makeText(context, R.string.unassign_dialog_result_message, Toast.LENGTH_LONG).show();
            }
        }.execute();

        BusProvider.get().post(new TaskAbandonedEvent());
    }

    public void doAssign() {
        new SafeAsyncTask<Void>() {
            @Override
            public Void call() throws Exception {
                repository.updateTaskToAssigned(taskDetails.id);

                return null;
            }

            @Override
            protected void onSuccess(Void t) throws Exception {
                super.onSuccess(t);

                SyncAccountHelper.syncAllAccountsTasks(context);

                Toast.makeText(context, R.string.assign_dialog_result_message, Toast.LENGTH_LONG).show();
            }
        }.execute();

        BusProvider.get().post(new TaskAbandonedEvent());
    }

    public void refresh(TaskDetails taskDetails) {
        this.taskDetails = taskDetails;
    }

}
