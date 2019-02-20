package org.monet.space.mobile.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import org.monet.mobile.model.Task;
import org.monet.space.mobile.activity.TaskActivity;
import org.monet.space.mobile.model.TaskDetails;

import java.util.List;

import org.monet.space.mobile.R;

public class NotificationHelper {

    private static final int NOTIFICATION_TYPE_TASK = 0;
    private static final int NOTIFICATION_TYPE_CHAT = 1;
    private static final int NOTIFICATION_TYPE_UNTRUSTED_SERVER = 2;
    private static final int NOTIFICATION_TYPE_SYNC_DISABLED = 3;

    private static boolean untrustedNotified = false;

    public static void notifyNewTasks(Context context, long sourceId, String source, List<Task> newTasks, List<Long> tasksIds) {
        String title = context.getResources().getQuantityString(R.plurals.notification_title, newTasks.size(), newTasks.size());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_notify_task)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_stat_notify_task_big)).getBitmap())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(source);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setSummaryText(source);

        for (Task task : newTasks) {
            String taskLine = String.format(context.getString(R.string.notification_task_details), task.Label, task.Description);
            inboxStyle.addLine(Html.fromHtml(taskLine));
        }

        builder.setStyle(inboxStyle);

        Intent resultIntent = new Intent(context, TaskActivity.class);
        if (newTasks.size() == 1) {
            resultIntent.putExtra(RouterHelper.TASK_ID, tasksIds.get(0));
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(TaskActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_TYPE_TASK, builder.build());
    }


    public static void notifyNewChats(Context context, TaskDetails task) {
        String ticker = task.chatItems.get(task.chatItems.size() - 1).Message;
        String contentTitle = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            contentTitle = task.label;
        else
            contentTitle = ticker;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.stat_notify_chat)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.stat_notify_chat_big)).getBitmap())
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setTicker(ticker)
                .setContentInfo(String.valueOf(task.chatItems.size()))
                .setContentText(task.sourceLabel);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setSummaryText(task.sourceLabel);

        for (org.monet.space.mobile.model.ChatItem item : task.chatItems)
            inboxStyle.addLine(item.Message);

        builder.setStyle(inboxStyle);

        Intent resultIntent = new Intent(context, TaskActivity.class);
        resultIntent.putExtra(RouterHelper.TASK_ID, task.id);
        resultIntent.putExtra(RouterHelper.SHOW_CHAT_PAGE, true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(TaskActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(String.valueOf(task.id), NOTIFICATION_TYPE_CHAT, builder.build());
    }

    public static void notifyUntrustedServer(Context context, String serverUrl) {

        if (untrustedNotified) return;
        untrustedNotified = true;

        String title = context.getString(R.string.untrusted_server);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.untrusted_server)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.untrusted_server_big)).getBitmap())
                .setAutoCancel(true)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(serverUrl);

        createVoidIntent(context, builder);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_TYPE_UNTRUSTED_SERVER, builder.build());
    }

    public static void cancelTaskNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_TYPE_TASK);
    }

    public static void cancelChatNotifications(Context context, long taskId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(String.valueOf(taskId), NOTIFICATION_TYPE_CHAT);
    }

    public static void notifySyncDisabled(Context context, String name, String cause) {
        String title = context.getString(R.string.sync_disabled);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.sync_disabled)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.sync_disabled_big)).getBitmap())
                .setAutoCancel(true)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(cause);

        createVoidIntent(context, builder);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(name, NOTIFICATION_TYPE_SYNC_DISABLED, builder.build());
    }

    public static void cancelSyncDisabledNotification(Context context, String name) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(name, NOTIFICATION_TYPE_SYNC_DISABLED);
    }

    private static void createVoidIntent(Context context, NotificationCompat.Builder builder) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pend);
    }

}
