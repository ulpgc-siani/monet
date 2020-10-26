package org.monet.space.kernel.machines.ttm.behavior;

import com.google.inject.Inject;
import org.monet.metamodel.TaskProviderProperty;
import org.monet.metamodel.TaskRequestProperty;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.machines.ttm.CourierService;
import org.monet.space.kernel.machines.ttm.model.MailBox;
import org.monet.space.kernel.machines.ttm.model.MailBox.Type;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Provider;
import org.monet.space.kernel.machines.ttm.model.Signaling;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceHandler;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.model.MailBoxUri;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Role;
import org.monet.space.kernel.model.TaskOrder;

import java.util.Collection;
import java.util.Date;

public class ProviderBehavior extends Behavior {

	@Inject
	private CourierService courierService;
	@Inject
	private AgentNotifier agentNotifier;

	private String taskId;
	private TaskProviderProperty declaration;
	private Provider model;
	private boolean starting = false;

	public void inject(String taskId, TaskProviderProperty declaration, Provider model, PersistenceHandler persistenceHandler, PersistenceService persistenceService) {
		this.taskId = taskId;
		this.declaration = declaration;
		this.model = model;
		this.persistenceHandler = persistenceHandler;
		this.persistenceService = persistenceService;
	}

	public Provider getModel() {
		return this.model;
	}

	public TaskOrder initialize(String id, TaskProviderProperty declaration, Role role, boolean urgent) {
		TaskOrder taskOrder;

		this.taskId = id;
		this.declaration = declaration;

		if (this.model.getOrderId() != null) {
			taskOrder = this.persistenceService.loadTaskOrder(this.model.getOrderId());
			taskOrder.setRole(role);
			this.persistenceService.saveTaskOrder(taskOrder);
		} else {
			taskOrder = this.persistenceService.createTaskOrder(this.taskId, declaration.getCode(), role.getId(), TaskOrder.Type.provider, urgent);
			taskOrder.setUrgent(true);

			this.model.setOrderId(taskOrder.getId());
			this.persistenceHandler.save();
		}

		return taskOrder;
	}

	public void start() {
		if (this.model.isOpen()) {
			throw new RuntimeException("Provider already open.");
		}

		if (this.starting) {
			AgentLogger.getInstance().info("Provider already starting for task " + this.taskId + " and order " + this.model.getOrderId());
			return;
		}

		this.starting = true;

		String orderId = this.model.getOrderId();
		TaskOrder taskOrder = this.persistenceService.loadTaskOrder(orderId);
		Role role = taskOrder.getRole();

		MailBox.Type mailBoxType = null;
		String internalTask = null;

		if (role.isUserRole()) {
			this.model.setInternal(true);
			mailBoxType = Type.INTERNAL_PROVIDER;

			if (this.declaration.getInternal() == null) {
				this.model.setFailureDate(new Date());
				this.persistenceHandler.save();
				return;
			}

			internalTask = this.declaration.getInternal().getService().getValue();
		} else if (role.isServiceRole() || role.isFeederRole()) {
			this.model.setInternal(false);

			if (this.declaration.getExternal() == null) {
				this.model.setFailureDate(new Date());
				this.persistenceHandler.save();
				return;
			}

			mailBoxType = Type.EXTERNAL_PROVIDER;
		}

		this.model.setLocalMailBox(this.courierService.createMailBox(declaration.getCode(), this.taskId, mailBoxType, null).toUri());

		this.courierService.openChannel(this.model.getLocalMailBox(), taskOrder, internalTask);

		this.persistenceHandler.save();
	}

	public boolean isOpen() {
		return model.isOpen();
	}

	public void complete(MailBoxUri providerMailbox) {
		this.model.setServiceMailBox(providerMailbox);

		this.model.setOpen(true);
		this.starting = false;
		this.persistenceHandler.save();

		try {
			MonetEvent event = new MonetEvent(MonetEvent.TASK_PROVIDER_INITIALIZED, null, this.taskId);
			event.addParameter(MonetEvent.PARAMETER_PROVIDER, (this.model.isInternal() ? "internal" : "external") + declaration.getName());
			this.agentNotifier.notify(event);
		}
		catch (Throwable exception) {
			this.model.setFailureDate(new Date());
			this.persistenceHandler.save();
			throw exception;
		}
	}

	public void send(String requestName) throws Throwable {
		if (!model.isOpen())
			throw new RuntimeException(String.format("Provider %s is closed", this.declaration.getName()));

		TaskRequestProperty request = null;
		if (this.model.isInternal()) {
			request = this.declaration.getInternal().getRequestMap().get(requestName);
		} else {
			request = this.declaration.getExternal().getRequestMap().get(requestName);
		}

		Message message = new Message();
		message.setTo(this.model.getServiceMailBox().toString());
		message.setSubject(request.getCode());

		try {
			MonetEvent event = new MonetEvent(MonetEvent.TASK_PROVIDER_REQUEST_CONSTRUCTOR, null, this.taskId);
			event.addParameter(MonetEvent.PARAMETER_PROVIDER, (this.model.isInternal() ? "internal" : "external") + declaration.getName());
			event.addParameter(MonetEvent.PARAMETER_CODE, message.getSubject());
			event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
			this.agentNotifier.notify(event);
		}
		catch (Throwable exception) {
			this.model.setFailureDate(new Date());
			this.persistenceHandler.save();
			throw exception;
		}

		this.courierService.send(this.model.getOrderId(), message);
	}

	public void chat(String id, String messageContent) {
		if (!model.isOpen())
			throw new RuntimeException(String.format("Provider %s is closed", this.declaration.getName()));

		Message message = new Message();
		message.setType(Message.Type.CHAT);
		message.setSubject(id);
		message.setContent(messageContent);
		message.setTo(this.model.getServiceMailBox().toString());

		this.courierService.send(this.model.getOrderId(), message);
	}

	public void process(Message message) {

		if (message.isChat()) {
			this.processChat(message);
			return;
		}

		String code = message.getSubject();
		if (!checkResponseMessageCode(code)) {
			throw new RuntimeException("Message response code '" + code + "' not recognized in " + declaration.getName() + " for task " + this.taskId);
		}

		MonetEvent event = new MonetEvent(MonetEvent.TASK_PROVIDER_RESPONSE_IMPORT, null, this.taskId);
		event.addParameter(MonetEvent.PARAMETER_PROVIDER, (this.model.isInternal() ? "internal" : "external") + declaration.getName());
		event.addParameter(MonetEvent.PARAMETER_CODE, code);
		event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
		this.agentNotifier.notify(event);
	}

	private boolean checkResponseMessageCode(String code) {
		if (this.model.isInternal()) {
			Collection<TaskProviderProperty.InternalProperty.ResponseProperty> responseList = declaration.getInternal().getResponseList();
			for (TaskProviderProperty.InternalProperty.ResponseProperty response : responseList) {
				if (response.getCode().equals(code)) return true;
			}
			return false;
		}
		else {
			Collection<TaskProviderProperty.ExternalProperty.ResponseProperty> responseList = declaration.getExternal().getResponseList();
			for (TaskProviderProperty.ExternalProperty.ResponseProperty response : responseList) {
				if (response.getCode().equals(code)) return true;
			}
			return false;
		}
	}

	private void processChat(Message message) {

		this.persistenceService.addTaskOrderChatItem(this.taskId, this.declaration.getCode(), message.getContent());

		MonetEvent event = new MonetEvent(MonetEvent.TASK_ORDER_CHAT_MESSAGE_RECEIVED, null, this.taskId);
		event.addParameter(MonetEvent.PARAMETER_PROVIDER, (this.model.isInternal() ? "internal" : "external") + declaration.getName());
		event.addParameter(MonetEvent.PARAMETER_CODE, this.declaration.getCode());
		event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
		this.agentNotifier.notify(event);
	}

	public void expire() {
		this.finish(MonetEvent.TASK_PROVIDER_EXPIRED);
	}

	public void reject() {
		this.finish(MonetEvent.TASK_PROVIDER_REJECTED);
	}

	public void terminate() {
		this.finish(MonetEvent.TASK_PROVIDER_TERMINATED);
	}

	private void finish(String eventCode) {
		this.markAsCompleted(this.model.getOrderId());

		this.model.reset();
		this.model.setOpen(false);
		this.starting = false;
		this.courierService.destroyMailBox(this.model.getLocalMailBox());
		this.persistenceHandler.save();

		MonetEvent event = new MonetEvent(eventCode, null, this.taskId);
		event.addParameter(MonetEvent.PARAMETER_PROVIDER, (this.model.isInternal() ? "internal" : "external") + declaration.getName());
		this.agentNotifier.notify(event);
	}

	public void abort() {
		String orderId = this.model.getOrderId();
		this.markAsCompleted(orderId);
		this.starting = false;
		this.courierService.signaling(orderId, this.model.getServiceMailBox(), Signaling.ABORT);
	}

}
