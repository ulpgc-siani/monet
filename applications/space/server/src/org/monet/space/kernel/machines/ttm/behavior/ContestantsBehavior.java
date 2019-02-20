package org.monet.space.kernel.machines.ttm.behavior;

import com.google.inject.Inject;
import org.monet.metamodel.ActivityDefinition.ContestantsProperty;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.machines.ttm.CourierService;
import org.monet.space.kernel.machines.ttm.model.Contestants;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceHandler;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.model.MailBoxUri;
import org.monet.space.kernel.model.MonetEvent;

public class ContestantsBehavior extends Behavior {

	@Inject
	private CourierService courierService;
	@Inject
	private AgentNotifier agentNotifier;

	private String taskId;
	private ContestantsProperty declaration;
	private Contestants model;

	public void inject(String taskId, ContestantsProperty declaration, Contestants model, PersistenceHandler persistenceHandler, PersistenceService persistenceService) {
		this.taskId = taskId;
		this.declaration = declaration;
		this.model = model;
		this.persistenceHandler = persistenceHandler;
		this.persistenceService = persistenceService;
	}

	public void initialize(MailBoxUri localMailBox, MailBoxUri clientMailBox) {
		this.model.setLocalMailBox(localMailBox);
		this.model.setClientMailBox(clientMailBox);

		this.persistenceHandler.save();

		MonetEvent event = new MonetEvent(MonetEvent.TASK_CONTESTANT_INITIALIZED, null, this.taskId);
		this.agentNotifier.notify(event);
	}

	public void process(Message message) {
		String code = message.getSubject();

		MonetEvent event = new MonetEvent(MonetEvent.TASK_CONTESTANT_REQUEST_IMPORT, null, this.taskId);
		event.addParameter(MonetEvent.PARAMETER_CODE, code);
		event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
		this.agentNotifier.notify(event);
	}

	public void send(String responseName) {
//		ContestantResponseProperty response = this.declaration.getResponseMap().get(responseName);
//
//		Message message = new Message();
//		message.setTo(this.model.getClientMailBox().toString());
//		message.setSubject(response.getCode());
//
//		MonetEvent event = new MonetEvent(MonetEvent.TASK_CONTESTANT_RESPONSE_CONSTRUCTOR, null, this.taskId);
//		event.addParameter(MonetEvent.PARAMETER_CODE, message.getSubject());
//		event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
//		this.agentNotifier.notify(event);
//
//		this.courierService.send(null, message);
	}

	public MailBoxUri getClientMailBox() {
		return this.model.getClientMailBox();
	}

}
