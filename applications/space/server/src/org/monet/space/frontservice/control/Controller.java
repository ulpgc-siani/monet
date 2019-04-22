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

package org.monet.space.frontservice.control;

import org.monet.http.HttpRequest;
import org.monet.http.Request;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.applications.library.LibraryResponse;
import org.monet.space.frontservice.ApplicationFrontService;
import org.monet.space.frontservice.control.actions.Action;
import org.monet.space.frontservice.control.actions.ActionsFactory;
import org.monet.space.frontservice.control.constants.Actions;
import org.monet.space.frontservice.control.constants.Parameter;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.BaseException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Error;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

public class Controller extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	public Controller() {
		super();
	}

	public void init(ServletConfig oConfiguration) throws ServletException {
		super.init(oConfiguration);
	}

	private String getOperation(HttpServletRequest request) {
		String op = request.getParameter("op");
		return ActionsFactory.getInstance().getOperation(op);
	}

	private String createSession() {
		AgentSession agentSession = AgentSession.getInstance();
		String idSession = UUID.randomUUID().toString();
		Session session = agentSession.get(idSession);

		if (session == null)
			session = agentSession.add(idSession);

		return idSession;
	}

	private Boolean doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Error error;
		String result, operation;
		Action action;
		String idSession = this.createSession();
		Context context = Context.getInstance();
		Long idThread = Thread.currentThread().getId();
		FederationLayer federationLayer;
		ArrayList<Entry<String, Object>> parameterList = LibraryRequest.parametersToList(request);
		HashMap<String, Object> parameters = LibraryRequest.parametersToMap(parameterList);
		String signature, timestamp;

		try {
			context.setApplication(idThread, LibraryRequest.getRealIp(request), ApplicationFrontService.NAME, ApplicationInterface.USER);
			context.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
			context.setSessionId(idThread, idSession);
			context.setDatabaseConnectionType(idThread, Database.ConnectionTypes.AUTO_COMMIT);

			federationLayer = ComponentFederation.getInstance().getLayer(createConfiguration(request));

			if ((!BusinessUnit.isRunning()) || (!ApplicationFrontService.isRunning())) {
				LibraryResponse.sendRedirect(response, ApplicationFrontService.getConfiguration().getOutOfServiceUrl());
				context.clear(idThread);
				return false;
			}

			if ((operation = this.getOperation(request)) == null) {
				context.clear(idThread);
				return false;
			}

			signature = (String) parameters.get(Parameter.SIGNATURE);
			timestamp = (String) parameters.get(Parameter.TIMESTAMP);

			if (!operation.equals(Actions.SHOW_SERVICES)) {
				if (signature == null) {
					response.setStatus(403);
					response.getWriter().println("No signature on request");
					return false;
				}

				if (timestamp == null || timestamp.isEmpty()) {
					response.setStatus(403);
					response.getWriter().println("No timestamp on request");
					return false;
				}

				ValidationResult validationResult = federationLayer.validateRequest(signature, Long.valueOf(timestamp), parameterList);
				if (!validationResult.isValid()) {
					response.setStatus(403);
					response.getWriter().println(validationResult.getReason());
					return false;
				}
			}

			action = ActionsFactory.getInstance().get(operation, request, response, parameters);
			result = action.execute();
		} catch (BaseException oException) {
			error = oException.getError();
			AgentLogger.getInstance().error(oException);
			response.setStatus(500);
			response.getWriter().print(error.getCode() + Strings.COLON + Strings.SPACE + error.getMessage());
			return false;
		} finally {
			context.clear(idThread);
			AgentSession.getInstance().remove(idSession);
		}

		if (result != null) response.getWriter().print(result);

		return true;
	}

	private FederationLayer.Configuration createConfiguration(final HttpServletRequest request) {
		return new FederationLayer.Configuration() {
			@Override
			public String getSessionId() {
				return request.getSession().getId();
			}

			@Override
			public String getCallbackUrl() {
				return ApplicationFrontService.getCallbackUrl();
			}

			@Override
			public String getLogoUrl() {
				return null;
			}

			@Override
			public Request getRequest() {
				return new HttpRequest(request);
			}
		};
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