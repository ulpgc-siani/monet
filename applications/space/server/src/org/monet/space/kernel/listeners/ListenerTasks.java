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

package org.monet.space.kernel.listeners;

import org.monet.metamodel.Project;
import org.monet.metamodel.TaskDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.model.*;

public class ListenerTasks extends Listener {

	public ListenerTasks() {
	}

	@Override
	public void taskFinishing(MonetEvent event) {
		Task task = (Task) event.getSender();

		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();

		if (task.isInitializer()) {
			TaskLayer taskLayer = componentPersistence.getTaskLayer();
			Dictionary dictionary = Dictionary.getInstance();

			Project project = BusinessModel.getInstance().getProject();
			for (Ref script : project.getScript()) {
				TaskDefinition definition = dictionary.getTaskDefinition(script.getValue());
				String taskCode = definition.getCode();

				String taskId = taskLayer.getTaskIdIfUnique(taskCode, null);

				if (taskId == null)
					break;

				Task nextInitializerTask = taskLayer.loadTask(taskId);

				if (!nextInitializerTask.getState().equals(TaskState.FINISHED))
					nextInitializerTask.getProcess().resume();

				if (!nextInitializerTask.getState().equals(TaskState.FINISHED))
					break;
			}
		}

	}

	@Override
	public void taskOrderRequestSuccess(MonetEvent event) {
		String taskId = (String) event.getSender();
        MailBoxUri mailBox = (MailBoxUri) event.getParameter(MonetEvent.PARAMETER_MAILBOX);
		String userId = (String) event.getParameter(MonetEvent.PARAMETER_USER_ID);

		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		TaskLayer taskLayer = componentPersistence.getTaskLayer();
		taskLayer.loadTask(taskId).getProcess().completeDelegationAction(mailBox, userId);
	}

	@Override
	public void taskOrderRequestFailure(MonetEvent event) {
		String taskId = (String) event.getSender();

		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		TaskLayer taskLayer = componentPersistence.getTaskLayer();
		taskLayer.loadTask(taskId).getProcess().failureDelegationAction();
	}

	@Override
	public void taskOrderChatMessageSent(MonetEvent event) {
		String chatItemId = (String) event.getParameter(MonetEvent.PARAMETER_CHAT_ITEM_ID);

		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		TaskLayer taskLayer = componentPersistence.getTaskLayer();

		taskLayer.updateChatListItemStateToSent(chatItemId);
	}

}