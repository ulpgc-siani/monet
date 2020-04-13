package org.monet.space.kernel.machines.ttm.behavior;

import com.google.inject.Inject;
import org.monet.bpi.BusinessUnit;
import org.monet.metamodel.ServiceDefinition.CustomerProperty;
import org.monet.metamodel.ServiceDefinitionBase.CustomerPropertyBase.CustomerResponseProperty;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.machines.ttm.CourierService;
import org.monet.space.kernel.machines.ttm.model.Customer;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceHandler;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.model.MailBoxUri;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.TaskOrder;

import java.util.Date;

public class CustomerBehavior extends Behavior {

	@Inject
	private CourierService courierService;
	@Inject
	private AgentNotifier agentNotifier;

	private String taskId;
	private CustomerProperty declaration;
	private Customer model;
	public static final String NAME = "customer";

	public void inject(String taskId, CustomerProperty declaration, Customer model, PersistenceHandler persistenceHandler, PersistenceService persistenceService) {
		this.taskId = taskId;
		this.declaration = declaration;
		this.model = model;
		this.persistenceHandler = persistenceHandler;
		this.persistenceService = persistenceService;
	}

	public void initialize(MailBoxUri localMailBox, MailBoxUri clientMailBox, Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent, String partnerContext) {
		this.model.setLocalMailBox(localMailBox);
		this.model.addClientMailBox(clientMailBox);

		TaskOrder taskOrder = this.persistenceService.createTaskOrder(this.taskId, NAME, TaskOrder.Type.customer, suggestedStartDate, suggestedEndDate, comments, urgent, partnerContext);

		this.model.setOrderId(taskOrder.getId());
		this.persistenceHandler.save();

		MonetEvent event = new MonetEvent(MonetEvent.TASK_CUSTOMER_INITIALIZED, null, this.taskId);
		this.agentNotifier.notify(event);
	}

	public void addClientMailBox(MailBoxUri clientMailBox) {
		this.model.addClientMailBox(clientMailBox);
	}

	public void process(Message message) {
		String code = message.getSubject();

		if (message.isChat()) {
			this.processChat(message);
			return;
		}

		if (!checkRequestMessageCode(code)) {
			throw new RuntimeException("Message request code '" + code + "' not recognized in customer for task " + this.taskId);
		}

		MonetEvent event = new MonetEvent(MonetEvent.TASK_CUSTOMER_REQUEST_IMPORT, null, this.taskId);
		event.addParameter(MonetEvent.PARAMETER_CODE, code);
		event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
		this.agentNotifier.notify(event);
	}

	private boolean checkRequestMessageCode(String code) {
		return declaration.getRequestByCode(code) != null;
	}

	private void processChat(Message message) {

		this.persistenceService.addTaskOrderChatItem(this.taskId, NAME, message.getContent());

		MonetEvent event = new MonetEvent(MonetEvent.TASK_ORDER_CHAT_MESSAGE_RECEIVED, null, this.taskId);
		event.addParameter(MonetEvent.PARAMETER_PROVIDER, NAME);
		event.addParameter(MonetEvent.PARAMETER_CODE, NAME);
		event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
		this.agentNotifier.notify(event);
	}

	public void send(String responseName) {
		CustomerResponseProperty response = this.declaration.getResponseMap().get(responseName);

		Message message = new Message();
		message.setSubject(response.getCode());

		try {
			MonetEvent event = new MonetEvent(MonetEvent.TASK_CUSTOMER_RESPONSE_CONSTRUCTOR, null, this.taskId);
			event.addParameter(MonetEvent.PARAMETER_CODE, message.getSubject());
			event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
			this.agentNotifier.notify(event);
		}
		catch (Throwable exception) {
			this.model.setFailureDate(new Date());
			this.persistenceHandler.save();
			throw exception;
		}

		for (MailBoxUri mailBox : this.model.getClientMailBoxes()) {
			message.setTo(mailBox.toString());

            if (mailBox.getPartner().equals(BusinessUnit.getName()))
                this.courierService.sendDelayed(this.model.getOrderId(), message);
			else
                this.courierService.send(this.model.getOrderId(), message);
		}
	}

	public void expire() {
		this.finish(MonetEvent.TASK_CUSTOMER_EXPIRED);
	}

	public void abort() {
		this.finish(MonetEvent.TASK_CUSTOMER_ABORTED);
	}

	private void finish(String eventCode) {
		this.markAsCompleted(this.model.getOrderId());

		this.courierService.destroyMailBox(this.model.getLocalMailBox());
		this.persistenceHandler.save();

		MonetEvent event = new MonetEvent(eventCode, null, this.taskId);
		this.agentNotifier.notify(event);
	}

	public MailBoxUri getClientMailBox() {

        if (this.model.getClientMailBoxes().size() == 0)
            return null;

		return this.model.getClientMailBoxes().get(0);
	}

	public MailBoxUri getLocalMailBox() {
		return this.model.getLocalMailBox();
	}

	public String getOrderId() {
		return this.model.getOrderId();
	}

	public void chat(String id, String messageContent) {
		Message message = new Message();
		message.setType(Message.Type.CHAT);
		message.setSubject(id);
		message.setContent(messageContent);

		for (MailBoxUri clientMailBox : this.model.getClientMailBoxes()) {
			message.setTo(clientMailBox.toString());

			this.courierService.send(this.model.getOrderId(), message);
		}
	}

}
