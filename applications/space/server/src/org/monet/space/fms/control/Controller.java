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

package org.monet.space.fms.control;

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.fms.ApplicationFms;
import org.monet.space.fms.control.actions.Action;
import org.monet.space.fms.control.actions.ActionsFactory;
import org.monet.space.fms.control.constants.Parameter;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.BaseException;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Error;
import org.monet.space.kernel.model.Language;

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

	private String getOperation(HttpServletRequest request) {
		return request.getParameter(Parameter.OPERATION);
	}

	private Boolean doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AgentLogger oAgentException = AgentLogger.getInstance();
		Error oError;
		String sResult, sOperation;
		Action oAction;
		String idSession = request.getSession().getId();
		Context context = Context.getInstance();
		Long idThread = Thread.currentThread().getId();

		try {
			context.setApplication(idThread, LibraryRequest.getRealIp(request), ApplicationFms.NAME, ApplicationInterface.USER);
			context.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
			context.setSessionId(idThread, idSession);
			Language.fillCurrentLanguage(request);

			if ((sOperation = this.getOperation(request)) == null) {
				context.clear(idThread);
				return false;
			}

			oAction = ActionsFactory.getInstance().get(sOperation, request, response);
			sResult = oAction.execute();
		} catch (BaseException oException) {
			oAgentException.error(oException);
			oError = oException.getError();
			response.getWriter().print(oError.getCode() + Strings.COLON + Strings.SPACE + oError.getMessage());
			return false;
		} finally {
			context.clear(idThread);
		}

		if (sResult != null) response.getWriter().print(sResult);

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