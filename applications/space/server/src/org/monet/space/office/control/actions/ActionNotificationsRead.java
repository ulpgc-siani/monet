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
import org.monet.space.kernel.components.layers.NotificationLayer;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.applications.library.LibraryRequest;

public class ActionNotificationsRead extends Action {

	public String execute() {
		String sIds = LibraryRequest.getParameter(Parameter.IDS, this.request);

		NotificationLayer notificationLayer = ComponentPersistence.getInstance().getNotificationLayer();

		StringBuilder builder = new StringBuilder();
		String[] ids = sIds.split(",");
		for (String id : ids) {
			if (id == null || id.isEmpty())
				continue;

			builder.append(Integer.parseInt(id));
			builder.append(",");
		}
		if (builder.length() == 0)
			return "done";

		builder.delete(builder.length() - 1, builder.length());

		notificationLayer.markAsRead(builder.toString());

		return "done";
	}

}