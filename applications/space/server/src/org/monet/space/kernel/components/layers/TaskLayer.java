package org.monet.space.kernel.components.layers;

import com.vividsolutions.jts.geom.Geometry;
import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.machines.ttm.model.Snapshot;
import org.monet.space.kernel.machines.ttm.model.Timer;
import org.monet.space.kernel.model.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public interface TaskLayer extends Layer {

	// Task
	Task loadTask(String idTask);

	Task createInitializerTask(String code);

	Task createTask(String code);

	Task createTask(String code, Node input);

	void removeTask(String id);

	void removeTasks(String data);

	void saveTask(Task task);

	void saveTaskUrgency(Task task);

	void saveTaskState(Task task);

	void saveTaskOwner(Task task, User owner, String reason);

	void deleteTask(String id);

	void abortTask(String id);

	// Task List
	TaskFilters loadTasksFilters(String language);

	RoleList loadTasksRoleList();

	UserList loadTasksOwnerList();

	UserList loadTasksSenderList();

	UserList loadTasksSenderList(String idOwner);

	TaskList searchTasks(Account account, TaskSearchRequest searchRequest);

	int searchTasksCount(Account account, TaskSearchRequest searchRequest);

	TaskList loadTasks(String code);

	TaskList loadTasks(Account account, DataRequest dataRequest);

	int loadTasksCount(Account account, DataRequest dataRequest);

	TaskList loadTasks(Node node, String state, String Type);

	String getTaskIdIfUnique(String taskCode, String classificator);

	Task getCurrentInitializerTask();

	// Task Definitions
	void insertTaskDefinition(TaskDefinition taskDefinition);

	void cleanTaskDefinitions();

	// Task Facts
	void addTaskFact(Fact fact);

	TaskFactList loadTaskFacts(String id);

	List<Fact> loadTaskFactEntries(String taskId, int startPos, int limit);

	int getTaskFactEntriesCount(String idTask);

	// Task Process
	void saveTaskProcess(Task task);

	void runTask(Task task);

	void updateTaskLocation(Task task, Geometry geometry);

	// Task Timers
	List<Timer> loadTimers();

	void createTimer(Timer timer);

	void deleteTimer(String id);

	// Task Process Snapshot
	Snapshot loadLastSnapshot(String taskId);

	Snapshot loadAndDeleteLastSnapshot(String taskId);

	boolean hasSnapshots(String taskId);

	void saveSnapshot(String taskId, Snapshot snapshot);

	// Task Orders
	TaskOrder loadTaskOrder(String orderId);

	TaskOrder loadTaskOrderByCode(String taskId, String code);

	TaskOrder createTaskOrder(Task task, String code, String roleId, TaskOrder.Type type, boolean urgent);

	TaskOrder createTaskOrder(Task task, String code, TaskOrder.Type type, Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent, String partnerContext);

	void saveTaskOrder(TaskOrder order);

	void resetTaskOrderNewMessages(Task task, String idOrder);

	RoleList loadTaskOrdersRoleList(String taskId);

	Map<String, TaskOrder> requestTaskOrderListItems(String taskId, DataRequest dataRequest);

	int requestTaskOrderListItemsCount(String taskId);

	List<ChatItem> requestChatListItems(String orderId, DataRequest dataRequest);

	int requestChatListItemsCount(String orderId);

	void addChatListItem(Task task, String idOrder, ChatItem chatItem);

	void updateChatListItemStateToSent(String chatItemId);

	// Task job
	List<org.monet.mobile.model.Task> loadAvailableJobs(String userId, int offset, int count);

	List<String> loadAssignedJobsToDelete(String id, List<String> ids);

	List<String> loadFinishedJobsToDelete(String ownerId, List<String> ids);

	List<String> loadUnassignedJobsToDelete(List<String> ids);

	List<org.monet.mobile.model.Task> loadJobsNotRead(String ownerId, long syncMark);

	List<org.monet.mobile.model.Task> loadAvailableJobsNotRead(String ownerId, long syncMark);

	void loadJobRequest(String taskId, OutputStream messageStream);

	void loadJobResponse(String taskId, OutputStream messageStream);

	String createJob(Task task, InputStream messageStream, String callbackTaskId, String callbackCode, String callbackOrderId);

    void removeJob(String id);

	void saveJobResponse(String taskId, InputStream messageStream);

	void addJobAttachment(String taskId, String attachmentId);

	List<String> loadJobAttachments(String taskId);

	JobCallbackTask getJobCallbackTask(String taskId);

	void assignJob(Account account, String taskId);

	void unassignJob(Account account, String taskId);

	void addJobChatListItem(String jobId, ChatItem chatItem);

	List<ChatItem> loadJobNewChatItems(String userId, long syncMark);

	String getJobUserIdForTaskOrder(String taskOrderId);
}
