package org.monet.space.kernel.machines.ttm.behavior;

import com.google.inject.Inject;
import org.monet.metamodel.TaskContestProperty;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.machines.ttm.CourierService;
import org.monet.space.kernel.machines.ttm.model.Contest;
import org.monet.space.kernel.machines.ttm.model.MailBox.Type;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Signaling;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceHandler;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.model.MonetEvent;

public class ContestBehavior extends Behavior {

	@Inject
	private CourierService courierService;
	@Inject
	private AgentNotifier agentNotifier;

	private String taskId;
	private TaskContestProperty declaration;
	private Contest model;

	public void inject(String taskId, TaskContestProperty declaration, Contest model, PersistenceHandler persistenceHandler, PersistenceService persistenceService) {
		this.taskId = taskId;
		this.declaration = declaration;
		this.model = model;
		this.persistenceHandler = persistenceHandler;
		this.persistenceService = persistenceService;
	}

	public Contest getModel() {
		return this.model;
	}

	public void initialize(String taskId, TaskContestProperty declaration, String contestTaskId) {
		if (this.model.isOpen()) {
			throw new RuntimeException("Collaborator already open.");
		}

		this.taskId = taskId;
		this.declaration = declaration;

		this.model.setLocalMailBox(this.courierService.createMailBox(declaration.getCode(), this.taskId, Type.CONTEST, null).toUri());

		int i = 0;
		// TODO: Resolver con que tarea "enganchar" si existe.
		// 1.- Por tipo: Se buscan tareas de este tipo. Si solo hay una, se engancha
		// con esa.
		// 2.- Por tipo+tag: Se calcula el tag y se buscan de ese tipo con ese tag.
		// Si solo hay una, se engancha con esa.
		// 3.- Usuario: Si existe más de una candidata, se le da a elegir al
		// usuario. Si no hay ninguna, se le muestra el mensaje de "cree una nueva".

		this.model.setTaskMailBox(this.courierService.openChannel(this.model.getLocalMailBox(), contestTaskId));
		this.model.setOpen(true);
		this.persistenceHandler.save();

		MonetEvent event = new MonetEvent(MonetEvent.TASK_CONTEST_INITIALIZED, null, this.taskId);
		event.addParameter(MonetEvent.PARAMETER_CONTEST, declaration.getName());
		this.agentNotifier.notify(event);
	}

	public void process(Message message) {
		String code = message.getSubject();

		MonetEvent event = new MonetEvent(MonetEvent.TASK_CONTEST_RESPONSE_IMPORT, null, this.taskId);
		event.addParameter(MonetEvent.PARAMETER_CONTEST, declaration.getName());
		event.addParameter(MonetEvent.PARAMETER_CODE, code);
		event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
		this.agentNotifier.notify(event);
	}

	public void send(String requestName) throws Throwable {
// CONTESTANTS
//		TaskRequestProperty request = this.declaration.getRequestMap().get(requestName);
//
//		Message message = new Message();
//		message.setTo(this.model.getTaskMailBox().toString());
//		message.setSubject(request.getCode());
//
//		try {
//			MonetEvent event = new MonetEvent(MonetEvent.TASK_CONTEST_REQUEST_CONSTRUCTOR, null, this.taskId);
//			event.addParameter(MonetEvent.PARAMETER_CONTEST, declaration.getName());
//			event.addParameter(MonetEvent.PARAMETER_CODE, message.getSubject());
//			event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
//			this.agentNotifier.notify(event);
//		}
//		catch (Throwable exception) {
//			this.model.setFailureDate(new Date());
//			this.persistenceHandler.save();
//			throw exception;
//		}
//
//		this.courierService.send(null, message);
	}

	public void terminate() {
		this.finish(MonetEvent.TASK_CONTEST_TERMINATED);
	}

	private void finish(String eventCode) {
		this.markAsCompleted(this.model.getOrderId());

		this.model.reset();
		this.model.setOpen(false);
		this.courierService.destroyMailBox(this.model.getLocalMailBox());
		this.persistenceHandler.save();

		MonetEvent event = new MonetEvent(eventCode, null, this.taskId);
		event.addParameter(MonetEvent.PARAMETER_CONTEST, declaration.getName());
		this.agentNotifier.notify(event);
	}

	public void abort() {
		String orderId = this.model.getOrderId();
		this.markAsCompleted(orderId);
		this.courierService.signaling(orderId, this.model.getTaskMailBox(), Signaling.ABORT);
	}

}
