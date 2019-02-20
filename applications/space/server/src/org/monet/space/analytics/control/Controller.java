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

package org.monet.space.analytics.control;

import org.monet.space.analytics.ApplicationAnalytics;
import org.monet.space.analytics.configuration.Configuration;
import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.control.actions.Action;
import org.monet.space.analytics.control.actions.Actions;
import org.monet.space.analytics.control.actions.ActionsFactory;
import org.monet.space.analytics.listeners.ListenerDatawarehouse;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.BaseException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Error;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	public Controller() {
		super();
	}

	public void init(ServletConfig configuration) throws ServletException {
		super.init(configuration);
		AgentNotifier agentNotifier = AgentNotifier.getInstance();
		agentNotifier.register("datastoremounter", ListenerDatawarehouse.class);
	}

	private Boolean checkSession(String idSession) {
		AgentSession agentSession = AgentSession.getInstance();
		Session session = agentSession.get(idSession);

		if (session == null) {
			agentSession.add(idSession);
			return false;
		}

		return true;
	}

	private String getOperation(HttpServletRequest request) {
		String operation = request.getParameter(Parameter.OPERATION);

		if (operation == null)
			operation = Actions.SHOW_APPLICATION;

		return operation;
	}

	private Boolean doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Error error;
		String operation;
		Action action;
		Context context = Context.getInstance();
		Long idThread = Thread.currentThread().getId();
		String idSession = request.getSession().getId();
		String result;
		ActionsFactory actionsFactory = ActionsFactory.getInstance();

		try {
			Language.fillCurrentLanguage(request);

			context.setApplication(idThread, LibraryRequest.getRealIp(request), ApplicationAnalytics.NAME, ApplicationInterface.USER);
			context.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
			context.setSessionId(idThread, idSession);

			if ((!BusinessUnit.isRunning()) || (!ApplicationAnalytics.isRunning())) {
				request.setAttribute(Parameter.PAGE, Configuration.PAGE_OUT_OF_SERVICE);
				Action actionRedirect = actionsFactory.get(Actions.REDIRECT, request, response);
				response.getWriter().print(actionRedirect.execute());
				context.clear(idThread);
				return false;
			}

			this.checkSession(idSession);
			if ((operation = this.getOperation(request)) == null) {
				context.clear(idThread);
				return false;
			}

			action = actionsFactory.get(operation, request, response);
			result = action.execute();
		} catch (BaseException oException) {
			error = oException.getError();
			AgentLogger.getInstance().error(oException);
			response.setStatus(500);
			response.getWriter().print(error.getCode() + Strings.COLON + Strings.SPACE + error.getMessage());
			return false;
		} finally {
			context.clear(idThread);
		}

		if (result != null)
			response.getWriter().print(result);

		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.doRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.doRequest(request, response);
	}

}