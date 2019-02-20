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

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.machines.ttm.behavior.CustomerBehavior;
import org.monet.space.kernel.machines.ttm.behavior.ProviderBehavior;
import org.monet.space.kernel.model.ChatItem;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskOrder;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;

public class ActionSendTaskOrderChatMessage extends Action {
	private TaskLayer taskLayer;

	public ActionSendTaskOrderChatMessage() {
		super();
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String idOrder = LibraryRequest.getParameter(Parameter.ORDER, this.request);
		String message = LibraryRequest.getParameter(Parameter.MESSAGE, this.request);
		boolean itemSent = false;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (id == null || idOrder == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SEND_TASK_ORDER_CHAT_MESSAGE);

		ChatItem chatItem = new ChatItem();
		chatItem.setType(ChatItem.Type.out);
		chatItem.setMessage(message);

		Task task = this.taskLayer.loadTask(id);
		if (!this.componentSecurity.canWrite(task, this.getAccount()))
			return ErrorCode.WRITE_TASK_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.WRITE_TASK_PERMISSIONS);

		TaskOrder order = this.taskLayer.loadTaskOrder(idOrder);
		switch (order.getType()) {
			case customer:
				CustomerBehavior customer = task.getProcess().getCustomer();
				customer.chat(chatItem.getId(), message);
				itemSent = true;
				break;
			case provider:
				ProviderBehavior provider = task.getProcess().getProvider(order.getCode());
				if (provider.isOpen()) {
					provider.chat(chatItem.getId(), message);
					itemSent = true;
				}
				break;
			case job:
				itemSent = true;
				break;
		}

		if (itemSent)
			this.taskLayer.addChatListItem(task, idOrder, chatItem);

		return "done";
	}

}