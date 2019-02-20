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

package org.monet.space.explorer.control;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.explorer.ApplicationExplorer;
import org.monet.space.explorer.configuration.Configuration;
import org.monet.space.explorer.control.actions.Action;
import org.monet.space.explorer.control.dialogs.constants.Operation;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.control.exceptions.UserNotLoggedException;
import org.monet.space.explorer.injection.InjectorFactory;
import org.monet.space.explorer.model.ComponentProvider;
import org.monet.space.explorer.model.Error;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.BaseException;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Language;
import org.monet.space.kernel.model.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;
	private static final String SENDER_AJAX = "ajax";

	public Controller() {
		super();
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
		String mapping = request.getServletPath().replaceAll(Strings.BAR45, Strings.EMPTY);

		if (mapping.equals(Operation.LOADING)) return Operation.LOADING;
		if (mapping.equals(Operation.HOME)) return Operation.HOME;
		if ((mapping.equals(ApplicationExplorer.NAME)) || (mapping.equals(ApplicationExplorer.SERVLET_NAME)) || (mapping.equals(ApplicationExplorer.HOME_NAME)))
			return Operation.HOME;

		String operation = LibraryRequest.getParameter(Parameter.OPERATION, request);
		if (operation == null && ServletFileUpload.isMultipartContent(request))
			return Operation.UPLOAD;

		return operation;
	}

	private void redirectToPage(String page, HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setAttribute(Parameter.PAGE, page);
		Action actionRedirect = getActionsFactory().create(Operation.REDIRECT, request, response);
		actionRedirect.execute();
	}

	private Boolean doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operation, sender;
		Action action;
		String idSession = request.getSession().getId();
		Context context = Context.getInstance();
		Long idThread = Thread.currentThread().getId();

		try {
			context.setApplication(idThread, LibraryRequest.getRealIp(request), ApplicationExplorer.NAME, ApplicationInterface.USER);
			context.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
			context.setSessionId(idThread, idSession);
			context.setDatabaseConnectionType(idThread, Database.ConnectionTypes.AUTO_COMMIT);
			Language.fillCurrentLanguage(request);

			if (!BusinessUnit.getInstance().isInstalled()) {
				sender = LibraryRequest.getParameter(Parameter.SENDER, request);

				if ((sender != null) && (sender.equals(SENDER_AJAX)))
					response.getWriter().print(Error.BUSINESS_UNIT_NOT_INSTALLED);
				else
					redirectToPage(Configuration.PAGE_NOT_INSTALLED, request, response);

				context.clear(idThread);
				return false;
			}

			if ((!BusinessUnit.isRunning()) || (!ApplicationExplorer.isRunning())) {
				sender = LibraryRequest.getParameter(Parameter.SENDER, request);
				if ((sender != null) && (sender.equals(SENDER_AJAX)))
					response.getWriter().print(Error.BUSINESS_UNIT_STOPPED);
				else
					this.redirectToPage(Configuration.PAGE_OUT_OF_SERVICE, request, response);
				context.clear(idThread);
				return false;
			}

			if (!this.checkSession(idSession)) operation = Operation.HOME;
			else if ((operation = this.getOperation(request)) == null) {
				context.clear(idThread);
				return false;
			}

			action = getActionsFactory().create(operation, request, response);

			if (action.checkUserLogged() && !getFederationLayer(request).isLogged())
				throw new UserNotLoggedException();

			action.execute();

		} catch (BaseException exception) {
			AgentLogger.getInstance().error(exception);
			response.setStatus(500);
			response.getWriter().print(exception.toString());
			return false;
		} finally {
			context.clear(idThread);
		}

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

	private ActionsFactory getActionsFactory() {
		return InjectorFactory.get().getInstance(ActionsFactory.class);
	}

	private FederationLayer getFederationLayer(final HttpServletRequest request) {
		ComponentProvider componentProvider = InjectorFactory.get().getInstance(ComponentProvider.class);

		return componentProvider.getComponentFederation().getLayer(new FederationLayer.Configuration() {
			@Override
			public String getSessionId() {
				return request.getSession().getId();
			}

			@Override
			public String getCallbackUrl() {
				return ApplicationExplorer.getCallbackUrl();
			}

			@Override
			public String getLogoUrl() {
				return Configuration.getInstance().getFederationLogoUrl();
			}

			@Override
			public HttpServletRequest getRequest() {
				return request;
			}
		});
	}

}