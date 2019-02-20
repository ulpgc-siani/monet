package org.monet.space.kernel.components.monet.layers;

import com.vividsolutions.jts.geom.Geometry;
import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.machines.ttm.model.Snapshot;
import org.monet.space.kernel.machines.ttm.model.Timer;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.map.Location;
import org.monet.space.kernel.producers.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class TaskLayerMonet extends PersistenceLayerMonet implements TaskLayer {

	public TaskLayerMonet(ComponentPersistence componentPersistence) {
		super(componentPersistence);
	}

	@Override
	public TaskList loadTasks(String code) {
		ProducerTaskList producerTaskList;
		Account account = this.getAccount();
		DataRequest dataRequest = new DataRequest();

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);
		dataRequest.addParameter(Task.Parameter.TYPE, code);

		return producerTaskList.load(account, dataRequest);
	}

	@Override
	public TaskList loadTasks(Account account, DataRequest dataRequest) {
		ProducerTaskList producerTaskList;
		TaskList taskList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);
		taskList = producerTaskList.load(account, dataRequest);

		return taskList;
	}

	@Override
	public int loadTasksCount(Account account, DataRequest dataRequest) {
		ProducerTaskList producerTaskList;
		Integer result;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);
		result = producerTaskList.loadItemsCount(account, dataRequest);

		return result;
	}

	@Override
	public TaskFilters loadTasksFilters(String language) {
		ProducerTaskList producerTaskList;
		TaskFilters taskFilters;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);
		taskFilters = producerTaskList.getFilters(language);

		return taskFilters;
	}

	@Override
	public RoleList loadTasksRoleList() {
		ProducerTaskList producerTaskList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);

		return producerTaskList.getRoles();
	}

	@Override
	public UserList loadTasksOwnerList() {
		ProducerTaskList producerTaskList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);

		return producerTaskList.getOwners();
	}

	@Override
	public UserList loadTasksSenderList() {
		ProducerTaskList producerTaskList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);

		return producerTaskList.getSenders();
	}

	@Override
	public UserList loadTasksSenderList(String idOwner) {
		ProducerTaskList producerTaskList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);

		return producerTaskList.getSenders(idOwner);
	}

	@Override
	public TaskList searchTasks(Account account, TaskSearchRequest searchRequest) {
		ProducerTaskList producerTaskList;
		TaskList taskList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);
		taskList = producerTaskList.search(account, searchRequest);

		return taskList;
	}

	@Override
	public int searchTasksCount(Account account, TaskSearchRequest searchRequest) {
		ProducerTaskList producerTaskList;
		Integer result;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);
		result = producerTaskList.searchItemsCount(account, searchRequest);

		return result;
	}

	@Override
	public TaskList loadTasks(Node node, String state, String type) {
		ProducerTaskList producerTaskList;
		TaskList taskList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskList = this.producersFactory.get(Producers.TASKLIST);
		taskList = producerTaskList.load(node, state, type);

		return taskList;
	}

	@Override
	public String getTaskIdIfUnique(String taskCode, String classificator) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		return producerTask.getTaskIdIfUnique(taskCode, classificator);
	}

	@Override
	public Task getCurrentInitializerTask() {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		return producerTask.getCurrentInitializerTask();
	}

	@Override
	public void cleanTaskDefinitions() {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.cleanDefinitions();
	}

	@Override
	public void insertTaskDefinition(TaskDefinition taskDefinition) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.updateDefinition(taskDefinition);
	}

	@Override
	public Task loadTask(String idTask) {
		ProducerTask producerTask;
		Task task;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		task = producerTask.load(idTask);

		return task;
	}

	@Override
	public Task createInitializerTask(String code) {
		ProducerTask producerTask;
		Task oTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		oTask = producerTask.create(code, null, true);

		return oTask;
	}

	@Override
	public Task createTask(String code) {
		ProducerTask producerTask;
		Task oTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		oTask = producerTask.create(code, null, false);

		return oTask;
	}

	@Override
	public Task createTask(String code, Node input) {
		ProducerTask producerTask;
		Task oTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		oTask = producerTask.create(code, input, false);

		return oTask;
	}

	@Override
	public void removeTask(String id) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);

		producerTask.remove(id);
	}

	@Override
	public void removeTasks(String sData) {
		String[] aTasks = sData.split(Strings.COMMA);

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		for (String aTask : aTasks) this.removeTask(aTask);
	}

	@Override
	public void saveTask(Task task) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.save(task);
	}

	@Override
	public void saveTaskUrgency(Task task) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.saveUrgency(task);
	}

	@Override
	public void saveTaskState(Task task) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.saveState(task);
	}

	@Override
	public void saveTaskOwner(Task task, User owner, String reason) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.saveOwner(task, owner, reason);
	}

	@Override
	public void saveTaskProcess(Task task) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.saveProcess(task);
	}

	@Override
	public void addTaskFact(Fact fact) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.addFact(fact);
	}

	@Override
	public List<Timer> loadTimers() {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);

		return producerTask.loadTimers();
	}

	@Override
	public void createTimer(Timer timer) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);

		producerTask.createTimer(timer);
	}

	@Override
	public void deleteTimer(String id) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);

		producerTask.deleteTimer(id);
	}

	@Override
	public TaskFactList loadTaskFacts(String taskId) {
		ProducerFactTask producerFactTask;
		TaskFactList result;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerFactTask = this.producersFactory.get(Producers.FACTBOOKTASK);
		result = producerFactTask.load(taskId);

		return result;
	}

	@Override
	public List<Fact> loadTaskFactEntries(String taskId, int startPos, int limit) {
		ProducerFactTask producerFactBookTask;
		List<Fact> entryList;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerFactBookTask = this.producersFactory.get(Producers.FACTBOOKTASK);
		entryList = producerFactBookTask.load(taskId, startPos, limit);

		return entryList;
	}

	@Override
	public int getTaskFactEntriesCount(String idTask) {
		ProducerFactTask producerFactBookTask;
		int count;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerFactBookTask = this.producersFactory.get(Producers.FACTBOOKTASK);
		count = producerFactBookTask.getCount(idTask);

		return count;
	}

	@Override
	public void runTask(Task task) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.run(task);
	}

	@Override
	public void updateTaskLocation(Task task, Geometry geometry) {

		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerTask producerTask = this.producersFactory.get(Producers.TASK);

		Location location = new Location();
		location.setGeometry(geometry);
		location.setDescription(task.getLabel());
		task.setLocation(location);

		producerTask.updateLocation(task);
	}

	@Override
	public void deleteTask(String id) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.deleteTask(id);
	}

	@Override
	public void abortTask(String id) {
		ProducerTask producerTask;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.abortTask(id);
	}

	@Override
	public Snapshot loadLastSnapshot(String taskId) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		return producerTask.loadLastSnapshot(taskId);
	}

	@Override
	public Snapshot loadAndDeleteLastSnapshot(String taskId) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		return producerTask.loadAndDeleteLastSnapshot(taskId);
	}

	@Override
	public boolean hasSnapshots(String taskId) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		return producerTask.hasSnapshots(taskId);
	}

	@Override
	public void saveSnapshot(String taskId, Snapshot snapshot) {
		ProducerTask producerTask;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTask = this.producersFactory.get(Producers.TASK);
		producerTask.saveSnapshot(taskId, snapshot);
	}

	@Override
	public TaskOrder loadTaskOrder(String orderId) {
		ProducerTaskOrder producerTaskOrder;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskOrder = this.producersFactory.get(Producers.TASKORDER);

		return producerTaskOrder.load(orderId);
	}

	@Override
	public TaskOrder loadTaskOrderByCode(String taskId, String code) {
		ProducerTaskOrder producerTaskOrder;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskOrder = this.producersFactory.get(Producers.TASKORDER);

		return producerTaskOrder.loadByCode(taskId, code);
	}

	@Override
	public TaskOrder createTaskOrder(Task task, String code, String roleId, TaskOrder.Type type, boolean urgent) {
		ProducerTaskOrder producerTaskOrder;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskOrder = this.producersFactory.get(Producers.TASKORDER);

		return producerTaskOrder.create(task, code, roleId, type, urgent);
	}

	@Override
	public TaskOrder createTaskOrder(Task task, String code, TaskOrder.Type type, Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent, String partnerContext) {
		ProducerTaskOrder producerTaskOrder;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskOrder = this.producersFactory.get(Producers.TASKORDER);

		return producerTaskOrder.create(task, code, type, suggestedStartDate, suggestedEndDate, comments, urgent, partnerContext);
	}

	@Override
	public void saveTaskOrder(TaskOrder order) {
		ProducerTaskOrder producerTaskOrder;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskOrder = this.producersFactory.get(Producers.TASKORDER);
		producerTaskOrder.save(order);
	}

	@Override
	public void resetTaskOrderNewMessages(Task task, String idOrder) {
		ProducerTaskOrder producerTaskOrder;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskOrder = this.producersFactory.get(Producers.TASKORDER);
		producerTaskOrder.resetOrderNewMessages(task, idOrder);
	}

	@Override
	public RoleList loadTaskOrdersRoleList(String taskId) {
		ProducerTaskOrder producerTaskOrder;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerTaskOrder = this.producersFactory.get(Producers.TASKORDER);

		return producerTaskOrder.loadRoles(taskId);
	}

	@Override
	public Map<String, TaskOrder> requestTaskOrderListItems(String taskId, DataRequest dataRequest) {
		ProducerTaskOrderList producerTaskOrderList;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerTaskOrderList = this.producersFactory.get(Producers.TASKORDERLIST);

		return producerTaskOrderList.loadItems(taskId, dataRequest);
	}

	@Override
	public int requestTaskOrderListItemsCount(String taskId) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		return this.producersFactory.<ProducerTaskOrderList>get(Producers.TASKORDERLIST).loadItemsCount(taskId);
	}

	@Override
	public List<ChatItem> requestChatListItems(String orderId, DataRequest dataRequest) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		return producersFactory.<ProducerTaskOrder>get(Producers.TASKORDER).loadChatItems(orderId, dataRequest);
	}

	@Override
	public int requestChatListItemsCount(String orderId) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		return producersFactory.<ProducerTaskOrder>get(Producers.TASKORDER).loadChatItemsCount(orderId);
	}

	@Override
	public void addChatListItem(Task task, String idOrder, ChatItem chatItem) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producersFactory.<ProducerTaskOrder>get(Producers.TASKORDER).addChatItem(task, idOrder, chatItem);
	}

	@Override
	public void updateChatListItemStateToSent(String chatItemId) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		producersFactory.<ProducerTaskOrder>get(Producers.TASKORDER).updateChatItemStateToSent(chatItemId);
	}

	@Override
	public List<org.monet.mobile.model.Task> loadJobsNotRead(String ownerId, long syncMark) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		return producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).loadNotRead(ownerId, syncMark);
	}

	@Override
	public List<org.monet.mobile.model.Task> loadAvailableJobsNotRead(String ownerId, long syncMark) {

		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		return producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).loadAvailableNotRead(ownerId, syncMark);
	}

	@Override
	public List<String> loadAssignedJobsToDelete(String ownerId, List<String> ids) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		return producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).loadAssignedJobsToDelete(ownerId, ids);
	}

	@Override
	public List<String> loadFinishedJobsToDelete(String ownerId, List<String> ids) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		return producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).loadFinishedJobsToDelete(ownerId, ids);
	}

	@Override
	public List<String> loadUnassignedJobsToDelete(List<String> ids) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		return producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).loadUnassignedJobsToDelete(ids);
	}

	@Override
	public void loadJobRequest(String taskId, OutputStream messageStream) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).loadRequest(taskId, messageStream);
	}

	@Override
	public void loadJobResponse(String taskId, OutputStream messageStream) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).loadResponse(taskId, messageStream);
	}

	@Override
	public String createJob(Task task, InputStream messageStream, String callbackTaskId, String callbackCode, String callbackOrderId) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		return producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).create(task, messageStream, callbackTaskId, callbackCode, callbackOrderId);
	}

    @Override
    public void removeJob(String id) {
	    if (!isStarted())
            throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

	    producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).remove(id);
    }

    @Override
	public void saveJobResponse(String taskId, InputStream messageStream) {

	    if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

	    producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).save(taskId, messageStream);
	}

	@Override
	public void addJobAttachment(String taskId, String attachmentId) {

		if (!isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		this.producersFactory.<ProducerTaskJob>get(Producers.TASKJOB).addAttachment(taskId, attachmentId);
	}

	@Override
	public List<String> loadJobAttachments(String taskId) {
		ProducerTaskJob producerTaskJob;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskJob = this.producersFactory.get(Producers.TASKJOB);

		return producerTaskJob.loadAttachments(taskId);
	}

	@Override
	public JobCallbackTask getJobCallbackTask(String taskId) {
		ProducerTaskJob producerTaskJob;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskJob = this.producersFactory.get(Producers.TASKJOB);

		return producerTaskJob.getJobCallbackTask(taskId);
	}

	@Override
	public List<org.monet.mobile.model.Task> loadAvailableJobs(String userId, int offset, int count) {
		ProducerTaskJob producerTaskJob;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskJob = this.producersFactory.get(Producers.TASKJOB);

		return producerTaskJob.loadAvailableTasks(userId, offset, count);
	}

	@Override
	public void assignJob(Account account, String jobId) {
		ProducerTaskJob producerTaskJob;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskJob = this.producersFactory.get(Producers.TASKJOB);

		producerTaskJob.assignJob(account, jobId);
	}

	@Override
	public void unassignJob(Account account, String jobId) {
		ProducerTaskJob producerTaskJob;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskJob = this.producersFactory.get(Producers.TASKJOB);

		producerTaskJob.unassignJob(account, jobId);
	}

	@Override
	public void addJobChatListItem(String jobId, ChatItem chatItem) {
		ProducerTaskJob producerTaskJob;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskJob = this.producersFactory.get(Producers.TASKJOB);

		producerTaskJob.addChatListItem(jobId, chatItem);
	}

	@Override
	public List<ChatItem> loadJobNewChatItems(String userId, long syncMark) {
		ProducerTaskJob producerTaskJob;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskJob = this.producersFactory.get(Producers.TASKJOB);

		return producerTaskJob.loadJobNewChatItems(userId, syncMark);
	}

	@Override
	public String getJobUserIdForTaskOrder(String taskOrderId) {
		ProducerTaskJob producerTaskJob;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTaskJob = this.producersFactory.get(Producers.TASKJOB);

		return producerTaskJob.loadUserIdForTaskOrder(taskOrderId);
	}

}
