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

import org.monet.space.office.presentation.user.renders.OfficeRender;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Dashboard;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionLoadDashboardTemplate extends Action {

	public ActionLoadDashboardTemplate() {
		super();
	}

	public String execute() {
		String code = (String) this.request.getParameter(Parameter.CODE);
		String view = (String) this.request.getParameter(Parameter.VIEW);
		OfficeRender render = null;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (code == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_SYSTEM_TEMPLATE);

		Dashboard dashboard = ComponentDatawareHouse.getInstance().getDashboardLayer().load(code);
		render = this.rendersFactory.get(dashboard, "preview.html?mode=page", this.getRenderLink(), getAccount());
		render.setParameter("view", view);

		return render.getOutput();
	}

}