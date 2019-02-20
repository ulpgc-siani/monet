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

package org.monet.space.setup.control;

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.BaseException;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Error;
import org.monet.space.kernel.model.Language;
import org.monet.space.kernel.model.Session;
import org.monet.space.setup.ApplicationSetup;
import org.monet.space.setup.control.actions.Action;
import org.monet.space.setup.control.actions.ActionsFactory;
import org.monet.space.setup.control.constants.Actions;
import org.monet.space.setup.control.constants.Parameter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Controller extends javax.servlet.http.HttpServlet implements
	javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	public Controller() {
		super();
	}

	public void init(ServletConfig oConfiguration) throws ServletException {
		super.init(oConfiguration);
	}

	private Boolean checkSession(String sessionId) {
		AgentSession agentSession = AgentSession.getInstance();
		Session session = agentSession.get(sessionId);

		if (session == null) {
			agentSession.add(sessionId);
			return false;
		}

		return true;
	}

	private String getOperation(HttpServletRequest request) {
		String sMapping;

		sMapping = request.getServletPath().replaceAll(Strings.BAR45, Strings.EMPTY);

		if (sMapping.equals(Actions.SHOW_LOADING)) return Actions.SHOW_LOADING;
		if (sMapping.equals(Actions.SHOW_APPLICATION)) return Actions.SHOW_APPLICATION;
		if ((sMapping.equals(ApplicationSetup.NAME)) || (sMapping.equals(ApplicationSetup.SERVLET_NAME)))
			return Actions.SHOW_APPLICATION;

		return (String) request.getParameter(Parameter.OPERATION);
	}

	private Boolean request(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AgentLogger agentException = AgentLogger.getInstance();
		Error error;
		String result, operation;
		Action action;
		String sessionId = request.getSession().getId();
		Context context = Context.getInstance();
		Long threadId = Thread.currentThread().getId();

		try {
			context.setApplication(threadId, LibraryRequest.getRealIp(request), ApplicationSetup.NAME, ApplicationInterface.USER);
			context.setUserServerConfig(threadId, request.getServerName(), request.getContextPath(), request.getServerPort());
			context.setSessionId(threadId, sessionId);
			context.setDatabaseConnectionType(threadId, Database.ConnectionTypes.AUTO_COMMIT);
			Language.fillCurrentLanguage(request);

			if (!this.checkSession(sessionId)) operation = Actions.SHOW_APPLICATION;
			else if ((operation = this.getOperation(request)) == null) {
				context.clear(threadId);
				return false;
			}

			action = ActionsFactory.getInstance().get(operation, request, response);
			result = action.execute();
		} catch (BaseException oException) {
			agentException.error(oException);
			error = oException.getError();
			response.getWriter().print(error.getCode() + Strings.COLON + Strings.SPACE + error.getMessage());
			return false;
		} finally {
			context.clear(threadId);
		}

		if (result != null) response.getWriter().print(result);

		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.request(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.request(request, response);
	}

}