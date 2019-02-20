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

package org.monet.space.setup.control.actions;

import org.monet.space.applications.library.LibraryResponse;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.setup.configuration.Configuration;
import org.monet.space.setup.core.Kernel;
import org.monet.space.setup.core.KernelImpl;
import org.monet.space.setup.presentation.user.views.ViewsFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Action {
	protected String codeLanguage;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected AgentSession agentSession;
	protected AgentLogger agentException;
	protected Kernel kernel;
	protected ActionsFactory actionsFactory;
	protected ViewsFactory viewsFactory;
	protected String idSession;

	public Action() {
		this.codeLanguage = null;
		this.request = null;
		this.response = null;
		this.agentSession = AgentSession.getInstance();
		this.agentException = AgentLogger.getInstance();
		this.actionsFactory = ActionsFactory.getInstance();
		this.viewsFactory = ViewsFactory.getInstance();
		this.kernel = new KernelImpl();
	}

	protected Boolean initLanguage() {

		this.codeLanguage = null;
		if (this.codeLanguage == null) this.codeLanguage = this.request.getHeader("Accept-Language").substring(0, 2);

		return true;
	}

	protected void launchAuthenticateAction() {
		Configuration configuration = Configuration.getInstance();
		String redirectUrlPattern = "%s?op=%s";
		LibraryResponse.sendRedirect(this.response, String.format(redirectUrlPattern, configuration.getApiUrl(), "showlogin"));
	}

	public Boolean setRequest(HttpServletRequest oRequest) {
		this.request = oRequest;
		this.idSession = oRequest.getSession().getId();
		return true;
	}

	public Boolean setResponse(HttpServletResponse oResponse) {
		this.response = oResponse;
		return true;
	}

	public Boolean initialize() {

		this.response.setContentType("text/html;charset=UTF-8");
		this.initLanguage();

		return true;
	}

	public abstract String execute() throws IOException;

}