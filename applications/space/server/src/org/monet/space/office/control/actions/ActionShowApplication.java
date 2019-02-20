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

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.applications.library.LibraryResponse;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Task;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.presentation.user.constants.Views;
import org.monet.space.office.presentation.user.views.ViewApplication;
import org.monet.space.office.presentation.user.views.ViewLogout;
import org.monet.space.office.presentation.user.views.ViewUpdatingSpace;
import org.monet.space.office.presentation.user.views.ViewsFactory;

import java.io.IOException;

public class ActionShowApplication extends Action {
	private TaskLayer taskLayer;

	public ActionShowApplication() {
		super();
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	public String execute() throws IOException {
		String action = LibraryRequest.getParameter(Parameter.ACTION, this.request);
		String verifier = LibraryRequest.getParameter(Parameter.VERIFIER, this.request);
		String message = null;
		ViewApplication viewApplication;
		ViewLogout viewLogout;
		ViewUpdatingSpace viewUpdatingSpace;
		Account account;
		FederationLayer layer = this.getFederationLayer();

		if (action != null && action.equals(Actions.LOGIN)) {
			layer.login(verifier);
			LibraryResponse.sendRedirect(this.response, LibraryRequest.getRequestURL(this.request));
			return null;
		}

		if (layer.isLogged()) {
			account = layer.loadAccount();

			if (!account.hasPermissions()) {
				viewLogout = (ViewLogout) ViewsFactory.getInstance().get(Views.LOGOUT, this.agentRender, this.codeLanguage);
				viewLogout.setBusinessUnit(BusinessUnit.getInstance());
				viewLogout.setTarget(message);
				viewLogout.execute(this.response.getWriter());
				return null;
			}

			Task currentInitializerTask = this.taskLayer.getCurrentInitializerTask();
			if (currentInitializerTask != null && !account.canResolveInitializerTask(currentInitializerTask)) {
				viewUpdatingSpace = (ViewUpdatingSpace) ViewsFactory.getInstance().get(Views.UPDATING_SPACE, this.agentRender, this.codeLanguage);
				viewUpdatingSpace.setBusinessUnit(BusinessUnit.getInstance());
				viewUpdatingSpace.setTarget(message);
				viewUpdatingSpace.execute(this.response.getWriter());
				return null;
			}

			viewApplication = (ViewApplication) ViewsFactory.getInstance().get(Views.APPLICATION, this.agentRender, this.codeLanguage);

			viewApplication.execute(this.response.getWriter());
		} else {
			String authUrl = layer.getAuthorizationUrl();
			authUrl += (authUrl.indexOf("?") != -1) ? "&" : "?";
			authUrl += this.request.getQueryString();
			LibraryResponse.sendRedirect(this.response, authUrl);
		}

		return null;
	}

}