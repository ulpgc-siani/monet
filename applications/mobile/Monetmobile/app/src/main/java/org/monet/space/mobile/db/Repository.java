package org.monet.space.mobile.db;

import android.database.Cursor;
import org.monet.mobile.model.TaskDefinition;
import org.monet.space.mobile.model.ChatItem;
import org.monet.space.mobile.model.SourceDetails;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.model.TaskDetails.Attachment;
import org.monet.space.mobile.mvp.content.SimpleDataLoader.SimpleDataObserver;

import java.util.Date;
import java.util.Map;
import java.util.List;

public interface Repository extends SimpleDataObserver {
  
  //Sources
  
  Cursor getSources();
  
  SourceDetails getSource(long sourceId);

  void addSource(String label, String title, String subtitle, String account, String username);

  void updateSource(long sourceId, String label, String title, String subtitle, String account, String username);
  
  long[] getOldSources(String currentAccounts);

  void deleteSource(long sourceId);

  void deleteSource(String accountName);

  SourceDetails getSourceOfUser(String accountName);
  
  
  //Tasks
  
  Cursor getTasksAssigned(long sourceId, double latitude, double longitude, String sort, String filter);

  Cursor getTasksFinished(long sourceId, double latitude, double longitude, String sort, String filter);

  Cursor getTasksUnassigned(long sourceId, double latitude, double longitude, String sort, String filter);

  Cursor getTasksAssignedWithLocations(long sourceId, String filter);

  Cursor getTasksFinishedWithLocations(long sourceId, String filter);
  
  Cursor getTasksUnassignedWithLocations(long sourceId, String filter);

  long[] getTasksIds(long sourceId);

  long getTaskId(long sourceId, String serverTaskId);

  String getTaskServerId(long id);

  TaskDetails getTaskDetails(long taskId);
  
  long addTask(long sourceId, String sourceLabel, String serverId, String context, String code, String label, String description, Double positionLat, Double positionLon, Date suggestedStartDate, Date suggestedEndDate, boolean urgent, String comments, int stepCount, int tray, Boolean isSynchronized);

  void updateTaskStartDate(long taskId, Date date);

  void updateTaskToFinishState(long taskId, int step, Date date);
  
  void updateTaskStep(long taskId, int step, int stepIteration);
  
  void updateTaskAsSynchronized(long idTask);

  void updateTaskToAssigned(long taskId);

  void updateTaskToUnassigned(long taskId);
  
  void updateTaskServerId(long taskId, String serverId);
  
  void updateTaskLabel(long taskId, String taskLabel);
  
  void deleteTask(long taskId);
  
  boolean existsTask(long sourceId, String serverId);
  
  Map<String, Long> getAssignedTasksIds(long sourceId, Boolean isSynchronized);

  Map<String, Long> getFinishedTasksIds(long sourceId, Boolean isSynchronized);

  Map<String, Long> getUnassignedTasksIds(long sourceId, Boolean isSynchronized);
  
  long [] getFinishedAndExpiredTasksIds(long sourceId, Date date);

  void addAttachmentsToTask(long taskId, List<Attachment> attachments);


  //Definitions
  
  void addDefinition(long sourceId, String sourceLabel, TaskDefinition definition);

  long getDefinitionId(long sourceId, String code);

  Cursor getDefinitions(long sourceId);

  long getDefinitionSource(long definitionId);

  TaskDefinition getDefinition(long id);

  void clearDefinitions(long sourceId);

  
  //Glossaries
  
  void createGlossary(String code, String context);

  void emptyGlossary(String code, String context);
  
  void deleteGlossary(String code, String context);

  void addOrUpdateTerm(String glossaryCode, String context, String code, String parent, String label, String flattenLabel, String tags, int type, boolean isEnable, boolean isLeaf);

  Cursor getTerms(String glossaryCode, String context, String parent, String searchFilter, List<String> tagFilters, boolean onlyInternal, int level);

  boolean hasChildren(String glossaryCode, String context, String termCode, Boolean areLeaf);
  
  
  //Chats

  ChatItem addChatItem(long taskId, String serverId, long sourceId, String message, Date dateTime, boolean isOut);
  
  ChatItem addChatItem(long taskId, String serverId, long sourceId, String message, Date datetime, boolean isOut, boolean isSent);
  
  void updateChatItemToSent(long chatItemId);

  List<ChatItem> getChatItems(long taskId);

  void updateNotReadChatCount(long taskId, int newChats);
  
  void resetNotReadChatCount(long taskId);
  
  List<org.monet.space.mobile.model.ChatItem> getChatItemsNotSent(long id);

  void markChatItemsAsSent(List<org.monet.space.mobile.model.ChatItem> chatItemsNotSent);


  //Files
  long newFileId();

  long updateFileName(long fileId, String name);

  void consolidateFile(String name);
  
  boolean isConsolidatedFile(String name);
  
}
