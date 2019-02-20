package org.monet.space.kernel.machines.ttm.persistence;

import org.monet.space.kernel.machines.ttm.model.MailBox;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Process;
import org.monet.space.kernel.machines.ttm.model.Timer;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Role.Nature;

import java.util.Date;
import java.util.List;

public interface PersistenceService {

	Task createTask(String taskName);

	void save(Task task);

	Task loadTask(String processId);

	void resumeJob(Task task, Message message, String callbackTaskId, String callbackCode, String callbackOrderId);

	void updateTaskState(Process process, String newState);

	String findCollaboratorTask(String taskName, String classificator);

	Process loadProcess(String processId);

	void save(Process process);

	void create(MailBox mailBox);

    boolean existsMailBox(String mailBoxId);

	MailBox loadMailBox(String mailBoxId);

	void delete(MailBox mailBox);

	void addMailBoxPermission(String mailBoxId, String userId);

    boolean isAllowedSenderForMailBox(User sender, String recipientMailBox);

	List<Timer> loadAllTimers();

	void create(Timer timer);

	void delete(Timer timer);

	Node createForm(String taskId, String definitionName);

	Node loadForm(String nodeId);

	void saveForm(Node node);

	void deleteNodeAndRemoveFromTrash(String nodeId);

	TaskOrder loadTaskOrder(String id);

	TaskOrder createTaskOrder(String taskId, String code, String roleId, TaskOrder.Type type, boolean urgent);

	TaskOrder createTaskOrder(String taskId, String code, TaskOrder.Type customer, Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent, String partnerContext);

	void saveTaskOrder(TaskOrder order);

	void addTaskOrderChatItem(String taskId, String code, String content);

	RoleList loadNonExpiredRoleList(String roleName, Nature nature);

    String getUrl(Uri uri);
}
