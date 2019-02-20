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

package org.monet.space.office.control;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.monet.space.analytics.configuration.Configuration;
import org.monet.space.office.control.actions.ActionsFactory;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.BaseException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Error;
import org.monet.space.office.ApplicationOffice;
import org.monet.space.office.control.actions.Action;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.control.constants.RequestSender;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.layer.Layer;
import org.monet.space.applications.library.LibraryRequest;

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

	public void init(ServletConfig configuration) throws ServletException {
		super.init(configuration);
		Layer.getInstance();
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
		String mapping;
		String operation = null;

		mapping = request.getServletPath().replaceAll(Strings.BAR45, Strings.EMPTY);

		if (mapping.equals(Actions.SHOW_LOADING)) return Actions.SHOW_LOADING;
		if (mapping.equals(Actions.SHOW_APPLICATION)) return Actions.SHOW_APPLICATION;
		if ((mapping.equals(ApplicationOffice.NAME)) || (mapping.equals(ApplicationOffice.SERVLET_NAME)))
			return Actions.SHOW_APPLICATION;

		operation = LibraryRequest.getParameter(Parameter.OPERATION, request);
		if (operation == null) {
			if (ServletFileUpload.isMultipartContent(request)) return Actions.UPLOAD_NODE_CONTENT;
			else return null;
		}

		return (String) operation;
	}

	private void redirectToPage(String page, HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setAttribute(Parameter.PAGE, page);
		Action actionRedirect = ActionsFactory.getInstance().get(Actions.REDIRECT, request, response);
		actionRedirect.execute();
	}

	private Boolean doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Error error;
		String result, operation, sender;
		Action action;
		String idSession = request.getSession().getId();
		Context context = Context.getInstance();
		Long idThread = Thread.currentThread().getId();

		try {
			context.setApplication(idThread, LibraryRequest.getRealIp(request), ApplicationOffice.NAME, ApplicationInterface.USER);
			context.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
			context.setSessionId(idThread, idSession);
			context.setDatabaseConnectionType(idThread, Database.ConnectionTypes.AUTO_COMMIT);
			Language.fillCurrentLanguage(request);

			if (!BusinessUnit.getInstance().isInstalled()) {
				sender = LibraryRequest.getParameter(Parameter.SENDER, request);
				if ((sender != null) && (sender.equals(RequestSender.AJAX)))
					response.getWriter().print(ErrorCode.BUSINESSUNIT_NOT_INSTALLED);
				else
					this.redirectToPage(Configuration.PAGE_NOT_INSTALLED, request, response);
				context.clear(idThread);
				return false;
			}

			if ((!BusinessUnit.isRunning()) || (!ApplicationOffice.isRunning())) {
				sender = LibraryRequest.getParameter(Parameter.SENDER, request);
				if ((sender != null) && (sender.equals(RequestSender.AJAX)))
					response.getWriter().print(ErrorCode.BUSINESSUNIT_STOPPED);
				else
					this.redirectToPage(Configuration.PAGE_OUT_OF_SERVICE, request, response);
				context.clear(idThread);
				return false;
			}

			if (!this.checkSession(idSession)) operation = Actions.SHOW_APPLICATION;
			else if ((operation = this.getOperation(request)) == null) {
				context.clear(idThread);
				return false;
			}

			action = ActionsFactory.getInstance().get(operation, request, response);
			result = action.execute();
		} catch (BaseException exception) {
			error = exception.getError();
			AgentLogger.getInstance().error(exception);
			response.setStatus(500);
			response.getWriter().print(error.getCode() + Strings.COLON + Strings.SPACE + error.getMessage());
			return false;
		} finally {
			context.clear(idThread);
		}

		if (result != null) response.getWriter().print(result);

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