package org.monet.space.mobile.syncservice;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Xml;
import com.squareup.otto.Subscribe;
import org.apache.http.ParseException;
import org.monet.mobile.model.ChatItem;
import org.monet.mobile.model.Task;
import org.monet.mobile.model.TaskDefinition;
import org.monet.mobile.model.TaskMetadata;
import org.monet.mobile.service.results.*;
import org.monet.space.mobile.db.Repository;
import org.monet.space.mobile.events.*;
import org.monet.space.mobile.federation.FederationAccountAuthenticator;
import org.monet.space.mobile.helpers.*;
import org.monet.space.mobile.model.Preferences;
import org.monet.space.mobile.model.SourceDetails;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.model.TaskDetails.Attachment;
import org.monet.space.mobile.mvp.BusProvider;
import org.monet.space.mobile.net.ProxyFactory;
import org.monet.space.mobile.net.ProxyLayer;
import org.monet.space.mobile.provider.MonetDataProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import roboguice.RoboGuice;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String SYNC_MODE = "SYNC_MODE";

    public static final int SYNC_MODE_ALL = 0;
    public static final int SYNC_MODE_TASKS = 1;
    public static final int SYNC_MODE_CHATS = 2;
    public static final int SYNC_MODE_DEFINITIONS = 3;
    public static final int SYNC_MODE_GLOSSARIES = 4;

    @Inject
    private Repository repository;

    private final ConnectivityManager connectivityManager;
    private final Preferences preferences;

    private Account account;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        RoboGuice.getInjector(getContext()).injectMembersWithoutViews(this);

        this.preferences = new Preferences(context);
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private boolean checkConnection() {
        NetworkInfo networkInfo = this.connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) return false;
        if (!networkInfo.isConnected()) return false;
        if (!networkInfo.isAvailable()) return false;

        if ((networkInfo.getType() != ConnectivityManager.TYPE_WIFI) && this.preferences.syncOnWiFiOnly()) return false;

        if ((networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) && networkInfo.isRoaming() && !this.preferences.syncOnRoaming())
            return false;

        return true;
    }

    private boolean syncEnabled() {
        return ContentResolver.getSyncAutomatically(account, MonetDataProvider.AUTHORITIES);
    }

    @Subscribe
    public void onUntrustedServer(UntrustedServerEvent event) {
        String accountName = this.account.name;
        int indexSeparator = accountName.indexOf(FederationAccountAuthenticator.ACCOUNT_NAME_SPLITTER);
        String serverUrl = accountName.substring(indexSeparator + FederationAccountAuthenticator.ACCOUNT_NAME_SPLITTER.length());

        NotificationHelper.notifyUntrustedServer(this.getContext(), serverUrl);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        this.account = account;

        int syncMode = extras.getInt(SYNC_MODE, SYNC_MODE_ALL);

        if (!checkConnection()) return;
        if (!syncEnabled()) return;

        SourceDetails source = SyncAccountHelper.getSourceDetails(account, repository, this.getContext());
        if (source == null) return;

        BusProvider.get().register(this);
        BusProvider.get().post(new SyncStartedEvent());
        try {
            Log.debug("Start of sync for account: " + account.name);

            ProxyLayer proxyLayer = ProxyFactory.getInstance().getProxy(getContext(), account.name);

            switch (syncMode) {
                case SYNC_MODE_DEFINITIONS:
                    this.syncDefinitions(syncResult, proxyLayer, source);
                    break;

                case SYNC_MODE_GLOSSARIES:
                    this.syncGlossaries(syncResult, proxyLayer, source);
                    break;

                case SYNC_MODE_TASKS:
                    this.syncTasks(syncResult, proxyLayer, source);
                    break;

                case SYNC_MODE_CHATS:
                    this.syncChats(proxyLayer, source);
                    break;

                case SYNC_MODE_ALL:
                    this.syncDefinitions(syncResult, proxyLayer, source);
                    this.syncGlossaries(syncResult, proxyLayer, source);
                    this.syncTasks(syncResult, proxyLayer, source);
                    this.syncChats(proxyLayer, source);
                    break;
            }

            syncResult.delayUntil = 0;

        } catch (final OperationCanceledException e) {
            Log.error(e);
        } catch (final ParseException e) {
            Log.error(e);
            syncResult.stats.numParseExceptions++;
        } catch (Exception e) {
            Log.error(e);
            syncResult.stats.numIoExceptions++;

            SyncAccountHelper.disableSync(this.getContext(), account, true, SynchronizationFailure.DEFAULT);

        } finally {
            BusProvider.get().post(new SyncEndedEvent());
            BusProvider.get().unregister(this);

            Log.debug("End of sync for account: " + account.name);
        }
    }

    private void syncDefinitions(SyncResult syncResult, ProxyLayer proxyLayer, SourceDetails source) throws Exception {
        long lastSyncMarker = SyncAccountHelper.getServerSyncMarker(this.getContext(), account, SyncAccountHelper.DEFINITION_SYNC_MARKER_KEY);

        Long syncMark = this.downloadDefinitions(syncResult, source, proxyLayer, lastSyncMarker);
        if (syncMark != null)
            SyncAccountHelper.setServerSyncMarker(this.getContext(), account, SyncAccountHelper.DEFINITION_SYNC_MARKER_KEY, syncMark);
    }

    private void syncGlossaries(SyncResult syncResult, ProxyLayer proxyLayer, SourceDetails source) throws Exception {
        long lastSyncMarker = SyncAccountHelper.getServerSyncMarker(this.getContext(), account, SyncAccountHelper.GLOSSARY_SYNC_MARKER_KEY);

        Long syncMark = this.downloadGlossaries(syncResult, source, proxyLayer, lastSyncMarker);
        if (syncMark != null)
            SyncAccountHelper.setServerSyncMarker(this.getContext(), account, SyncAccountHelper.GLOSSARY_SYNC_MARKER_KEY, syncMark);
    }

    private void syncTasks(SyncResult syncResult, ProxyLayer proxyLayer, SourceDetails source) throws Exception {
        long lastSyncMarker = SyncAccountHelper.getServerSyncMarker(this.getContext(), account, SyncAccountHelper.TASKS_SYNC_MARKER_KEY);

        removeExpiredFinishedTasks(source);

        uploadFinishedTasks(syncResult, source, proxyLayer);

        uploadAbandonedTasks(syncResult, source, proxyLayer);
        removeDeletedInServerUnassignedTasks(syncResult, source, proxyLayer);
        downloadNewAvailableTasks(syncResult, source, proxyLayer, lastSyncMarker);

        uploadAssignedTasks(syncResult, source, proxyLayer);
        removeDeletedInServerAssignedTasks(syncResult, source, proxyLayer);
        Long syncMark = this.downloadNewAssignedTasks(syncResult, source, proxyLayer, lastSyncMarker);

        if (syncMark != null)
            SyncAccountHelper.setServerSyncMarker(this.getContext(), account, SyncAccountHelper.TASKS_SYNC_MARKER_KEY, syncMark);

        BusProvider.get().post(new NewTasksEvent());
    }

    private boolean syncChats(ProxyLayer proxyLayer, SourceDetails source) throws Exception {
        boolean hasChanges = false;

        long lastChatSyncMarker = SyncAccountHelper.getServerSyncMarker(this.getContext(), account, SyncAccountHelper.CHAT_SYNC_MARKER_KEY);

        List<org.monet.space.mobile.model.ChatItem> chatItemsNotSent = repository.getChatItemsNotSent(source.id);
        List<ChatItem> chatItemsToSend = new ArrayList<>();
        for (org.monet.space.mobile.model.ChatItem item : chatItemsNotSent)
            chatItemsToSend.add(new ChatItem(item.ServerId, item.Message, item.DateTime.getTime(), item.IsOut));

        try {
            SyncChatsResult newChatMessages = proxyLayer.syncChats(chatItemsToSend, lastChatSyncMarker);

            repository.markChatItemsAsSent(chatItemsNotSent);
            this.saveNewChatItems(newChatMessages, source);

            if (newChatMessages.Chats.size() > 0)
                hasChanges = true;

            SyncAccountHelper.setServerSyncMarker(this.getContext(), account, SyncAccountHelper.CHAT_SYNC_MARKER_KEY, newChatMessages.SyncMark);

            return hasChanges;

        } catch (Exception e) {
            Log.error("Error syncing chats.", e);
            throw e;
        }
    }

    private void saveNewChatItems(SyncChatsResult newChatMessages, SourceDetails source) {

        Map<Long, TaskDetails> tasksToNotify = new HashMap<Long, TaskDetails>();

        for (ChatItem item : newChatMessages.Chats) {
            long taskId = repository.getTaskId(source.id, item.ServerId);
            if (taskId < 0) continue;

            org.monet.space.mobile.model.ChatItem chatItem = repository.addChatItem(taskId, item.ServerId, source.id, item.Message, item.getDateTime(), item.IsOut, true);
            ChatMessageReceivedEvent event = new ChatMessageReceivedEvent(taskId, chatItem);
            BusProvider.get().post(event);
            if (!event.Captured) {
                TaskDetails task = tasksToNotify.get(taskId);
                if (task == null) {
                    task = repository.getTaskDetails(taskId);
                    tasksToNotify.put(taskId, task);
                }
                if (task.chatItems == null)
                    task.chatItems = new ArrayList<org.monet.space.mobile.model.ChatItem>();
                task.chatItems.add(chatItem);
            }
        }

        for (TaskDetails task : tasksToNotify.values()) {
            repository.updateNotReadChatCount(task.id, task.chatItems.size());
            NotificationHelper.notifyNewChats(this.getContext(), task);
        }

    }

    private void removeExpiredFinishedTasks(SourceDetails source) {
        int daysKeepFinished = this.preferences.getDaysKeepFinished();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -(daysKeepFinished - 1));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long[] taskToDelete = repository.getFinishedAndExpiredTasksIds(source.id, cal.getTime());

        for (long taskId : taskToDelete)
            deleteTaskFromLocal(taskId);
    }

    private void uploadFinishedTasks(SyncResult syncResult, SourceDetails source, ProxyLayer proxyLayer) throws Exception {
        for (Long id : repository.getFinishedTasksIds(source.id, false).values())
            uploadTask(syncResult, id, proxyLayer);
    }

    private void uploadTask(SyncResult syncResult, Long idTask, ProxyLayer proxyLayer) throws Exception {
        try {
            File metadataFile = LocalStorage.getTaskResultMetadataFile(this.getContext(), String.valueOf(idTask));
            if (!metadataFile.exists())
                this.createMetadataFile(idTask);
            PrepareUploadTaskResult prepareResult = proxyLayer.prepareUploadTask(metadataFile);
            repository.updateTaskServerId(idTask, prepareResult.ID);

            this.uploadTaskFiles(String.valueOf(idTask), prepareResult.ID, proxyLayer);

            File schemaFile = LocalStorage.getTaskResultSchemaFile(this.getContext(), String.valueOf(idTask));
            proxyLayer.uploadTaskSchema(prepareResult.ID, schemaFile);
            repository.updateTaskAsSynchronized(idTask);

            syncResult.stats.numEntries++;
        } catch (Exception e) {
            Log.error("Error uploading task.", e);
            throw e;
        }
    }

    private void createMetadataFile(long taskId) throws Exception {
        try {
            TaskDetails task = repository.getTaskDetails(taskId);
            TaskMetadata metadata = new TaskMetadata();
            metadata.setId(task.serverId);
            metadata.setCode(task.code);
            metadata.setStartDate(task.startDate);
            metadata.setEndDate(task.endDate);
            PersisterHelper.save(LocalStorage.getTaskResultMetadataFile(this.getContext(), String.valueOf(taskId)), metadata);
        } catch (Exception e) {
            Log.error("Error creating metadata file.", e);
            throw e;
        }
    }

    private void uploadTaskFiles(String idTask, String idTaskServer, ProxyLayer proxyLayer) throws Exception {

        try {
            File[] files = LocalStorage.getTaskResultDataFiles(this.getContext(), String.valueOf(idTask));
            for (File file : files) {
                if (this.repository.isConsolidatedFile(file.getName())) continue;

                proxyLayer.uploadTaskFile(idTaskServer, file);
                this.repository.consolidateFile(file.getName());
            }
        } catch (Exception e) {
            Log.error("Error uploading task files.", e);
            throw e;
        }
    }

    private void uploadAbandonedTasks(SyncResult syncResult, SourceDetails source, ProxyLayer proxyLayer) throws Exception {
        Map<String, Long> tasksIdsToUnassign = repository.getUnassignedTasksIds(source.id, false);

        for (Entry<String, Long> taskEntry : tasksIdsToUnassign.entrySet()) {
            try {
                proxyLayer.unassignTask(taskEntry.getKey());

                long idTask = taskEntry.getValue();
                repository.updateTaskAsSynchronized(idTask);
                this.deleteTaskFromLocal(idTask);

                syncResult.stats.numUpdates++;
            } catch (Exception e) {
                Log.error("Error uploading abandoned tasks.", e);
                throw e;
            }
        }
    }

    private Long removeDeletedInServerUnassignedTasks(SyncResult syncResult, SourceDetails source, ProxyLayer proxyLayer) throws Exception {
        Map<String, Long> unassignedTasksIds = repository.getUnassignedTasksIds(source.id, true);

        List<String> serverIds = new ArrayList<>();
        for (String id : unassignedTasksIds.keySet())
            serverIds.add(id);

        try {
            DownloadTasksToDeleteResult result = proxyLayer.loadUnassignedTasksToDelete(serverIds);

            for (String serverIdToDelete : result.TasksToDelete) {
                Long idTask = unassignedTasksIds.get(serverIdToDelete);
                this.deleteTaskFromLocal(idTask);

                syncResult.stats.numDeletes++;
            }
            return result.SyncMark;
        } catch (Exception e) {
            Log.error("Error removing deleted_in_server unassigned tasks.", e);
            throw e;
        }
    }

    private Long downloadNewAvailableTasks(SyncResult syncResult, SourceDetails source, ProxyLayer proxyLayer, long lastSyncMarker) throws Exception {
        DownloadTasksResult result;
        try {
            result = proxyLayer.loadNewAvailableTasks(lastSyncMarker);

            List<Task> tasksAdded = new ArrayList<>();
            List<Long> tasksIds = new ArrayList<>();

            for (Task task : result.Tasks) {
                if (repository.existsTask(source.id, task.ID)) continue;

                long taskId = repository.addTask(source.id, source.label, task.ID, task.Context, task.Code, task.Label, task.Description, task.PositionLat, task.PositionLon, task.getSuggestedStartDate(), task.getSuggestedEndDate(), task.Urgent, task.Comments, task.StepCount, TaskDetails.TASK_TRAY_UNASSIGNED, true);
                tasksAdded.add(task);
                tasksIds.add(taskId);
                syncResult.stats.numInserts++;
            }

            if (tasksAdded.size() > 0) {
                if (this.preferences.isNotificationsEnabled())
                    NotificationHelper.notifyNewTasks(this.getContext(), source.id, source.label, tasksAdded, tasksIds);
            }

            return result.SyncMark;

        } catch (Exception e) {
            Log.error("Error downloading new available tasks.", e);
            throw e;
        }
    }

    private void uploadAssignedTasks(SyncResult syncResult, SourceDetails source, ProxyLayer proxyLayer) throws Exception {
        Map<String, Long> tasksIdsToAssign = repository.getAssignedTasksIds(source.id, false);

        for (Entry<String, Long> taskEntry : tasksIdsToAssign.entrySet()) {
            try {
                proxyLayer.assignTask(taskEntry.getKey());

                long idTask = taskEntry.getValue();
                repository.updateTaskAsSynchronized(idTask);

                String idServer = repository.getTaskServerId(idTask);
                if (idServer != null)
                    this.saveTaskInLocal(idTask, idServer, proxyLayer);

                syncResult.stats.numUpdates++;
            } catch (Exception e) {
                Log.error("Error uploading assigned tasks", e);
                throw e;
            }
        }
    }

    private Long removeDeletedInServerAssignedTasks(SyncResult syncResult, SourceDetails source, ProxyLayer proxyLayer) throws Exception {
        Map<String, Long> assignedTasksIds = repository.getAssignedTasksIds(source.id, true);

        List<String> serverIds = new ArrayList<>();
        for (String id : assignedTasksIds.keySet())
            serverIds.add(id);

        try {
            DownloadTasksToDeleteResult result = proxyLayer.loadAssignedTasksToDelete(serverIds);

            for (String serverIdToDelete : result.TasksToDelete) {
                Long idTask = assignedTasksIds.get(serverIdToDelete);
                deleteTaskFromLocal(idTask);

                syncResult.stats.numDeletes++;
            }
            return result.SyncMark;
        } catch (Exception e) {
            Log.error("Error removing deleted_in_server assigned tasks.", e);
            throw e;
        }
    }

    private Long downloadNewAssignedTasks(SyncResult syncResult, SourceDetails source, ProxyLayer proxyLayer, long lastSyncMarker) throws Exception {
        DownloadTasksResult result;
        try {
            result = proxyLayer.loadNewAssignedTasks(lastSyncMarker);

            List<Task> tasksAdded = new ArrayList<>();
            List<Long> tasksIds = new ArrayList<>();

            for (Task task : result.Tasks) {
                if (repository.existsTask(source.id, task.ID)) continue;

                long taskId = repository.addTask(source.id, source.label, task.ID, task.Context, task.Code, task.Label, task.Description, task.PositionLat, task.PositionLon, task.getSuggestedStartDate(), task.getSuggestedEndDate(), task.Urgent, task.Comments, task.StepCount, TaskDetails.TASK_TRAY_ASSIGNED, true);

                if (this.saveTaskInLocal(taskId, task.ID, proxyLayer)) {
                    tasksAdded.add(task);
                    tasksIds.add(taskId);
                    syncResult.stats.numInserts++;
                } else {
                    this.deleteTaskFromLocal(taskId);
                }
            }

            if (tasksAdded.size() > 0) {
                if (this.preferences.isNotificationsEnabled())
                    NotificationHelper.notifyNewTasks(this.getContext(), source.id, source.label, tasksAdded, tasksIds);
            }

            return result.SyncMark;
        } catch (Exception e) {
            Log.error("Error downloading new assigned tasks.", e);
            throw e;
        }
    }

    private Boolean saveTaskInLocal(long taskId, String taskServerId, ProxyLayer proxyLayer) {
        FileResult taskFile = null;
        try {
            taskFile = proxyLayer.downloadTask(taskServerId);

            ArrayList<Attachment> attachments = ZipUtils.decompress(taskFile.resultFile, LocalStorage.getTaskSourceStore(this.getContext(), String.valueOf(taskId)));
            if (attachments == null) {
                return false;
            }

            File defaultValues = new File(LocalStorage.getTaskSourceStore(this.getContext(), String.valueOf(taskId)), ".default");
            if (defaultValues.exists())
                defaultValues.renameTo(LocalStorage.getTaskResultSchemaFile(this.getContext(), String.valueOf(taskId)));

            repository.addAttachmentsToTask(taskId, attachments);
            return true;

        } catch (Exception e) {
            Log.error("Error downloading task.", e);
            return false;

        } finally {
            if ((taskFile != null) && (taskFile.resultFile != null))
                taskFile.resultFile.delete();
        }
    }

    private void deleteTaskFromLocal(long idTask) {
        LocalStorage.deleteTask(getContext(), idTask);
        repository.deleteTask(idTask);
    }


    private Long downloadDefinitions(SyncResult syncResult, SourceDetails source, ProxyLayer proxyLayer, long lastSyncMarker) throws Exception {
        DownloadDefinitionsResult result;
        try {
            result = proxyLayer.loadNewDefinitions(lastSyncMarker);

            if (result.Definitions.size() > 0) {
                this.repository.clearDefinitions(source.id);

                for (TaskDefinition definition : result.Definitions) {
                    this.repository.addDefinition(source.id, source.label, definition);
                    syncResult.stats.numInserts++;
                }
            }

            return result.SyncMark;
        } catch (Exception e) {
            Log.error("Error downloading new definitions.", e);
            throw e;
        }
    }

    private Long downloadGlossaries(SyncResult syncResult, SourceDetails source, ProxyLayer proxyLayer, long lastSyncMarker) throws Exception {

        DownloadGlossariesResult result;
        try {
            result = proxyLayer.loadNewGlossaries(lastSyncMarker);

            for (Entry<String, String[]> glossary : result.Glossaries.entrySet()) {
                String glossaryCode = source.id + glossary.getKey();

                if (glossary.getValue() == null)
                    glossary.setValue(new String[]{null});

                for (String context : glossary.getValue()) {

                    try {
                        FileResult fileResult = proxyLayer.downloadGlossary(glossary.getKey(), context, lastSyncMarker);

                        this.repository.createGlossary(glossaryCode, context);
                        this.parseTermsFile(glossaryCode, context, fileResult.resultFile);
                    } catch (Exception e) {
                        Log.error("Error downloading glossary with key: " + glossary.getKey(), e);
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            Log.error("Error downloading new glossaries.", e);
            throw e;
        }

        return result.SyncMark;
    }

    private void parseTermsFile(String glossaryCode, String context, File termsFile) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(termsFile);
            if (inputStream.available() > 0) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(inputStream, "UTF-8");
                this.parseTerms(glossaryCode, context, parser);
            }
        } catch (Exception e) {
            Log.error("Error reading schema: " + e.getMessage(), e);
            throw new RuntimeException("Can't read schema", e);
        } finally {
            StreamHelper.close(inputStream);
            termsFile.delete();
        }
    }

    private void parseTerms(String glossaryCode, String context, XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String tagName = parser.getName();
                if (tagName.equals("term")) {
                    String code = parser.getAttributeValue("", "code");
                    String parent = parser.getAttributeValue("", "parent");
                    String tags = parser.getAttributeValue("", "tags");
                    int type = Integer.parseInt(parser.getAttributeValue("", "type"));
                    boolean isEnable = Boolean.parseBoolean(parser.getAttributeValue("", "is-enable"));
                    boolean isLeaf = Boolean.parseBoolean(parser.getAttributeValue("", "is-leaf"));
                    String flattenLabel = parser.getAttributeValue("", "flatten");
                    String label = parser.nextText();

                    this.repository.addOrUpdateTerm(glossaryCode, context, code, parent, label, flattenLabel, tags, type, isEnable, isLeaf);
                }
            }
            eventType = parser.next();
        }
    }
}
