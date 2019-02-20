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

import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NotificationLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.NotificationList;

public class ActionLoadNotifications extends Action {
	private NotificationLayer notificationLayer;

	public ActionLoadNotifications() {
		super();
		this.notificationLayer = ComponentPersistence.getInstance().getNotificationLayer();
	}

	public String execute() {
		String start = LibraryRequest.getParameter(Parameter.START, this.request);
		String limit = LibraryRequest.getParameter(Parameter.LIMIT, this.request);
		Account account = this.getAccount();
		NotificationList notificationList;
		int startValue, limitValue;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		try {
			startValue = Integer.valueOf(start);
		} catch (NumberFormatException oException) {
			startValue = 0;
		} catch (ClassCastException oException) {
			startValue = 0;
		}

		try {
			limitValue = Integer.valueOf(limit);
		} catch (NumberFormatException oException) {
			limitValue = 10;
		} catch (ClassCastException oException) {
			limitValue = 10;
		}

		notificationList = this.notificationLayer.loadNotificationList(account.getUser().getId(), startValue, limitValue);

		return notificationList.toJson().toJSONString();
	}

}