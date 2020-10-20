package org.monet.space.kernel.machines.ttm.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.machines.ttm.CourierService;
import org.monet.space.kernel.machines.ttm.Engine;
import org.monet.space.kernel.machines.ttm.MessageQueueService;
import org.monet.space.kernel.machines.ttm.behavior.CustomerBehavior;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.machines.ttm.model.MailBox;
import org.monet.space.kernel.machines.ttm.model.MailBox.Type;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Signaling;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.model.*;

import java.util.HashMap;

@Singleton
public class CourierServiceMonet implements CourierService {

	@Inject
	private Engine engine;
	@Inject
	private PersistenceService persistenceService;
	@Inject
	private MessageQueueService messageQueueService;

	@Override
	public void signaling(User sender, MailBoxUri mailBoxUri, Signaling signal) {
		MailBox mailBox = this.persistenceService.loadMailBox(mailBoxUri.getId());
		if (mailBox == null)
			return;
		if (!this.persistenceService.isAllowedSenderForMailBox(sender, mailBoxUri.getId())) {
			String taskState = this.persistenceService.loadTask(mailBox.getTaskId()).getState();
			if (signal == Signaling.TERMINATE && taskState.equals(TaskState.FINISHED)) {
				return;
			}
			throw new RuntimeException(String.format("Permission denied. User %s not allowed to communicate with this mailbox.", sender.getName()));
		}

		ProcessBehavior process = this.engine.getProcess(mailBox.getTaskId());
		process.signaling(mailBox, signal);
	}

	@Override
	public void signaling(String orderId, MailBoxUri mailBoxUri, Signaling signal) {
		Message message = new Message();
		message.setTo(mailBoxUri!=null?mailBoxUri.toString():null);
		message.setType(Message.Type.SIGNALING);
		message.setSubject(signal.toString());

		MailBox mailBox = mailBoxUri!=null?this.persistenceService.loadMailBox(mailBoxUri.getId()):null;
		boolean isLocalMessaging = mailBox != null;
		if (isLocalMessaging)
			this.localDeliver(mailBox, message);
		else
			this.messageQueueService.push(orderId, message);
	}

    private String getMailBoxUri(MailBoxUri uri) {
        return this.persistenceService.getUrl(uri);
    }

    public void deliver(User sender, Message message) {
		MailBoxUri recipientMailBox = MailBoxUri.build(message.getTo());

        if (!this.persistenceService.existsMailBox(recipientMailBox.getId())) {
            AgentLogger.getInstance().info(String.format("MailBox %s not found. Perhaps channel was closed.", recipientMailBox.toString()));
            return;
        }

		if (!this.persistenceService.isAllowedSenderForMailBox(sender, recipientMailBox.getId())) {
			AgentLogger.getInstance().error(new RuntimeException(String.format("Permission denied. User %s not allowed to communicate with this mailbox.", sender.getName())));
			return;
		}

		MailBox mailBox = this.persistenceService.loadMailBox(recipientMailBox.getId());
		this.localDeliver(mailBox, message);
	}

	private void localDeliver(MailBox mailBox, Message message) {
		ProcessBehavior process = this.engine.getProcess(mailBox.getTaskId());
		if (message.getType().equals(Message.Type.SIGNALING))
			process.signaling(mailBox, Signaling.valueOf(message.getSubject()));
		else
			process.deliver(mailBox, message);
	}

	@Override
	public void send(String orderId, Message message) {
		MailBoxUri recipientMailBox = MailBoxUri.build(message.getTo());
		MailBox mailBox = this.persistenceService.loadMailBox(recipientMailBox.getId());

		boolean isLocalMessaging = mailBox != null;
		if (isLocalMessaging)
			this.localDeliver(mailBox, message);
		else
			this.messageQueueService.push(orderId, message);
	}

    @Override
    public void sendDelayed(String orderId, Message message) {
        this.messageQueueService.push(orderId, message);
    }

    @Override
	public MailBoxUri openChannel(MailBoxUri fromMailBox, final TaskOrder taskOrder, String taskName) {
        MailBoxUri clientMailBox = null;
		final Role role = taskOrder.getRole();
		boolean isInternal = role instanceof UserRole;
		if (isInternal) {
			Task task = this.persistenceService.createTask(taskName);

			task.setPartnerContext(BusinessUnit.getInstance().getName());
			this.persistenceService.save(task);

			CustomerBehavior customer = task.getProcess().getCustomer();
			final MailBox taskMailBox = this.createMailBox(task.getCode(), task.getId(), Type.CUSTOMER, null);

            customer.initialize(taskMailBox.toUri(), fromMailBox, taskOrder.getInternalSuggestedStartDate(), taskOrder.getInternalSuggestedEndDate(), taskOrder.getComments(), taskOrder.isUrgent(), null);
			clientMailBox = taskMailBox.toUri();
			task.getProcess().resume();

			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(MonetEvent.PARAMETER_MAILBOX, taskMailBox.toUri());
			parameters.put(MonetEvent.PARAMETER_USER_ID, ((UserRole) role).getUserId());
			AgentNotifier.getInstance().notify(new MonetEvent(MonetEvent.TASK_ORDER_REQUEST_SUCCESS, null, taskOrder.getTaskId(), parameters));

		} else {
            ServiceRole serviceRole = ((ServiceRole) role);
			String serviceName = serviceRole.getPartnerServiceName();
            FederationUnit partner = ((ServiceRole) role).getPartner();

			Message message = new Message();
			message.setTo(partner.getServiceUri(serviceName).toString());
			message.setSubject(Message.Type.REQUEST_SERVICE);
			message.setType(Message.Type.REQUEST_SERVICE);
			message.setContent(fromMailBox.toString());

			AgentLogger.getInstance().info("CourierServiceMonet: Added message to message queue. Task id: " + taskOrder.getTaskId() + ". Order id: " + taskOrder.getId() + ". Setup node: " + taskOrder.getSetupNodeId());
			this.messageQueueService.push(taskOrder.getId(), message);
		}

		return clientMailBox;
	}

	@Override
	public MailBoxUri openChannel(MailBoxUri fromMailBoxUri, String taskId) {

		Task task = this.persistenceService.loadTask(taskId);

		CustomerBehavior customer = task.getProcess().getCustomer();

		return customer.getLocalMailBox();
	}

	@Override
	public MailBox createMailBox(String code, String taskId, Type type, User user) {
		MailBox mailBox = new MailBox();
		mailBox.setCode(code);
		mailBox.setTaskId(taskId);
		mailBox.setType(type);
		this.persistenceService.create(mailBox);

		if (user != null)
			this.persistenceService.addMailBoxPermission(mailBox.getId(), user.getId());

		return mailBox;
	}

	@Override
	public void destroyMailBox(MailBoxUri localMailBoxUri) {
		MailBox mailBox = new MailBox();
		mailBox.setId(localMailBoxUri.getId());
		this.persistenceService.delete(mailBox);
	}

}
