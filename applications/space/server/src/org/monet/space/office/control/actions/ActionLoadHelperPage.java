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

import org.monet.space.office.presentation.user.views.ViewHelper;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Page;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.office.presentation.user.constants.ViewType;
import org.monet.space.office.presentation.user.constants.Views;

public class ActionLoadHelperPage extends Action {
	private BusinessUnit oBusinessUnit;

	public ActionLoadHelperPage() {
		super();
		this.oBusinessUnit = BusinessUnit.getInstance();
	}

	public String execute() {
		String path = LibraryRequest.getParameter(Parameter.PATH, this.request);
		ViewHelper viewHelper;
		Page page;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		page = this.oBusinessUnit.loadHelperPage(path);

		viewHelper = (ViewHelper) this.viewsFactory.get(Views.HELPER, this.agentRender, this.codeLanguage);
		viewHelper.setType(ViewType.HELP);
		viewHelper.setTarget(page);

		page.setContent(viewHelper.execute());

		return page.toJson().toJSONString();
	}

}