/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.office.control.actions;

import org.monet.bpi.java.FieldBooleanImpl;
import org.monet.bpi.java.FieldDateImpl;
import org.monet.bpi.java.FieldTextImpl;
import org.monet.bpi.types.Date;
import org.monet.metamodel.internal.TaskOrderDefinition;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.agents.AgentUserClient;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.TaskAccessException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskOrder;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;

public class ActionSetupTaskDelegation extends Action {
	private TaskLayer taskLayer;
	private NodeLayer nodeLayer;
	private AgentUserClient agentUserClient;

	public ActionSetupTaskDelegation() {
		super();
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.taskLayer = componentPersistence.getTaskLayer();
		this.nodeLayer = componentPersistence.getNodeLayer();
		this.agentUserClient = AgentUserClient.getInstance();
	}

	private String getUserMessage(String message) {
		AgentUserClient agentUserClient = AgentUserClient.getInstance();
		Long threadId = Thread.currentThread().getId();
		String userMessage = agentUserClient.getMessageForUser(threadId);

		if (userMessage != null && !userMessage.isEmpty())
			message += ": " + userMessage;

		return message;
	}

	public String execute() {
		String idTask = LibraryRequest.getParameter(Parameter.ID, this.request);
		Long threadId = Thread.currentThread().getId();
		Task task;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (idTask == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SETUP_TASK_DELEGATION);

		task = this.taskLayer.loadTask(idTask);

		if (!this.componentSecurity.canWrite(task, this.getAccount()))
			throw new TaskAccessException(ErrorCode.WRITE_TASK_PERMISSIONS, task.getId());

		String orderId = task.getProcess().getCurrentProvider().getModel().getOrderId();
		TaskOrder order = this.taskLayer.loadTaskOrder(orderId);
		Node node = this.nodeLayer.loadNode(order.getSetupNodeId());
		Date startDate = FieldDateImpl.get(node.getAttribute(TaskOrderDefinition.SuggestedStartDateProperty.CODE));
		Date endDate = FieldDateImpl.get(node.getAttribute(TaskOrderDefinition.SuggestedEndDateProperty.CODE));

		order.setSuggestedStartDate(startDate != null ? startDate.getValue() : null);
		order.setSuggestedEndDate(endDate != null ? endDate.getValue() : null);
		order.setComments(FieldTextImpl.get(node.getAttribute(TaskOrderDefinition.CommentsProperty.CODE)));
		order.setUrgent(FieldBooleanImpl.get(node.getAttribute(TaskOrderDefinition.UrgentProperty.CODE)));

		this.taskLayer.saveTaskOrder(order);
		task.getProcess().setupDelegationAction();

		agentUserClient.clear(threadId);

		return this.getUserMessage(Language.getInstance().getMessage(MessageCode.TASK_SETUP_FINISHED));
	}

}