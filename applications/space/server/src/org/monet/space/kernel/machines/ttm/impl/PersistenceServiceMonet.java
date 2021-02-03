package org.monet.space.kernel.machines.ttm.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.agents.AgentFederationUnit;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.layers.MailBoxLayer;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.library.LibraryZip;
import org.monet.space.kernel.machines.ttm.model.MailBox;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;
import org.monet.space.kernel.machines.ttm.model.Process;
import org.monet.space.kernel.machines.ttm.model.Timer;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Role.Nature;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.PersisterHelper;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class PersistenceServiceMonet implements PersistenceService {

	@Inject
	private NodeLayer nodeLayer;
	@Inject
	private Provider<RoleLayer> roleLayerProvider;
	@Inject
	private TaskLayer taskLayer;
	@Inject
	private MailBoxLayer mailBoxLayer;

	@Override
	public Task createTask(String taskName) {
		TaskDefinition taskDefinition = org.monet.space.kernel.model.Dictionary.getInstance().getTaskDefinition(taskName);
		Task task = this.taskLayer.createTask(taskDefinition.getCode());

		task.setLabel(taskDefinition.getLabelString());
		task.setDescription(taskDefinition.getDescriptionString());
		task.setEndSuggestedDate(null);
		return task;
	}

	@Override
	public void save(Task task) {
		this.taskLayer.saveTask(task);
	}

	@Override
	public Task loadTask(String taskId) {
		return this.taskLayer.loadTask(taskId);
	}

	@Override
	public void resumeJob(Task task, Message message, String callbackTaskId, String callbackCode, String callbackOrderId) {
		File messageFile = null;
		InputStream messageStream = null;

		try {
			messageFile = File.createTempFile("Message", "", new File(Configuration.getInstance().getTempDir()));
			ZipOutputStream outputStream = null;

			try {
				outputStream = new ZipOutputStream(new FileOutputStream(messageFile));

				if (message.getDefaultValues() != null) {
					StringWriter writer = new StringWriter();
					PersisterHelper.save(writer, message.getDefaultValues());
					InputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes("UTF-8"));
					LibraryZip.addZipEntry(".default", ".default", inputStream, outputStream);
				}

				int i = 0;
				for (MessageAttach attach : message.getAttachments()) {
					LibraryZip.addZipEntry(String.format("attach_%d.%s", i, MimeTypes.getInstance().getExtension(attach.getContentType())), String.format("%s;%s", attach.getKey(), attach.getContentType()), attach.getInputStream(), outputStream);
					if (attach.isDocument()) LibraryZip.addZipEntry(String.format("attach_%d" + MonetReferenceFileExtension, i), String.format("%s;%s", attach.getKey(), MimeTypes.TEXT), attach.getDocumentReference(), outputStream);
					i++;
				}
			} finally {
				StreamHelper.close(outputStream);
			}

			messageStream = new FileInputStream(messageFile);

			this.taskLayer.createJob(task, messageStream, callbackTaskId, callbackCode, callbackOrderId);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			StreamHelper.close(messageStream);
			if (messageFile != null)
				messageFile.delete();
		}
	}

	@Override
	public void updateTaskState(Process process, String newState) {
		Task task = this.taskLayer.loadTask(process.getId());
		task.setState(newState);
		this.taskLayer.saveTaskState(task);
	}

	@Override
	public String findCollaboratorTask(String taskName, String classificator) {
		String taskCode = Dictionary.getInstance().getTaskDefinition(taskName).getCode();
		return this.taskLayer.getTaskIdIfUnique(taskCode, classificator);
	}

	@Override
	public Process loadProcess(String processId) {
		Task task = this.taskLayer.loadTask(processId);
		Process process = null;
		try {
			String taskData = task.getData().toString();
			if (taskData != null && !taskData.isEmpty())
				process = PersisterHelper.load(taskData, Process.class);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new RuntimeException("Error deserializing process", e);
		}
		return process;
	}

	@Override
	public void save(Process process) {
		Task task = this.taskLayer.loadTask(process.getId());
		StringWriter writer = new StringWriter();
		try {
			PersisterHelper.save(writer, process);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
		task.setData(writer.getBuffer());
		this.taskLayer.saveTaskProcess(task);
	}

	@Override
	public void create(MailBox mailBox) {
		this.mailBoxLayer.create(mailBox);
	}

	@Override
	public boolean existsMailBox(String mailBoxId) {
		return this.mailBoxLayer.exists(mailBoxId);
	}

	@Override
	public MailBox loadMailBox(String mailBoxId) {
		return this.mailBoxLayer.load(mailBoxId);
	}

	@Override
	public void delete(MailBox mailBox) {
		this.mailBoxLayer.delete(mailBox.getId());
	}

	@Override
	public void addMailBoxPermission(String mailBoxId, String userId) {
		this.mailBoxLayer.addPermission(mailBoxId, userId);
	}

	@Override
	public boolean isAllowedSenderForMailBox(User sender, String recipientMailBox) {
		return this.mailBoxLayer.hasPermission(recipientMailBox, sender.getId());
	}

	@Override
	public List<Timer> loadAllTimers() {
		return this.taskLayer.loadTimers();
	}

	@Override
	public void create(Timer timer) {
		this.taskLayer.createTimer(timer);
	}

	@Override
	public void delete(Timer timer) {
		this.taskLayer.deleteTimer(timer.getId());
	}

	@Override
	public Node createForm(String taskId, String definitionName) {
		Task task = this.taskLayer.loadTask(taskId);
		return this.nodeLayer.addNode(definitionName, (Node)null);
	}

	@Override
	public Node loadForm(String nodeId) {
		return this.nodeLayer.loadNode(nodeId);
	}

	@Override
	public void saveForm(Node node) {
		this.nodeLayer.saveNode(node);
	}

	@Override
	public void deleteNodeAndRemoveFromTrash(String nodeId) {
		this.nodeLayer.deleteAndRemoveNodeFromTrash(nodeId);
	}

	@Override
	public TaskOrder loadTaskOrder(String id) {
		return this.taskLayer.loadTaskOrder(id);
	}

	@Override
	public TaskOrder createTaskOrder(String taskId, String code, String roleId, TaskOrder.Type type, boolean urgent) {
		Task task = this.taskLayer.loadTask(taskId);
		return this.taskLayer.createTaskOrder(task, code, roleId, type, urgent);
	}

	public TaskOrder createTaskOrder(String taskId, String code, TaskOrder.Type type, Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent, String partnerContext) {
		Task task = this.taskLayer.loadTask(taskId);
		return this.taskLayer.createTaskOrder(task, code, type, suggestedStartDate, suggestedEndDate, comments, urgent, partnerContext);
	}

	@Override
	public void saveTaskOrder(TaskOrder order) {
		this.taskLayer.saveTaskOrder(order);
	}

	@Override
	public void addTaskOrderChatItem(String taskId, String code, String content) {
		Task task = this.taskLayer.loadTask(taskId);
		TaskOrder taskOrder = this.taskLayer.loadTaskOrderByCode(taskId, code);
		ChatItem chatItem = new ChatItem();
		chatItem.setCode(code);
		chatItem.setOrderId(taskOrder.getId());
		chatItem.setMessage(content);
		chatItem.setType(ChatItem.Type.in);
		this.taskLayer.addChatListItem(task, taskOrder.getId(), chatItem);
	}

	@Override
	public RoleList loadNonExpiredRoleList(String roleName, Nature nature) {
		String roleCode = Dictionary.getInstance().getRoleDefinition(roleName).getCode();
		DataRequest dataRequest = new DataRequest();
		dataRequest.setStartPos(0);
		dataRequest.setLimit(-1);
		dataRequest.addParameter(DataRequest.NATURE, nature.toString());
		dataRequest.addParameter(DataRequest.NON_EXPIRED, "true");
		return this.roleLayerProvider.get().loadRoleList(roleCode, dataRequest);
	}

	@Override
	public String getUrl(Uri uri) {
		return AgentFederationUnit.getUrl(uri);
	}

}
