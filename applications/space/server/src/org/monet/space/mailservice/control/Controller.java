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

// TODO Manejar exepciones en los adaptadores

package org.monet.space.mailservice.control;

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.applications.library.LibraryResponse;
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
import org.monet.space.mailservice.ApplicationMailService;
import org.monet.space.mailservice.control.actions.Action;
import org.monet.space.mailservice.control.actions.ActionsFactory;
import org.monet.space.mailservice.control.constants.Parameter;

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
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig oConfiguration) throws ServletException {
		super.init(oConfiguration);
	}

	private String createSession() {
		AgentSession agentSession = AgentSession.getInstance();
		String idSession = UUID.randomUUID().toString();
		org.monet.space.kernel.model.Session session = agentSession.get(idSession);

		if (session == null) {
			agentSession.add(idSession);
		}

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
			context.setApplication(idThread, LibraryRequest.getRealIp(request), ApplicationMailService.NAME, ApplicationInterface.USER);
			context.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
			context.setSessionId(idThread, idSession);
			context.setDatabaseConnectionType(idThread, Database.ConnectionTypes.AUTO_COMMIT);
			Language.fillCurrentLanguage(request);

			federationLayer = ComponentFederation.getInstance().getLayer(createConfiguration(request));

			if ((!BusinessUnit.isRunning()) || (!ApplicationMailService.isRunning())) {
				LibraryResponse.sendRedirect(response, ApplicationMailService.getConfiguration().getOutOfServiceUrl());
				context.clear(idThread);
				return false;
			}

			if ((operation = (String) parameters.get(Parameter.OPERATION)) == null) {
				context.clear(idThread);
				return false;
			}

			signature = (String) parameters.get(Parameter.SIGNATURE);
			timestamp = (String) parameters.get(Parameter.TIMESTAMP);

			if (signature == null) {
				response.setStatus(403);
				response.getWriter().println("No signature on request");
			}

			if (timestamp == null) {
				response.setStatus(403);
				response.getWriter().println("No timestamp on request");
			}

			ValidationResult validationResult = federationLayer.validateRequest(signature, Long.valueOf(timestamp), parameterList);
			if (!validationResult.isValid()) {
				response.setStatus(403);
				response.getWriter().println(validationResult.getReason());
				return false;
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
				return ApplicationMailService.getCallbackUrl();
			}

			@Override
			public String getLogoUrl() {
				return null;
			}

			@Override
			public HttpServletRequest getRequest() {
				return request;
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
